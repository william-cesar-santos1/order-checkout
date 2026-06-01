package br.com.will.classes.meli.checkout.core.pedido;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DeduplicacaoTest {

    private final Deduplicacao dedup = new Deduplicacao();

    @Test
    void mantemPrimeiroPedidoEDescartaDuplicadoDentroDeJanela() {
        Instant t0 = Instant.parse("2026-05-24T10:00:00Z");
        var p1 = new PedidoDeduplicacao("p1", "c1", new BigDecimal("100.00"), t0);
        var p2 = new PedidoDeduplicacao("p2", "c1", new BigDecimal("100.00"), t0.plusSeconds(60));

        var resultado = dedup.deduplicar(List.of(p1, p2));

        assertThat(resultado).extracting(PedidoDeduplicacao::getId).containsExactly("p1");
    }

    @Test
    void mantemPedidoDoMesmoClienteQuandoForaDaJanela() {
        Instant t0 = Instant.parse("2026-05-24T10:00:00Z");
        var p1 = new PedidoDeduplicacao("p1", "c1", new BigDecimal("100.00"), t0);
        var p2 = new PedidoDeduplicacao("p2", "c1", new BigDecimal("100.00"), t0.plusSeconds(600));

        var resultado = dedup.deduplicar(List.of(p1, p2));

        assertThat(resultado).extracting(PedidoDeduplicacao::getId).containsExactly("p1", "p2");
    }

    @Test
    void clientesDiferentesNuncaSaoDuplicados() {
        Instant t0 = Instant.parse("2026-05-24T10:00:00Z");
        var p1 = new PedidoDeduplicacao("p1", "c1", new BigDecimal("100.00"), t0);
        var p2 = new PedidoDeduplicacao("p2", "c2", new BigDecimal("100.00"), t0.plusSeconds(60));

        var resultado = dedup.deduplicar(List.of(p1, p2));

        assertThat(resultado).hasSize(2);
    }

    @Test
    void valoresDiferentesNaoSaoDuplicados() {
        Instant t0 = Instant.parse("2026-05-24T10:00:00Z");
        var p1 = new PedidoDeduplicacao("p1", "c1", new BigDecimal("100.00"), t0);
        var p2 = new PedidoDeduplicacao("p2", "c1", new BigDecimal("250.00"), t0.plusSeconds(30));

        var resultado = dedup.deduplicar(List.of(p1, p2));

        assertThat(resultado).hasSize(2);
    }

    @Test
    void descreveStringComoTexto() {
        assertThat(dedup.descreverMensagem("MELI10")).isEqualTo("texto:MELI10");
    }

    @Test
    void descreveNumeroComoNumero() {
        assertThat(dedup.descreverMensagem(42)).startsWith("numero:");
    }

    @Test
    void descreveNullComoVazio() {
        assertThat(dedup.descreverMensagem(null)).isEqualTo("vazio");
    }
}