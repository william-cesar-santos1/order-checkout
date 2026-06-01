package br.com.will.classes.meli.checkout.core.pedido;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Deduplicacao {

    public static final Duration JANELA = Duration.ofMinutes(5);

    /**
     * Remove pedidoDeduplicacaos duplicados (mesmo clienteId + mesmo valor dentro de 5 min).
     * Mantém o mais antigo de cada par duplicado.
     */
    public List<PedidoDeduplicacao> deduplicar(List<PedidoDeduplicacao> pedidoDeduplicacaos) {
        List<PedidoDeduplicacao> ordenados = new ArrayList<>(pedidoDeduplicacaos);
        ordenados.sort(Comparator.comparing(PedidoDeduplicacao::getTimestamp));

        Map<String, Instant> ultimoVistoPorChave = new HashMap<>();
        List<PedidoDeduplicacao> resultado = new ArrayList<>();

        for (PedidoDeduplicacao p : ordenados) {
            String chave = p.getClienteId() + "|" + p.getValor().stripTrailingZeros().toPlainString();
            Instant ultimo = ultimoVistoPorChave.get(chave);
            boolean duplicado = ultimo != null
                    && Duration.between(ultimo, p.getTimestamp()).compareTo(JANELA) <= 0;
            if (!duplicado) {
                resultado.add(p);
                ultimoVistoPorChave.put(chave, p.getTimestamp());
            }
        }
        return resultado;
    }

    /**
     * Recebe mensagens heterogêneas e devolve uma descrição.
     * TODO: trocar instanceof + cast por switch pattern sobre sealed interface.
     */
    public String descreverMensagem(Object mensagem) {
        if (mensagem instanceof String) {
            String s = (String) mensagem;
            return "texto:" + s;
        } else if (mensagem instanceof PedidoDeduplicacao) {
            PedidoDeduplicacao p = (PedidoDeduplicacao) mensagem;
            return "pedido:" + p.getId() + ":" + p.getValor();
        } else if (mensagem instanceof Number) {
            Number n = (Number) mensagem;
            return "numero:" + n.doubleValue();
        } else if (mensagem == null) {
            return "vazio";
        } else {
            return "desconhecido:" + mensagem.getClass().getSimpleName();
        }
    }

    /**
     * Devolve os últimos N pedidoDeduplicacaos vistos, em ordem do mais recente para o mais antigo.
     * TODO: usar LinkedList só por causa de descendingIterator é típico de Java 11.
     *       No Java 21, qualquer SequencedCollection oferece reversed() como view.
     */
    public List<PedidoDeduplicacao> ultimosNVistosDescendente(List<PedidoDeduplicacao> pedidoDeduplicacaos, int n) {
        LinkedList<PedidoDeduplicacao> fila = new LinkedList<>(pedidoDeduplicacaos);
        List<PedidoDeduplicacao> resultado = new ArrayList<>();
        Iterator<PedidoDeduplicacao> it = fila.descendingIterator();
        int contador = 0;
        while (it.hasNext() && contador < n) {
            resultado.add(it.next());
            contador++;
        }
        return resultado;
    }

    public static void main(String[] args) {
        Instant t0 = Instant.parse("2026-05-24T10:00:00Z");
        List<PedidoDeduplicacao> entrada = List.of(
                new PedidoDeduplicacao("p1", "c1", new BigDecimal("100.00"), t0),
                new PedidoDeduplicacao("p2", "c1", new BigDecimal("100.00"), t0.plusSeconds(60)),
                new PedidoDeduplicacao("p3", "c1", new BigDecimal("100.00"), t0.plusSeconds(600)),
                new PedidoDeduplicacao("p4", "c2", new BigDecimal("100.00"), t0.plusSeconds(60)),
                new PedidoDeduplicacao("p5", "c1", new BigDecimal("250.00"), t0.plusSeconds(30))
        );

        Deduplicacao dedup = new Deduplicacao();
        System.out.println("Deduplicados:");
        dedup.deduplicar(entrada).forEach(System.out::println);

        System.out.println("\nDescrever mensagens:");
        System.out.println(dedup.descreverMensagem("MELI10"));
        System.out.println(dedup.descreverMensagem(entrada.get(0)));
        System.out.println(dedup.descreverMensagem(42));
        System.out.println(dedup.descreverMensagem(null));
    }
}