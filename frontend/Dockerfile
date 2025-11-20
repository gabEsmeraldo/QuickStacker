# ----------------------------
# 1. Build da aplicação
# ----------------------------
FROM node:20-alpine AS builder

# Define diretório de trabalho
WORKDIR /app

# Copia package.json e package-lock.json (ou pnpm/yarn)
COPY package*.json ./

# Instala dependências
RUN npm install

# Copia todo o código do frontend
COPY . .

# Build de produção
RUN npm run build


# ----------------------------
# 2. Container final com NGINX
# ----------------------------
FROM nginx:stable-alpine

# Remove configuração padrão
RUN rm -rf /usr/share/nginx/html/*

# Copia a build gerada pelo Vite no estágio anterior
COPY --from=builder /app/dist /usr/share/nginx/html

# Copia config customizada do nginx (opcional)
# COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expor porta
EXPOSE 80

# Rodar nginx
CMD ["nginx", "-g", "daemon off;"]
