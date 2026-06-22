'use strict';

const amqp = require('amqplib');

const RABBITMQ_URL      = process.env.RABBITMQ_URL      ?? 'amqp://logiflow:logiflow2026@localhost:5672';
const RABBITMQ_QUEUE    = process.env.RABBITMQ_QUEUE    ?? 'q.seguimiento.posicion';
const RABBITMQ_EXCHANGE = process.env.RABBITMQ_EXCHANGE ?? 'logistica.topic';
const ROUTING_KEY       = 'posicion.actualizada';

const RETRY_DELAY_MS = 5000;
const MAX_RETRIES    = 12; // 1 minuto total de reintentos

/**
 * Conecta a RabbitMQ con reintentos y empieza a consumir la cola
 * q.seguimiento.posicion.
 *
 * @param {function} onPosicion - callback(payload) llamado por cada mensaje recibido
 */
async function connectRabbitMQ(onPosicion) {
  for (let intento = 1; intento <= MAX_RETRIES; intento++) {
    try {
      const connection = await amqp.connect(RABBITMQ_URL);
      const channel    = await connection.createChannel();

      // Declarar exchange y cola (idempotente — ya existen en definitions.json)
      await channel.assertExchange(RABBITMQ_EXCHANGE, 'topic', { durable: true });
      await channel.assertQueue(RABBITMQ_QUEUE, { durable: true });
      await channel.bindQueue(RABBITMQ_QUEUE, RABBITMQ_EXCHANGE, ROUTING_KEY);

      // Procesar un mensaje a la vez
      channel.prefetch(1);

      console.log(`[RabbitMQ] Escuchando cola: ${RABBITMQ_QUEUE}`);

      channel.consume(RABBITMQ_QUEUE, (msg) => {
        if (!msg) return;

        try {
          const payload = JSON.parse(msg.content.toString());
          console.log(`[RabbitMQ] Mensaje recibido: envioId=${payload.envioId}`);
          onPosicion(payload);
          channel.ack(msg);
        } catch (parseErr) {
          console.error('[RabbitMQ] Error parseando mensaje:', parseErr.message);
          // nack sin reencolar para evitar ciclos con mensajes malformados
          channel.nack(msg, false, false);
        }
      });

      // Reconexión automática si se cierra la conexión
      connection.on('close', () => {
        console.warn('[RabbitMQ] Conexión cerrada. Reconectando...');
        setTimeout(() => connectRabbitMQ(onPosicion), RETRY_DELAY_MS);
      });

      return; // Conexión exitosa

    } catch (err) {
      console.warn(`[RabbitMQ] Intento ${intento}/${MAX_RETRIES} fallido: ${err.message}`);
      if (intento === MAX_RETRIES) throw err;
      await delay(RETRY_DELAY_MS);
    }
  }
}

const delay = (ms) => new Promise((res) => setTimeout(res, ms));

module.exports = { connectRabbitMQ };
