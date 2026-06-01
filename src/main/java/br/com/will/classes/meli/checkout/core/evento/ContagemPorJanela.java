package br.com.will.classes.meli.checkout.core.evento;

import java.time.Instant;
import java.util.Objects;

public class ContagemPorJanela {
    private final Instant inicioJanela;
    private final long total;

    public ContagemPorJanela(Instant inicioJanela, long total) {
        this.inicioJanela = inicioJanela;
        this.total = total;
    }

    public Instant getInicioJanela() {
        return inicioJanela;
    }

    public long getTotal() {
        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContagemPorJanela)) return false;
        ContagemPorJanela c = (ContagemPorJanela) o;
        return total == c.total && Objects.equals(inicioJanela, c.inicioJanela);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inicioJanela, total);
    }

    @Override
    public String toString() {
        return inicioJanela + " -> " + total;
    }
}