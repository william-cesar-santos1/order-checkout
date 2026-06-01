package br.com.will.classes.meli.checkout.core.cupom;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ValidadorCupomTest {

    private final ValidadorCupom v = new ValidadorCupom();
    private final Cupom meli10 = new Cupom(
            "MELI10",
            LocalDate.of(2026, 12, 31),
            new BigDecimal("100.00"),
            Set.of("eletronicos", "casa")
    );

    @Test
    void cupomValidoQuandoPedidoAtendeTodasAsRegras() {
        var pedido = new PedidoCupom(new BigDecimal("250.00"), Set.of("eletronicos"));
        var r = v.validarCupom(meli10, pedido, LocalDate.of(2026, 5, 24));
        assertThat(r.isValido()).isTrue();
        assertThat(r.getMotivos()).isEmpty();
    }

    @Test
    void cupomFalhaQuandoExpirado() {
        var pedido = new PedidoCupom(new BigDecimal("250.00"), Set.of("eletronicos"));
        var r = v.validarCupom(meli10, pedido, LocalDate.of(2027, 1, 1));
        assertThat(r.isValido()).isFalse();
        assertThat(r.getMotivos()).anySatisfy(m -> assertThat(m).contains("expirado"));
    }

    @Test
    void cupomFalhaQuandoAbaixoValorMinimo() {
        var pedido = new PedidoCupom(new BigDecimal("50.00"), Set.of("eletronicos"));
        var r = v.validarCupom(meli10, pedido, LocalDate.of(2026, 5, 24));
        assertThat(r.isValido()).isFalse();
        assertThat(r.getMotivos()).anySatisfy(m -> assertThat(m).contains("valor mínimo"));
    }

    @Test
    void cupomFalhaQuandoCategoriaNaoElegivel() {
        var pedido = new PedidoCupom(new BigDecimal("250.00"), Set.of("moda"));
        var r = v.validarCupom(meli10, pedido, LocalDate.of(2026, 5, 24));
        assertThat(r.isValido()).isFalse();
        assertThat(r.getMotivos()).anySatisfy(m -> assertThat(m).contains("Categorias elegíveis"));
    }

    @Test
    void acumulaMotivosQuandoMaisDeUmaRegraFalha() {
        var pedido = new PedidoCupom(new BigDecimal("50.00"), Set.of("moda"));
        var r = v.validarCupom(meli10, pedido, LocalDate.of(2027, 1, 1));
        assertThat(r.getMotivos()).hasSize(3);
    }
}