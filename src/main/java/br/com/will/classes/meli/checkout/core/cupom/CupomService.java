package br.com.will.classes.meli.checkout.core.cupom;

import br.com.will.classes.meli.checkout.core.cupom.motivofalha.MotivoFalha;
import br.com.will.classes.meli.checkout.core.cupom.regra.RegraDeCupom;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class CupomService {
    private final List<RegraDeCupom> regras;

    public CupomService(List<RegraDeCupom> regras) {
        this.regras = List.copyOf(regras);
    }

    public ResultadoValidacao validar(Cupom cupom, PedidoCupom pedido, LocalDate hoje) {
        List<MotivoFalha> motivos = new ArrayList<>();
        for (var r : regras) {
            var m = r.aplicar(cupom, pedido, hoje);
            if (m != null) motivos.add(m);
        }
        return motivos.isEmpty() ? ResultadoValidacao.ok() : ResultadoValidacao.falha(motivos);
    }
}