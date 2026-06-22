'use strict';

const { queryResolvers }        = require('./queryResolvers');
const { mutationResolvers }     = require('./mutationResolvers');
const { subscriptionResolvers } = require('./subscriptionResolvers');

/**
 * Combina todos los resolvers en un único objeto para Apollo Server.
 */
const resolvers = {
  Query:        queryResolvers.Query,
  Mutation:     mutationResolvers.Mutation,
  Subscription: subscriptionResolvers.Subscription,
  Pedido:       queryResolvers.Pedido,
  Envio:        queryResolvers.Envio,
};

module.exports = { resolvers };
