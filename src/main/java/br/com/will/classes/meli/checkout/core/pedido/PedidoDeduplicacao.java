package br.com.will.classes.meli.checkout.core.pedido;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class PedidoDeduplicacao {
    private final String id;
    private final String clienteId;
    private final BigDecimal valor;
    private final Instant timestamp;

    public PedidoDeduplicacao(String id, String clienteId, BigDecimal valor, Instant timestamp) {
        this.id = id;
        this.clienteId = clienteId;
        this.valor = valor;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getClienteId() {
        return clienteId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PedidoDeduplicacao)) return false;
        PedidoDeduplicacao p = (PedidoDeduplicacao) o;
        return Objects.equals(id, p.id)
                && Objects.equals(clienteId, p.clienteId)
                && Objects.equals(valor, p.valor)
                && Objects.equals(timestamp, p.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clienteId, valor, timestamp);
    }

    @Override
    public String toString() {
        return "PedidoCupom{" + id + "," + clienteId + "," + valor + "," + timestamp + "}";
    }
}