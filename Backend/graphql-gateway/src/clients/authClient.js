'use strict';

const { createClient, unwrap } = require('./httpClient');

const client = createClient(process.env.MS_AUTH_URL ?? 'http://localhost:8081');

/**
 * Llama a GET /auth/verify con el token Bearer.
 * Retorna el payload { userId, email, rol } o lanza error si el token es inválido.
 */
async function verifyToken(token) {
  const response = await client.get('/auth/verify', {
    headers: { Authorization: `Bearer ${token}` },
  });
  return unwrap(response);
}

module.exports = { verifyToken };
