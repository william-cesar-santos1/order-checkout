package br.com.will.classes.meli.checkout.core.cupom.regra;

import br.com.will.classes.meli.checkout.core.cupom.Cupom;
import br.com.will.classes.meli.checkout.core.cupom.PedidoCupom;
import br.com.will.classes.meli.checkout.core.cupom.motivofalha.CategoriaInvalida;
import br.com.will.classes.meli.checkout.core.cupom.motivofalha.MotivoFalha;

import java.time.LocalDate;

public final class RegraCategoria implements RegraDeCupom {

    public MotivoFalha aplicar(Cupom c, PedidoCupom p, LocalDate hoje) {
        boolean valido = p.categorias().stream().anyMatch(c.categoriasElegiveis()::contains);
        return valido ? null : new CategoriaInvalida(c.categoriasElegiveis());
    }

}
