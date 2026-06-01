package br.com.will.classes.meli.checkout.core.cupom.motivofalha;

import java.math.BigDecimal;

public record ValorMinimo(BigDecimal exigido) implements MotivoFalha {
}