package br.com.will.classes.meli.checkout.core.cupom;

import br.com.will.classes.meli.checkout.core.cupom.motivofalha.CategoriaInvalida;
import br.com.will.classes.meli.checkout.core.cupom.motivofalha.Expirado;
import br.com.will.classes.meli.checkout.core.cupom.motivofalha.ValorMinimo;
import br.com.will.classes.meli.checkout.core.cupom.regra.RegraCategoria;
import br.com.will.classes.meli.checkout.core.cupom.regra.RegraValidade;
import br.com.will.classes.meli.checkout.core.cupom.regra.RegraValorMinimo;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ValidadorCupomTest {

    private final CupomService v = new CupomService(List.of(new RegraValidade(), new RegraValorMinimo(), new RegraCategoria()));

    private final Cupom meli10 = new Cupom(
            "MELI10",
            LocalDate.of(2026, 12, 31),
            new BigDecimal("100.00"),
            Set.of("eletronicos", "casa")
    );

    @Test
    void cupomValidoQuandoPedidoAtendeTodasAsRegras() {
        var pedido = new PedidoCupom(new BigDecimal("250.00"), Set.of("eletronicos"));
        var r = v.validar(meli10, pedido, LocalDate.of(2026, 5, 24));
        assertThat(r.valido()).isTrue();
        assertThat(r.motivos()).isEmpty();
    }

    @Test
    void cupomFalhaQuandoExpirado() {
        var pedido = new PedidoCupom(new BigDecimal("250.00"), Set.of("eletronicos"));
        var r = v.validar(meli10, pedido, LocalDate.of(2027, 1, 1));
        assertThat(r.valido()).isFalse();
        assertThat(r.motivos()).anySatisfy(m -> assertThat(m).isInstanceOf(Expirado.class));
    }

    @Test
    void cupomFalhaQuandoAbaixoValorMinimo() {
        var pedido = new PedidoCupom(new BigDecimal("50.00"), Set.of("eletronicos"));
        var r = v.validar(meli10, pedido, LocalDate.of(2026, 5, 24));
        assertThat(r.valido()).isFalse();
        assertThat(r.motivos()).anySatisfy(m -> assertThat(m).isInstanceOf(ValorMinimo.class));
    }

    @Test
    void cupomFalhaQuandoCategoriaNaoElegivel() {
        var pedido = new PedidoCupom(new BigDecimal("250.00"), Set.of("moda"));
        var r = v.validar(meli10, pedido, LocalDate.of(2026, 5, 24));
        assertThat(r.valido()).isFalse();
        assertThat(r.motivos()).anySatisfy(m -> assertThat(m).isInstanceOf(CategoriaInvalida.class));
    }

    @Test
    void acumulaMotivosQuandoMaisDeUmaRegraFalha() {
        var pedido = new PedidoCupom(new BigDecimal("50.00"), Set.of("moda"));
        var r = v.validar(meli10, pedido, LocalDate.of(2027, 1, 1));
        assertThat(r.motivos()).hasSize(3);
    }
}