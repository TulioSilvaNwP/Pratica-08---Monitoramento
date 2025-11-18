package com.example.pedido.controller;

import com.example.pedido.model.Pedido;
import com.example.pedido.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collection;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<Pedido> criarPedido(@RequestParam String usuario, @RequestParam BigDecimal valor) {
        Pedido pedido = pedidoService.criarPedido(usuario, valor);
        return ResponseEntity.ok(pedido);
    }

    @GetMapping
    public ResponseEntity<Collection<Pedido>> listarPedidos() {
        return ResponseEntity.ok(pedidoService.listarPedidos());
    }
}