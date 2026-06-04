package br.com.will.classes.meli.checkout.core.pedido.deduplicacao;

public sealed interface MensagemPedido permits Criado, Cancelado, Atualizado {}