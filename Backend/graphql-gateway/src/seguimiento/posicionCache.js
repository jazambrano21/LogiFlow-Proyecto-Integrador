'use strict';

/**
 * Cache en memoria de la última posición conocida por envioId.
 * Permite responder el campo ultimaPosicion en type Envio sin abrir
 * una subscripción activa.
 *
 * Map<envioId(string), Posicion>
 */
const cache = new Map();

function guardar(posicion) {
  if (!posicion?.envioId) return;
  cache.set(String(posicion.envioId), posicion);
}

function obtener(envioId) {
  return cache.get(String(envioId)) ?? null;
}

module.exports = { guardar, obtener };
