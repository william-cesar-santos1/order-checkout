package br.com.will.classes.meli.checkout.core.cupom;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Cupom {
    private final String codigo;
    private final LocalDate validoAte;
    private final BigDecimal valorMinimo;
    private final Set<String> categoriasElegiveis;

    public Cupom(String codigo, LocalDate validoAte, BigDecimal valorMinimo, Set<String> categoriasElegiveis) {
        this.codigo = codigo;
        this.validoAte = validoAte;
        this.valorMinimo = valorMinimo;
        this.categoriasElegiveis = Collections.unmodifiableSet(new HashSet<>(categoriasElegiveis));
    }

    public String getCodigo() {
        return codigo;
    }

    public LocalDate getValidoAte() {
        return validoAte;
    }

    public BigDecimal getValorMinimo() {
        return valorMinimo;
    }

    public Set<String> getCategoriasElegiveis() {
        return categoriasElegiveis;
    }
}