package br.com.will.classes.meli.checkout.core.pedido.deduplicacao;

public record Cancelado(String pedidoId, String motivo) implements MensagemPedido {}