package br.com.will.classes.meli.checkout.core.evento;

import java.time.Instant;

public record Evento(String id, Instant timestamp) {}