package br.com.will.classes.meli.checkout.core.evento;

import java.time.Instant;
import java.util.*;

public class ContagemEventos {

    public List<ContagemPorJanela> contar(List<Evento> eventos) {
        List<Evento> ordenados = new ArrayList<>(eventos);
        ordenados.sort((a, b) -> a.getTimestamp().compareTo(b.getTimestamp()));

        Map<Instant, Long> mapaContagens = new LinkedHashMap<>();
        for (Evento e : ordenados) {
            long epochMin = e.getTimestamp().getEpochSecond() / 60;
            Instant inicio = Instant.ofEpochSecond(epochMin * 60);
            mapaContagens.merge(inicio, 1L, Long::sum);
        }

        List<ContagemPorJanela> resultado = new ArrayList<>();
        for (Map.Entry<Instant, Long> e : mapaContagens.entrySet()) {
            resultado.add(new ContagemPorJanela(e.getKey(), e.getValue()));
        }
        return resultado;
    }

    public List<ContagemPorJanela> listarMaisRecentesPrimeiro(List<ContagemPorJanela> janelas) {
        List<ContagemPorJanela> copia = new ArrayList<>(janelas);
        Collections.reverse(copia);
        return copia;
    }

    public List<ContagemPorJanela> ultimasN(List<ContagemPorJanela> janelas, int n) {
        List<ContagemPorJanela> resultado = new ArrayList<>();
        ListIterator<ContagemPorJanela> it = janelas.listIterator(janelas.size());
        int c = 0;
        while (it.hasPrevious() && c < n) {
            resultado.add(it.previous());
            c++;
        }
        return resultado;
    }

    public Map.Entry<Instant, Long> pollFirstEntryManual(LinkedHashMap<Instant, Long> mapa) {
        Iterator<Map.Entry<Instant, Long>> it = mapa.entrySet().iterator();
        if (!it.hasNext()) return null;
        Map.Entry<Instant, Long> primeira = it.next();
        mapa.remove(primeira.getKey());
        return primeira;
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