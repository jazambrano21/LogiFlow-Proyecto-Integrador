'use strict';

const { io }        = require('socket.io-client');
const { PubSub }    = require('graphql-subscriptions');
const posicionCache = require('./posicionCache');

/**
 * SeguimientoManager
 *
 * Gestiona la conexión del gateway como cliente Socket.IO hacia ms-seguimiento.
 * - Mantiene una única conexión WebSocket compartida para todos los envíos.
 * - Por cada envioId con suscriptores activos, hace `subscribe` en ms-seguimiento.
 * - Los mensajes recibidos se publican al PubSub de GraphQL y se cachean.
 *
 * Flujo:
 *   cliente GraphQL → subscription posicionEnvio(envioId)
 *     → SeguimientoManager.suscribir(envioId)
 *       → socket.emit('subscribe', { envioId })
 *         → ms-seguimiento → evento 'posicion'
 *           → pubSub.publish(`POSICION_${envioId}`, posicion)
 *             → cliente GraphQL recibe la actualización
 */
class SeguimientoManager {
  constructor() {
    /** PubSub interno de Apollo para los resolvers de subscripción */
    this.pubSub = new PubSub();

    /** Socket.IO hacia ms-seguimiento */
    this.socket = null;

    /** Contador de suscriptores activos por envioId */
    this.contadores = new Map();

    /** Flag para no intentar reconexión en paralelo */
    this._conectando = false;
  }

  /**
   * Inicializa la conexión Socket.IO con ms-seguimiento.
   * Se llama una vez al arrancar el gateway.
   */
  connect() {
    const wsUrl = process.env.MS_SEGUIMIENTO_WS_URL ?? 'http://localhost:3005';

    console.log(`[SeguimientoManager] Conectando a ms-seguimiento en ${wsUrl}`);

    this.socket = io(wsUrl, {
      transports: ['websocket'],
      reconnection: true,
      reconnectionDelay: 3000,
      reconnectionAttempts: 20,
    });

    this.socket.on('connect', () => {
      console.log('[SeguimientoManager] Conectado a ms-seguimiento');
      // Re-suscribir a todos los envíos activos en caso de reconexión
      for (const envioId of this.contadores.keys()) {
        this.socket.emit('subscribe', { envioId });
      }
    });

    this.socket.on('disconnect', (reason) => {
      console.warn(`[SeguimientoManager] Desconectado de ms-seguimiento: ${reason}`);
    });

    this.socket.on('connect_error', (err) => {
      console.error(`[SeguimientoManager] Error de conexión: ${err.message}`);
    });

    /**
     * Evento 'posicion' recibido desde ms-seguimiento.
     * Estructura: { envioId, lat, lng, velocidad, eta, timestamp }
     */
    this.socket.on('posicion', (posicion) => {
      const envioId = String(posicion.envioId);
      const payload = {
        ...posicion,
        envioId,
        lat:       posicion.lat,
        lng:       posicion.lng,
        velocidad: posicion.velocidad ?? null,
        eta:       posicion.eta ?? null,
        timestamp: posicion.timestamp ?? new Date().toISOString(),
      };

      // Actualizar cache para el campo ultimaPosicion de type Envio
      posicionCache.guardar(payload);

      // Publicar al PubSub para que los resolvers de subscripción lo reenvíen
      this.pubSub.publish(`POSICION_${envioId}`, { posicionEnvio: payload });
    });
  }

  /**
   * Registra un nuevo suscriptor para un envioId.
   * Si es el primer suscriptor, emite 'subscribe' al Socket.IO.
   */
  suscribir(envioId) {
    const key = String(envioId);
    const count = (this.contadores.get(key) ?? 0) + 1;
    this.contadores.set(key, count);

    if (count === 1 && this.socket?.connected) {
      console.log(`[SeguimientoManager] Suscribiendo a envioId=${key}`);
      this.socket.emit('subscribe', { envioId: key });
    }
  }

  /**
   * Elimina un suscriptor. Cuando llega a 0 el envioId queda inactivo
   * (ms-seguimiento limpia automáticamente al desconectarse el socket).
   */
  desuscribir(envioId) {
    const key = String(envioId);
    const count = (this.contadores.get(key) ?? 1) - 1;
    if (count <= 0) {
      this.contadores.delete(key);
    } else {
      this.contadores.set(key, count);
    }
  }

  /**
   * Retorna el AsyncIterator del PubSub para el resolver de subscripción.
   */
  asyncIterator(envioId) {
    return this.pubSub.asyncIterator(`POSICION_${envioId}`);
  }
}

// Singleton compartido por toda la aplicación
const manager = new SeguimientoManager();
module.exports = manager;
