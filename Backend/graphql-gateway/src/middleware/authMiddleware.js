'use strict';

const { GraphQLError } = require('graphql');
const { verifyToken }  = require('../clients/authClient');

/**
 * Extrae el token del header Authorization: Bearer <token>
 */
function extractToken(req) {
  const authHeader = req?.headers?.authorization ?? '';
  if (authHeader.startsWith('Bearer ')) {
    return authHeader.slice(7).trim();
  }
  return null;
}

/**
 * Operaciones que NO requieren autenticación.
 * Permite que el Playground y las queries de instrospección funcionen sin token.
 */
const PUBLIC_OPERATIONS = new Set([
  'IntrospectionQuery',
  '__schema',
]);

/**
 * Función de contexto de Apollo Server.
 * Se ejecuta en cada request HTTP y cada conexión WebSocket.
 *
 * - Para HTTP: extrae el token del header, llama ms-auth/auth/verify
 * - Para WebSocket: extrae el token del connectionParams
 *
 * Inyecta en el contexto GraphQL: { token, user: { userId, email, rol } }
 */
async function buildContext({ req, connectionParams }) {
  // ── WebSocket (subscriptions) ────────────────────────────────────────────
  if (connectionParams) {
    const wsToken =
      connectionParams.authorization?.replace('Bearer ', '') ||
      connectionParams.Authorization?.replace('Bearer ', '') ||
      connectionParams.token;

    if (!wsToken) {
      throw new GraphQLError('Token requerido para subscriptions', {
        extensions: { code: 'UNAUTHENTICATED' },
      });
    }

    try {
      const user = await verifyToken(wsToken);
      return { token: wsToken, user };
    } catch {
      throw new GraphQLError('Token inválido o expirado', {
        extensions: { code: 'UNAUTHENTICATED' },
      });
    }
  }

  // ── HTTP (queries y mutations) ───────────────────────────────────────────
  const token = extractToken(req);

  // Permitir introspección sin token (para el Playground)
  const operationName = req?.body?.operationName;
  if (PUBLIC_OPERATIONS.has(operationName) || !operationName) {
    // Contexto vacío para introspección
    if (!token) return { token: null, user: null };
  }

  if (!token) {
    throw new GraphQLError('Token requerido. Agrega: Authorization: Bearer <token>', {
      extensions: { code: 'UNAUTHENTICATED' },
    });
  }

  try {
    const user = await verifyToken(token);
    return { token, user };
  } catch (err) {
    throw new GraphQLError(err.message ?? 'Token inválido o expirado', {
      extensions: { code: 'UNAUTHENTICATED' },
    });
  }
}

module.exports = { buildContext, extractToken };
