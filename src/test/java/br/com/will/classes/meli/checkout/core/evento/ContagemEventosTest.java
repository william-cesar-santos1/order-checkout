package br.com.will.classes.meli.checkout.core.evento;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ContagemEventosTest {

    private final ContagemEventos c = new ContagemEventos();

    @Test
    void agrupaEventosPorJanelaDeUmMinuto() {
        Instant t0 = Instant.parse("2026-05-24T10:00:00Z");
        var eventos = List.of(
                new Evento("e1", t0),
                new Evento("e2", t0.plusSeconds(10)),
                new Evento("e3", t0.plusSeconds(45)),
                new Evento("e4", t0.plusSeconds(60)),
                new Evento("e5", t0.plusSeconds(110))
        );

        var janelas = c.contar(eventos);

        assertThat(janelas).hasSize(2);
        assertThat(janelas.get(0).getTotal()).isEqualTo(3);
        assertThat(janelas.get(1).getTotal()).isEqualTo(2);
    }

    @Test
    void listaMaisRecentesPrimeiroDevolveOrdemReversa() {
        Instant t0 = Instant.parse("2026-05-24T10:00:00Z");
        var janelas = List.of(
                new ContagemPorJanela(t0, 3L),
                new ContagemPorJanela(t0.plusSeconds(60), 2L),
                new ContagemPorJanela(t0.plusSeconds(120), 1L)
        );
        assertThat(c.listarMaisRecentesPrimeiro(janelas))
                .extracting(ContagemPorJanela::getTotal)
                .containsExactly(1L, 2L, 3L);
    }

    @Test
    void ultimasNDevolveUltimasNJanelas() {
        Instant t0 = Instant.parse("2026-05-24T10:00:00Z");
        var janelas = List.of(
                new ContagemPorJanela(t0, 3L),
                new ContagemPorJanela(t0.plusSeconds(60), 2L),
                new ContagemPorJanela(t0.plusSeconds(120), 1L)
        );
        assertThat(c.ultimasN(janelas, 2))
                .extracting(ContagemPorJanela::getTotal)
                .containsExactly(1L, 2L);
    }
}