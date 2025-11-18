# PedidoService – Monitoramento Completo com Spring Boot, Prometheus, Grafana, Micrometer e Docker

## Descrição do Projeto

Este projeto demonstra como implementar **monitoramento completo** em uma aplicação Spring Boot. A aplicação `PedidoService` simula um serviço de pedidos de e-commerce, coletando métricas técnicas e de negócio, gerando alertas proativos e permitindo visualização em dashboards.

O projeto integra:

* Spring Boot
* Micrometer com Prometheus
* Grafana para dashboards
* Logging estruturado
* Orquestração via Docker Compose

## Cenário Simulado

O serviço `PedidoService` oferece endpoints REST para criar e listar pedidos. O monitoramento abrange:

* Estabilidade e desempenho da aplicação
* Visibilidade de falhas e gargalos
* Métricas de negócio em tempo real
* Capacidade de escalar baseada em dados reais

## Métricas Monitoradas

### Métricas Técnicas

* Uso de CPU e memória da JVM
* Tempo de resposta por endpoint
* Taxa de requisições por segundo
* Erros HTTP (4xx e 5xx)
* Threads e garbage collection

### Métricas de Negócio

* Quantidade de pedidos criados por minuto
* Valor médio dos pedidos
* Taxa de sucesso/falha no processamento

## Logging

* Logs com contexto (ID do pedido, usuário, status)
* Nível de log ajustável por ambiente
* Integração opcional com Grafana Loki ou ELK

## Alertas

Exemplos de alertas configuráveis no Prometheus:

* Latência acima de 1s no endpoint `/pedidos`
* Mais de 5% de erros 5xx em 5 minutos
* Queda brusca na criação de pedidos

## Configuração do Projeto

### Dependências do Maven (`pom.xml`)

* spring-boot-starter-web
* spring-boot-starter-actuator
* micrometer-registry-prometheus
* lombok (opcional)

### `application.yml`

```yaml
server:
  port: 8080

management:
  endpoints:
    web:
      exposure:
        include: health, prometheus, metrics
  metrics:
    tags:
      application: pedido-service

logging:
  level:
    root: INFO
    org.springframework.web: DEBUG
```

## Métricas Personalizadas com Micrometer

```java
@Autowired
private MeterRegistry registry;

public void registrarValorPedido(BigDecimal valor) {
    registry.summary("pedidos.valor").record(valor.doubleValue());
    registry.counter("pedidos.criados").increment();
}
```

## Docker Compose

`docker-compose.yml`:

```yaml
version: '3'
services:
  prometheus:
    image: prom/prometheus
    ports:
      - "9090:9090"
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml

  grafana:
    image: grafana/grafana
    ports:
      - "3000:3000"
    depends_on:
      - prometheus
```

## Configuração do Prometheus

`prometheus.yml`:

```yaml
global:
  scrape_interval: 5s

scrape_configs:
  - job_name: 'pedido-service'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['host.docker.internal:8080']
```

## Dashboards no Grafana

1. Acesse: `http://localhost:3000`
2. Login padrão: `admin / admin`
3. Adicione Prometheus como fonte de dados: `http://prometheus:9090`
4. Importe dashboards prontos com IDs: `4701` ou `6756`

## Exemplo de Alerta Prometheus

```yaml
- alert: HighErrorRate
  expr: rate(http_server_errors_total[1m]) > 5
  for: 1m
  labels:
    severity: critical
  annotations:
    summary: "Taxa de erro HTTP alta"
    description: "Mais de 5 erros por minuto detectados"
```

## Como Rodar a Aplicação

1. Clone o repositório:

```bash
git clone <seu-repo>
cd pedido-service
```

2. Gere o projeto com Maven:

```bash
mvn clean install
```

3. Rode a aplicação:

```bash
mvn spring-boot:run
```

4. Inicie os containers do Docker:

```bash
docker-compose up -d
```

5. Acesse:

* API REST: `http://localhost:8080/pedidos`
* Grafana: `http://localhost:3000`
* Prometheus: `http://localhost:9090`

## Exemplos de Requisições

```bash
# Criar pedido
curl -X POST "http://localhost:8080/pedidos?usuario=tulio&valor=100.50"

# Listar pedidos
curl "http://localhost:8080/pedidos"
```