'use strict';

const pedidosClient = require('../clients/pedidosClient');
const ruteoClient   = require('../clients/ruteoClient');
const { mapPedido, mapEnvio } = require('./queryResolvers');

const mutationResolvers = {
  Mutation: {
    /**
     * crearPedido(input: PedidoInput!): Pedido
     * POST /pedidos en ms-pedidos.
     * ms-pedidos publica automáticamente pedido.creado a RabbitMQ → ms-ruteo asigna.
     */
    async crearPedido(_, { input }, { token }) {
      const pedido = await pedidosClient.crearPedido(input, token);
      return mapPedido(pedido);
    },

    /**
     * cancelarPedido(id: ID!): Pedido
     * DELETE /pedidos/:id en ms-pedidos.
     */
    async cancelarPedido(_, { id }, { token }) {
      const pedido = await pedidosClient.cancelarPedido(id, token);
      return mapPedido(pedido);
    },

    /**
     * actualizarEstadoEnvio(id: ID!, estado: String!): Envio
     * PUT /envios/:id/estado en ms-ruteo.
     * Si estado=ENTREGADO, ms-ruteo publica pedido.entregado a RabbitMQ.
     */
    async actualizarEstadoEnvio(_, { id, estado }, { token }) {
      const envio = await ruteoClient.actualizarEstadoEnvio(id, estado, token);
      return mapEnvio(envio);
    },
  },
};

module.exports = { mutationResolvers };
