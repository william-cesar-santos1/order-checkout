package br.com.will.classes.meli.checkout.core.cupom.motivofalha;

import java.util.Set;

public record CategoriaInvalida(Set<String> elegiveis) implements MotivoFalha {

    public CategoriaInvalida {
        elegiveis = Set.copyOf(elegiveis);
    }

}