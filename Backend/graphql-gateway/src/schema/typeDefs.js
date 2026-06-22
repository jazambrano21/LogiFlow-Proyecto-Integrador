'use strict';

const { gql } = require('../utils/gql');

/**
 * Schema GraphQL completo del Gateway BFF de LogiFlow.
 * Agrega datos de: ms-clientes, ms-pedidos, ms-flota, ms-ruteo, ms-seguimiento
 */
const typeDefs = gql`
  # ── Tipos base ─────────────────────────────────────────────────────────────

  type Cliente {
    id:        ID!
    nombre:    String!
    email:     String!
    telefono:  String!
    tipo:      String!          # PERSONAL | CORPORATIVO
  }

  type Ubicacion {
    ciudad:    String!
    direccion: String!
  }

  type Paquete {
    pesoKg:      Float!
    descripcion: String!
  }

  type Pedido {
    id:              ID!
    clienteId:       ID!
    cliente:         Cliente     # resuelto llamando ms-clientes
    origen:          Ubicacion!
    destino:         Ubicacion!
    paquete:         Paquete!
    estado:          String!     # CREADO | ASIGNADO | EN_RUTA | ENTREGADO | CANCELADO
    prioridad:       String!     # NORMAL | URGENTE
    nivelGeografico: String!     # LOCAL | PROVINCIAL | NACIONAL
    fechaCreacion:   String!
  }

  type Ruta {
    origen:       Ubicacion!
    destino:      Ubicacion!
    kmsEstimados: Float!
  }

  type Posicion {
    envioId:   ID!
    lat:       Float!
    lng:       Float!
    velocidad: Float
    eta:       String
    timestamp: String!
  }

  type Envio {
    id:             ID!
    pedidoId:       ID!
    pedido:         Pedido       # resuelto llamando ms-pedidos
    vehiculoId:     ID!
    conductorId:    ID
    ruta:           Ruta!
    horarioEstimado: String
    estado:         String!      # ASIGNADO | EN_RUTA | ENTREGADO
    ultimaPosicion: Posicion     # última posición conocida (cache interno)
  }

  type Vehiculo {
    id:          ID!
    matricula:   String!
    tipo:        String!         # MOTO | AUTO | FURGONETA | CAMION
    capacidadKg: Float!
    estado:      String!         # DISPONIBLE | EN_SERVICIO | MANTENIMIENTO | INACTIVO
  }

  # ── Inputs ─────────────────────────────────────────────────────────────────

  input UbicacionInput {
    ciudad:    String!
    direccion: String!
  }

  input PaqueteInput {
    pesoKg:      Float!
    descripcion: String!
  }

  input PedidoInput {
    clienteId:       ID!
    origen:          UbicacionInput!
    destino:         UbicacionInput!
    paquete:         PaqueteInput!
    prioridad:       String!
    nivelGeografico: String!
  }

  # ── Queries ────────────────────────────────────────────────────────────────

  type Query {
    """Lista pedidos activos. Filtra por clienteId si se provee."""
    pedidosActivos(clienteId: ID): [Pedido!]!

    """Obtiene un pedido por ID con datos del cliente adjuntos."""
    pedido(id: ID!): Pedido

    """Obtiene un envío por ID con pedido y última posición."""
    envio(id: ID!): Envio

    """Lista todos los clientes."""
    clientes: [Cliente!]!

    """Lista vehículos disponibles, opcionalmente filtrados por nivelGeografico."""
    vehiculosDisponibles(nivelGeografico: String): [Vehiculo!]!
  }

  # ── Mutations ──────────────────────────────────────────────────────────────

  type Mutation {
    """Crea un nuevo pedido y publica el evento pedido.creado a RabbitMQ."""
    crearPedido(input: PedidoInput!): Pedido!

    """Cancela un pedido (DELETE ms-pedidos/pedidos/:id)."""
    cancelarPedido(id: ID!): Pedido!

    """
    Actualiza el estado de un envío.
    Transiciones: ASIGNADO→EN_RUTA→ENTREGADO
    """
    actualizarEstadoEnvio(id: ID!, estado: String!): Envio!
  }

  # ── Subscriptions ──────────────────────────────────────────────────────────

  type Subscription {
    """
    Recibe actualizaciones de posición GPS en tiempo real para un envío.
    El gateway se conecta como cliente al WebSocket de ms-seguimiento
    y reemite las actualizaciones.
    """
    posicionEnvio(envioId: ID!): Posicion!
  }
`;

module.exports = { typeDefs };
