package br.com.will.classes.meli.checkout.core.evento;

import java.time.Instant;
import java.util.*;

public class ContagemEventos {

    public List<ContagemPorJanela> contar(List<Evento> eventos) {
        LinkedHashMap<Instant, Long> agrupado = new LinkedHashMap<>();
        eventos.stream()
                .sorted((a, b) -> a.timestamp().compareTo(b.timestamp()))
                .forEach(e -> {
                    long minutoEpoch = e.timestamp().getEpochSecond() / 60;
                    Instant inicio = Instant.ofEpochSecond(minutoEpoch * 60);
                    agrupado.merge(inicio, 1L, Long::sum);
                });
        return agrupado.entrySet().stream()
                .map(en -> new ContagemPorJanela(en.getKey(), en.getValue()))
                .toList();
    }

    public List<ContagemPorJanela> listarMaisRecentesPrimeiro(SequencedCollection<ContagemPorJanela> janelas) {
        return janelas.reversed().stream().toList();
    }

    public List<ContagemPorJanela> ultimasN(SequencedCollection<ContagemPorJanela> janelas, int n) {
        return janelas.reversed().stream().limit(n).toList();
    }

    public Map.Entry<Instant, Long> pollMaisAntiga(LinkedHashMap<Instant, Long> mapa) {
        return mapa.pollFirstEntry();
    }

    public static void main(String[] args) {
        Instant t0 = Instant.parse("2026-05-24T10:00:00Z");
        List<Evento> eventos = new ArrayList<>();
        // 3 eventos no minuto 0
        eventos.add(new Evento("e1", t0));
        eventos.add(new Evento("e2", t0.plusSeconds(10)));
        eventos.add(new Evento("e3", t0.plusSeconds(45)));
        // 2 eventos no minuto 1
        eventos.add(new Evento("e4", t0.plusSeconds(60)));
        eventos.add(new Evento("e5", t0.plusSeconds(110)));
        // 1 evento no minuto 3
        eventos.add(new Evento("e6", t0.plusSeconds(190)));

        ContagemEventos c = new ContagemEventos();
        List<ContagemPorJanela> janelas = c.contar(eventos);
        System.out.println("Janelas (cronológico):");
        janelas.forEach(System.out::println);

        System.out.println("\nMais recentes primeiro:");
        c.listarMaisRecentesPrimeiro(janelas).forEach(System.out::println);
    }
}