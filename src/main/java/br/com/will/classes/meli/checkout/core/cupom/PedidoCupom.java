package br.com.will.classes.meli.checkout.core.cupom;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PedidoCupom {
    private final BigDecimal valor;
    private final Set<String> categorias;

    public PedidoCupom(BigDecimal valor, Set<String> categorias) {
        this.valor = valor;
        this.categorias = Collections.unmodifiableSet(new HashSet<>(categorias));
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Set<String> getCategorias() {
        return categorias;
    }
}