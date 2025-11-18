package com.example.pedido.service;

import com.example.pedido.model.Pedido;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PedidoService {

    private final MeterRegistry registry;
    private final Map<String, Pedido> pedidos = new ConcurrentHashMap<>();

    public PedidoService(MeterRegistry registry) {
        this.registry = registry;
    }

    public Pedido criarPedido(String usuario, BigDecimal valor) {
        String id = UUID.randomUUID().toString();
        Pedido pedido = new Pedido(id, usuario, valor, LocalDateTime.now(), true);

        pedidos.put(id, pedido);

        // Métricas personalizadas
        registry.summary("pedidos.valor").record(valor.doubleValue());
        registry.counter("pedidos.criados").increment();

        return pedido;
    }

    public Collection<Pedido> listarPedidos() {
        return pedidos.values();
    }
}