'use strict';

const clientesClient = require('../clients/clientesClient');
const pedidosClient  = require('../clients/pedidosClient');
const flotaClient    = require('../clients/flotaClient');
const ruteoClient    = require('../clients/ruteoClient');
const posicionCache  = require('../seguimiento/posicionCache');

/**
 * Mappers: adaptan las respuestas de los microservicios al schema GraphQL.
 * ms-flota usa "capacidad" pero el schema expone "capacidadKg".
 */
function mapVehiculo(v) {
  return {
    id:          String(v.id),
    matricula:   v.matricula,
    tipo:        v.tipo,
    capacidadKg: v.capacidad ?? v.capacidadKg ?? 0,
    estado:      v.estado,
  };
}

function mapUbicacion(u) {
  if (!u) return { ciudad: '', direccion: '' };
  return { ciudad: u.ciudad ?? '', direccion: u.direccion ?? '' };
}

function mapPedido(p) {
  if (!p) return null;
  return {
    id:              String(p.id),
    clienteId:       String(p.clienteId),
    origen:          mapUbicacion(p.origen),
    destino:         mapUbicacion(p.destino),
    paquete:         { pesoKg: p.paquete?.pesoKg ?? 0, descripcion: p.paquete?.descripcion ?? '' },
    estado:          p.estado,
    prioridad:       p.prioridad,
    nivelGeografico: p.nivelGeografico,
    fechaCreacion:   p.fechaCreacion ?? '',
    // cliente se resuelve en el field resolver de Pedido.cliente
    _clienteId:      String(p.clienteId),
  };
}

function mapEnvio(e) {
  if (!e) return null;
  return {
    id:              String(e.id),
    pedidoId:        String(e.pedidoId),
    vehiculoId:      String(e.vehiculoId),
    conductorId:     e.conductorId ? String(e.conductorId) : null,
    ruta: {
      origen:       mapUbicacion(e.ruta?.origen),
      destino:      mapUbicacion(e.ruta?.destino),
      kmsEstimados: e.ruta?.kmsEstimados ?? 0,
    },
    horarioEstimado: e.horarioEstimado ?? null,
    estado:          e.estado,
    ultimaPosicion:  posicionCache.obtener(e.id),
    _pedidoId:       String(e.pedidoId),
  };
}

const queryResolvers = {
  Query: {
    /**
     * pedidosActivos(clienteId: ID): [Pedido]
     * Llama GET /pedidos?clienteId=X y trae los pedidos que no estén CANCELADOS.
     */
    async pedidosActivos(_, { clienteId }, { token }) {
      const params = {};
      if (clienteId) params.clienteId = clienteId;

      const pedidos = await pedidosClient.listarPedidos(params, token);
      const activos = pedidos.filter(p => p.estado !== 'CANCELADO');
      return activos.map(mapPedido);
    },

    /**
     * pedido(id: ID!): Pedido
     */
    async pedido(_, { id }, { token }) {
      const p = await pedidosClient.obtenerPedido(id, token);
      return mapPedido(p);
    },

    /**
     * envio(id: ID!): Envio
     * Retorna el envío con ruta y última posición cacheada.
     */
    async envio(_, { id }, { token }) {
      const e = await ruteoClient.obtenerEnvio(id, token);
      return mapEnvio(e);
    },

    /**
     * clientes: [Cliente]
     */
    async clientes(_, __, { token }) {
      const lista = await clientesClient.listarClientes(token);
      return lista.map(c => ({
        id:       String(c.id),
        nombre:   c.nombre,
        email:    c.email,
        telefono: c.telefono,
        tipo:     c.tipo,
      }));
    },

    /**
     * vehiculosDisponibles(nivelGeografico: String): [Vehiculo]
     * nivelGeografico se usa solo para logging/contexto en esta query.
     * La capacidad mínima se pasa como undefined (sin filtro de peso aquí).
     */
    async vehiculosDisponibles(_, { nivelGeografico }) {
      const vehiculos = await flotaClient.vehiculosDisponibles(undefined);
      return vehiculos.map(mapVehiculo);
    },
  },

  // ── Field resolvers ──────────────────────────────────────────────────────

  /**
   * Pedido.cliente: resuelve el cliente llamando ms-clientes por clienteId.
   * Se ejecuta solo si el cliente pide el campo `cliente` en su query.
   */
  Pedido: {
    async cliente(pedido, _, { token }) {
      if (!pedido._clienteId) return null;
      const c = await clientesClient.obtenerCliente(pedido._clienteId, token);
      if (!c) return null;
      return {
        id:       String(c.id),
        nombre:   c.nombre,
        email:    c.email,
        telefono: c.telefono,
        tipo:     c.tipo,
      };
    },
  },

  /**
   * Envio.pedido: resuelve el pedido llamando ms-pedidos por pedidoId.
   */
  Envio: {
    async pedido(envio, _, { token }) {
      if (!envio._pedidoId) return null;
      const p = await pedidosClient.obtenerPedido(envio._pedidoId, token);
      return mapPedido(p);
    },
  },
};

module.exports = { queryResolvers, mapPedido, mapEnvio };
