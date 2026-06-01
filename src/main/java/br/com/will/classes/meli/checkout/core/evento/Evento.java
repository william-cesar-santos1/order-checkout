package br.com.will.classes.meli.checkout.core.evento;

import java.time.Instant;

public class Evento {
    private final String id;
    private final Instant timestamp;

    public Evento(String id, Instant timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}