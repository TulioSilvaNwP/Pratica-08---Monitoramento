package com.example.pedido.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    private String id;
    private String usuario;
    private BigDecimal valor;
    private LocalDateTime dataCriacao;
    private boolean sucesso;
}
