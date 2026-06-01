package br.com.will.classes.meli.checkout.core.cupom.regra;

import br.com.will.classes.meli.checkout.core.cupom.Cupom;
import br.com.will.classes.meli.checkout.core.cupom.PedidoCupom;
import br.com.will.classes.meli.checkout.core.cupom.motivofalha.MotivoFalha;

import java.time.LocalDate;

@FunctionalInterface
public interface RegraDeCupom {
    MotivoFalha aplicar(Cupom cupom, PedidoCupom pedido, LocalDate hoje);
}
