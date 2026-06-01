package br.com.will.classes.meli.checkout.core.cupom.motivofalha;

public sealed interface MotivoFalha permits Expirado, ValorMinimo, CategoriaInvalida {
}
