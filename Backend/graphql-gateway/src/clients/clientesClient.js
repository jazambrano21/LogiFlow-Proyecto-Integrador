'use strict';

const { createClient, unwrap } = require('./httpClient');

const client = createClient(process.env.MS_CLIENTES_URL ?? 'http://localhost:8082');

/**
 * GET /clientes — lista todos los clientes
 */
async function listarClientes(token) {
  const res = await client.get('/clientes', { _token: token });
  return unwrap(res) ?? [];
}

/**
 * GET /clientes/:id — obtiene un cliente por ID
 * Retorna null si no existe (404) en lugar de lanzar error.
 */
async function obtenerCliente(id, token) {
  try {
    const res = await client.get(`/clientes/${id}`, { _token: token });
    return unwrap(res);
  } catch (err) {
    if (err.statusCode === 404) return null;
    throw err;
  }
}

module.exports = { listarClientes, obtenerCliente };
