package br.com.will.classes.meli.checkout.core.cupom;

import java.math.BigDecimal;
import java.util.Set;

public record PedidoCupom(
        BigDecimal valor,
        Set<String> categorias
) {
}