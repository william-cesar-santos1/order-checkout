package br.com.will.classes.meli.checkout.core.pedido.deduplicacao;

import java.math.BigDecimal;
import java.time.Instant;

public record PedidoDeduplicacao(
        String id,
        String clienteId,
        BigDecimal valor,
        Instant timestamp
) {
}
