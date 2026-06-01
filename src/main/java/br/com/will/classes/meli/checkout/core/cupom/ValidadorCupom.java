package br.com.will.classes.meli.checkout.core.cupom;

import br.com.will.classes.meli.checkout.core.cupom.motivofalha.CategoriaInvalida;
import br.com.will.classes.meli.checkout.core.cupom.motivofalha.Expirado;
import br.com.will.classes.meli.checkout.core.cupom.motivofalha.MotivoFalha;
import br.com.will.classes.meli.checkout.core.cupom.motivofalha.ValorMinimo;
import br.com.will.classes.meli.checkout.core.cupom.regra.RegraCategoria;
import br.com.will.classes.meli.checkout.core.cupom.regra.RegraValidade;
import br.com.will.classes.meli.checkout.core.cupom.regra.RegraValorMinimo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class ValidadorCupom {

    public static String descreverMotivo(MotivoFalha m) {
        return switch (m) {
            case Expirado(LocalDate em) -> "Cupom expirado em " + em;
            case ValorMinimo(BigDecimal v) -> "Pedido abaixo do valor mínimo R$ " + v.toPlainString();
            case CategoriaInvalida(Set<String> el) -> "Categorias elegíveis: " + String.join(",", el);
        };
    }

    public static void main(String[] args) {
        var service = new CupomService(List.of(new RegraValidade(), new RegraValorMinimo(), new RegraCategoria()));
        var meli10 = new Cupom("MELI10", LocalDate.of(2026, 12, 31), new BigDecimal("100.00"),
                Set.of("eletronicos", "casa"));

        var pedido = new PedidoCupom(new BigDecimal("50.00"), Set.of("moda"));
        var r = service.validar(meli10, pedido, LocalDate.of(2027, 1, 1));
        r.motivos().forEach(m -> System.out.println(descreverMotivo(m)));
    }

}