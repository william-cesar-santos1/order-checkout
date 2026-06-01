package br.com.will.classes.meli.checkout.core.cupom.motivofalha;

import java.time.LocalDate;

public record Expirado(LocalDate em) implements MotivoFalha {}