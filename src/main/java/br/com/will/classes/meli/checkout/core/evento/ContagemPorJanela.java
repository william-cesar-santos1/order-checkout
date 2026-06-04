package br.com.will.classes.meli.checkout.core.evento;

import java.time.Instant;

public record ContagemPorJanela(Instant inicioJanela, long total) {
}