'use strict';

const { GraphQLError } = require('graphql');
const seguimientoManager = require('../seguimiento/seguimientoManager');

const subscriptionResolvers = {
  Subscription: {
    /**
     * posicionEnvio(envioId: ID!): Posicion
     *
     * subscribe: registra al cliente en ms-seguimiento vía Socket.IO y devuelve
     *            el AsyncIterator del PubSub interno.
     * resolve:   retorna el payload tal cual llega del PubSub.
     */
    posicionEnvio: {
      subscribe(_, { envioId }, { user }) {
        // user fue validado en el contexto WebSocket
        if (!user) {
          throw new GraphQLError('No autenticado', {
            extensions: { code: 'UNAUTHENTICATED' },
          });
        }

        const id = String(envioId);
        seguimientoManager.suscribir(id);

        const iterator = seguimientoManager.asyncIterator(id);

        // Limpieza al desconectarse el cliente GraphQL
        const originalReturn = iterator.return?.bind(iterator);
        iterator.return = async (value) => {
          seguimientoManager.desuscribir(id);
          if (originalReturn) return originalReturn(value);
          return { value, done: true };
        };

        return iterator;
      },

      resolve(payload) {
        // payload tiene forma { posicionEnvio: { envioId, lat, lng, ... } }
        return payload.posicionEnvio;
      },
    },
  },
};

module.exports = { subscriptionResolvers };
