'use strict';

const { createClient, unwrap } = require('./httpClient');

const client = createClient(process.env.MS_RUTEO_URL ?? 'http://localhost:8085');

/**
 * GET /envios/:id
 */
async function obtenerEnvio(id, token) {
  try {
    const res = await client.get(`/envios/${id}`, { _token: token });
    return unwrap(res);
  } catch (err) {
    if (err.statusCode === 404) return null;
    throw err;
  }
}

/**
 * PUT /envios/:id/estado
 */
async function actualizarEstadoEnvio(id, estado, token) {
  const res = await client.put(
    `/envios/${id}/estado`,
    { estado },
    { _token: token }
  );
  return unwrap(res);
}

module.exports = { obtenerEnvio, actualizarEstadoEnvio };
