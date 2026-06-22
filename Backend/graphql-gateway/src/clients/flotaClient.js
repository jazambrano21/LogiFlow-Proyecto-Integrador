'use strict';

const { createClient, unwrap } = require('./httpClient');

const client = createClient(process.env.MS_FLOTA_URL ?? 'http://localhost:8080');

/**
 * GET /api/vehiculos/disponibles?capacidadMinima=X
 * ms-flota-rest no requiere token en este endpoint (público en Fase 1)
 */
async function vehiculosDisponibles(capacidadMinima) {
  const params = {};
  if (capacidadMinima != null) params.capacidadMinima = capacidadMinima;

  const res = await client.get('/api/vehiculos/disponibles', { params });

  // ms-flota retorna un array directo (no envuelto en { success, data })
  if (Array.isArray(res)) return res;
  return unwrap(res) ?? [];
}

module.exports = { vehiculosDisponibles };
