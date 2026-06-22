'use strict';

const axios = require('axios');

/**
 * Crea una instancia de axios preconfigurada para un microservicio.
 * Inyecta automáticamente el token JWT del contexto GraphQL.
 *
 * @param {string} baseURL - URL base del microservicio
 * @param {number} timeout - timeout en ms (por defecto 8000)
 */
function createClient(baseURL, timeout = 8000) {
  const instance = axios.create({ baseURL, timeout });

  // Interceptor para agregar el token en cada request
  instance.interceptors.request.use((config) => {
    if (config._token) {
      config.headers['Authorization'] = `Bearer ${config._token}`;
      delete config._token;
    }
    return config;
  });

  // Interceptor para normalizar errores
  instance.interceptors.response.use(
    (res) => res.data,
    (err) => {
      const status  = err.response?.status;
      const message = err.response?.data?.message || err.message;
      const error   = new Error(`[${status ?? 'NET'}] ${message}`);
      error.statusCode = status;
      throw error;
    }
  );

  return instance;
}

/**
 * Helper: extrae el campo data de la respuesta estandarizada { success, message, data }
 * que usan todos los microservicios Java de LogiFlow.
 */
function unwrap(response) {
  if (response && typeof response === 'object' && 'data' in response) {
    return response.data;
  }
  return response;
}

module.exports = { createClient, unwrap };
