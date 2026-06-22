'use strict';

/**
 * simulator.js — Simulador de posiciones GPS para pruebas.
 *
 * Publica cada 3 segundos un evento posicion.actualizada al exchange
 * logistica.topic con datos de posición aleatoria para un envioId de prueba.
 *
 * Uso:
 *   npm run simulate
 *   npm run simulate -- --envioId=5       (envioId personalizado)
 *   npm run simulate -- --envioId=5 --count=20  (cantidad de mensajes)
 */

require('dotenv').config();

const amqp = require('amqplib');

const RABBITMQ_URL      = process.env.RABBITMQ_URL      ?? 'amqp://logiflow:logiflow2026@localhost:5672';
const RABBITMQ_EXCHANGE = process.env.RABBITMQ_EXCHANGE ?? 'logistica.topic';
const ROUTING_KEY       = 'posicion.actualizada';
const INTERVAL_MS       = 3000;

// Parsear args de línea de comandos --key=value
const args = Object.fromEntries(
  process.argv.slice(2)
    .filter(a => a.startsWith('--'))
    .map(a => {
      const [k, v] = a.slice(2).split('=');
      return [k, v ?? true];
    })
);

const TEST_ENVIO_ID = parseInt(args.envioId ?? '1', 10);
const MAX_COUNT     = args.count ? parseInt(args.count, 10) : Infinity;

// Ruta simulada: Quito → Guayaquil (aproximada)
const RUTA = [
  { lat: -0.1807, lng: -78.4678 },  // Quito
  { lat: -0.5597, lng: -78.5568 },  // Latacunga
  { lat: -1.8621, lng: -78.4800 },  // Riobamba
  { lat: -2.1710, lng: -79.5875 },  // Guayaquil
];

let routeIndex = 0;
let count = 0;

async function main() {
  let connection, channel;

  try {
    connection = await amqp.connect(RABBITMQ_URL);
    channel    = await connection.createChannel();
    await channel.assertExchange(RABBITMQ_EXCHANGE, 'topic', { durable: true });

    console.log(`[Simulator] Iniciando simulación para envioId=${TEST_ENVIO_ID}`);
    console.log(`[Simulator] Exchange: ${RABBITMQ_EXCHANGE}, routing key: ${ROUTING_KEY}`);
    console.log(`[Simulator] Intervalo: ${INTERVAL_MS}ms. Ctrl+C para detener.\n`);

    const interval = setInterval(async () => {
      if (count >= MAX_COUNT) {
        clearInterval(interval);
        await channel.close();
        await connection.close();
        console.log('[Simulator] Simulación completada.');
        return;
      }

      const punto = RUTA[routeIndex % RUTA.length];
      // Añadir variación aleatoria pequeña para simular movimiento real
      const lat = punto.lat + (Math.random() - 0.5) * 0.01;
      const lng = punto.lng + (Math.random() - 0.5) * 0.01;

      const payload = {
        envioId:   TEST_ENVIO_ID,
        lat:       parseFloat(lat.toFixed(6)),
        lng:       parseFloat(lng.toFixed(6)),
        velocidad: Math.floor(40 + Math.random() * 60),  // 40-100 km/h
        eta:       new Date(Date.now() + (RUTA.length - routeIndex) * 30 * 60 * 1000).toISOString(),
        timestamp: new Date().toISOString(),
      };

      channel.publish(
        RABBITMQ_EXCHANGE,
        ROUTING_KEY,
        Buffer.from(JSON.stringify(payload)),
        { persistent: true, contentType: 'application/json' }
      );

      console.log(
        `[Simulator] #${count + 1} envioId=${payload.envioId} ` +
        `lat=${payload.lat} lng=${payload.lng} v=${payload.velocidad}km/h`
      );

      routeIndex++;
      count++;
    }, INTERVAL_MS);

  } catch (err) {
    console.error('[Simulator] Error:', err.message);
    process.exit(1);
  }
}

main();
