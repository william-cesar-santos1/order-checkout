package br.com.will.classes.meli.checkout.core.cupom.regra;

import br.com.will.classes.meli.checkout.core.cupom.Cupom;
import br.com.will.classes.meli.checkout.core.cupom.PedidoCupom;
import br.com.will.classes.meli.checkout.core.cupom.motivofalha.MotivoFalha;
import br.com.will.classes.meli.checkout.core.cupom.motivofalha.ValorMinimo;

import java.time.LocalDate;

public final class RegraValorMinimo implements RegraDeCupom {

    public MotivoFalha aplicar(Cupom c, PedidoCupom p, LocalDate hoje) {
        return p.valor().compareTo(c.valorMinimo()) < 0 ? new ValorMinimo(c.valorMinimo()) : null;
    }

}
