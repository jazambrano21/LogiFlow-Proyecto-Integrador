# ── Stage 1: Install deps ────────────────────────────────────────────────────
FROM node:20-alpine AS deps
WORKDIR /app
COPY package.json package-lock.json* ./
RUN npm ci --omit=dev

# ── Stage 2: Runtime ─────────────────────────────────────────────────────────
FROM node:20-alpine AS runtime
WORKDIR /app

RUN addgroup -S logiflow && adduser -S gqlgateway -G logiflow

COPY --from=deps /app/node_modules ./node_modules
COPY src ./src
COPY package.json .

USER gqlgateway

EXPOSE 4000

CMD ["node", "src/index.js"]
