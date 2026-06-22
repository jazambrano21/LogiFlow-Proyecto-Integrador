'use strict';

require('dotenv').config();

const http        = require('http');
const express     = require('express');
const cors        = require('cors');
const bodyParser  = require('body-parser');
const { WebSocketServer }       = require('ws');
const { useServer }             = require('graphql-ws/lib/use/ws');
const { ApolloServer }          = require('@apollo/server');
const { expressMiddleware }     = require('@apollo/server/express4');
const { makeExecutableSchema }  = require('@graphql-tools/schema');
const { typeDefs }              = require('./schema/typeDefs');
const { resolvers }             = require('./resolvers/index');
const { buildContext }          = require('./middleware/authMiddleware');
const seguimientoManager        = require('./seguimiento/seguimientoManager');

const PORT = parseInt(process.env.PORT ?? '4000', 10);

async function bootstrap() {

  // ── 1. Schema ejecutable (necesario para graphql-ws) ──────────────────────
  const schema = makeExecutableSchema({ typeDefs, resolvers });

  // ── 2. HTTP server + Express ──────────────────────────────────────────────
  const app        = express();
  const httpServer = http.createServer(app);

  app.use(cors({ origin: '*' }));
  app.use(bodyParser.json({ limit: '10mb' }));

  // Health check
  app.get('/health', (_, res) =>
    res.json({ status: 'ok', service: 'graphql-gateway', port: PORT })
  );

  // ── 3. WebSocket server para subscriptions ────────────────────────────────
  const wsServer = new WebSocketServer({ server: httpServer, path: '/graphql' });

  // ── 4. Apollo Server ──────────────────────────────────────────────────────
  const apolloServer = new ApolloServer({
    schema,
    introspection: true,
    plugins: [
      {
        async serverWillStart() {
          return {
            async drainServer() {
              await serverCleanup.dispose();
            },
          };
        },
      },
    ],
  });

  // graphql-ws maneja el protocolo de subscriptions sobre WebSocket
  const serverCleanup = useServer(
    {
      schema,
      context: async (ctx) => buildContext({ connectionParams: ctx.connectionParams }),
    },
    wsServer
  );

  await apolloServer.start();

  // ── 5. Montar Apollo como middleware Express ───────────────────────────────
  app.use(
    '/graphql',
    expressMiddleware(apolloServer, {
      context: async ({ req }) => buildContext({ req }),
    })
  );

  // ── 6. Conectar al WebSocket de ms-seguimiento ────────────────────────────
  seguimientoManager.connect();

  // ── 7. Arrancar servidor ──────────────────────────────────────────────────
  await new Promise((resolve) => httpServer.listen(PORT, resolve));

  console.log(`
╔══════════════════════════════════════════════════════════╗
║       GraphQL Gateway — LogiFlow Fase 2 Etapa 3          ║
╠══════════════════════════════════════════════════════════╣
║  GraphQL Sandbox:   http://localhost:${PORT}/graphql          ║
║  Health check:      http://localhost:${PORT}/health           ║
║  WebSocket (subs):  ws://localhost:${PORT}/graphql            ║
╚══════════════════════════════════════════════════════════╝
  `);
}

bootstrap().catch((err) => {
  console.error('[Gateway] Error fatal al arrancar:', err);
  process.exit(1);
});
