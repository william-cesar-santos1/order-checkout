package br.com.will.classes.meli.checkout.core.pedido.processar;

public record PedidoEnriquecido(String id, boolean aprovadoNaFraude, int estoque, String previsaoFrete) {}