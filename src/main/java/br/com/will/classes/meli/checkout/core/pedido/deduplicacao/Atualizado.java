package br.com.will.classes.meli.checkout.core.pedido.deduplicacao;

import java.math.BigDecimal;

public record Atualizado(String pedidoId, BigDecimal novoValor) implements MensagemPedido {}