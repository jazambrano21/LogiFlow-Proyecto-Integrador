'use strict';

require('dotenv').config();

const { createServer } = require('http');
const { Server }       = require('socket.io');
const { connectRabbitMQ } = require('./rabbitmq/consumer');

const WS_PORT = parseInt(process.env.WS_PORT ?? '3005', 10);

// ── HTTP server + Socket.IO ───────────────────────────────────────────────
const httpServer = createServer();
const io = new Server(httpServer, {
  cors: { origin: '*' },
  transports: ['websocket', 'polling'],
});

/**
 * Mapa de suscripciones: envioId (string) → Set de socket IDs
 * Permite emitir posiciones solo a los clientes suscritos a ese envío.
 */
const suscripciones = new Map();

io.on('connection', (socket) => {
  console.log(`[WS] Cliente conectado: ${socket.id}`);

  /**
   * Evento "subscribe" del cliente:
   * Payload: { envioId }
   * El cliente se suscribe a actualizaciones de posición de un envío específico.
   */
  socket.on('subscribe', ({ envioId }) => {
    if (!envioId) {
      socket.emit('error', { message: 'envioId requerido en evento subscribe' });
      return;
    }

    const key = String(envioId);
    socket.join(key);              // Socket.IO room por envioId

    if (!suscripciones.has(key)) {
      suscripciones.set(key, new Set());
    }
    suscripciones.get(key).add(socket.id);

    console.log(`[WS] Socket ${socket.id} suscrito a envioId=${key}`);
    socket.emit('subscribed', { envioId: key, message: `Suscrito a envioId ${key}` });
  });

  /**
   * Limpieza cuando el cliente desconecta:
   * Elimina el socket de todas las suscripciones que tenía.
   */
  socket.on('disconnect', () => {
    console.log(`[WS] Cliente desconectado: ${socket.id}`);
    suscripciones.forEach((sockets, envioId) => {
      sockets.delete(socket.id);
      if (sockets.size === 0) {
        suscripciones.delete(envioId);
      }
    });
  });
});

/**
 * Emite el evento "posicion" a todos los clientes suscritos a ese envioId.
 * Llamado desde el consumer de RabbitMQ al recibir un mensaje posicion.actualizada.
 *
 * @param {object} payload - { envioId, lat, lng, velocidad, eta, timestamp }
 */
function emitirPosicion(payload) {
  const key = String(payload.envioId);
  console.log(`[WS] Emitiendo posicion a sala envioId=${key} — lat=${payload.lat}, lng=${payload.lng}`);
  io.to(key).emit('posicion', payload);
}

// ── Iniciar servidor ──────────────────────────────────────────────────────
httpServer.listen(WS_PORT, async () => {
  console.log(`[ms-seguimiento] WebSocket server escuchando en puerto ${WS_PORT}`);

  try {
    await connectRabbitMQ(emitirPosicion);
    console.log('[ms-seguimiento] Conectado a RabbitMQ y escuchando cola');
  } catch (err) {
    console.error('[ms-seguimiento] Error conectando a RabbitMQ:', err.message);
    process.exit(1);
  }
});

module.exports = { io, emitirPosicion };
