package br.com.will.classes.meli.checkout.core.cupom.regra;

import br.com.will.classes.meli.checkout.core.cupom.Cupom;
import br.com.will.classes.meli.checkout.core.cupom.PedidoCupom;
import br.com.will.classes.meli.checkout.core.cupom.motivofalha.Expirado;
import br.com.will.classes.meli.checkout.core.cupom.motivofalha.MotivoFalha;

import java.time.LocalDate;

public final class RegraValidade implements RegraDeCupom {

    public MotivoFalha aplicar(Cupom c, PedidoCupom p, LocalDate hoje) {
        return hoje.isAfter(c.validoAte()) ? new Expirado(c.validoAte()) : null;
    }

}
