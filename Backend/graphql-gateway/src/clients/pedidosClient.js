'use strict';

const { createClient, unwrap } = require('./httpClient');

const client = createClient(process.env.MS_PEDIDOS_URL ?? 'http://localhost:8083');

/**
 * GET /pedidos?clienteId=X&estado=Y — lista pedidos con filtros opcionales
 */
async function listarPedidos({ clienteId, estado } = {}, token) {
  const params = {};
  if (clienteId) params.clienteId = clienteId;
  if (estado)    params.estado    = estado;

  const res = await client.get('/pedidos', { params, _token: token });
  return unwrap(res) ?? [];
}

/**
 * GET /pedidos/:id
 */
async function obtenerPedido(id, token) {
  try {
    const res = await client.get(`/pedidos/${id}`, { _token: token });
    return unwrap(res);
  } catch (err) {
    if (err.statusCode === 404) return null;
    throw err;
  }
}

/**
 * POST /pedidos — crea un pedido
 */
async function crearPedido(input, token) {
  const res = await client.post('/pedidos', input, { _token: token });
  return unwrap(res);
}

/**
 * DELETE /pedidos/:id — cancela el pedido
 */
async function cancelarPedido(id, token) {
  const res = await client.delete(`/pedidos/${id}`, { _token: token });
  return unwrap(res);
}

module.exports = { listarPedidos, obtenerPedido, crearPedido, cancelarPedido };
