package br.com.will.classes.meli.checkout.core.pedido.deduplicacao;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Deduplicacao {

    public static final Duration JANELA = Duration.ofMinutes(5);

    public List<PedidoDeduplicacao> deduplicar(List<PedidoDeduplicacao> PedidoDeduplicacaos) {
        Map<String, Instant> ultimoVisto = new HashMap<>();
        return PedidoDeduplicacaos.stream()
                .sorted(Comparator.comparing(PedidoDeduplicacao::timestamp))
                .filter(p -> {
                    String chave = p.clienteId() + "|" + p.valor().stripTrailingZeros().toPlainString();
                    Instant ultimo = ultimoVisto.get(chave);
                    boolean duplicado = ultimo != null
                            && Duration.between(ultimo, p.timestamp()).compareTo(JANELA) <= 0;
                    if (!duplicado) ultimoVisto.put(chave, p.timestamp());
                    return !duplicado;
                }).collect(Collectors.toList());
    }

    public String descreverMensagem(MensagemPedido msg) {
        return switch (msg) {
            case null -> "vazio";
            case Criado(PedidoDeduplicacao p) -> "criado:" + p.id() + ":" + p.valor();
            case Cancelado(String id, String motivo) -> "cancelado:" + id + ":" + motivo;
            case Atualizado(String id, BigDecimal v) -> "atualizado:" + id + ":" + v;
        };
    }

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

    public Map.Entry<String, PedidoDeduplicacao> primeiroDoMapa(HashMap<String, PedidoDeduplicacao> ordenado) {
        return ordenado.entrySet().stream().findFirst().orElse(null);
    }

    public static void main(String[] args) {
        Instant t0 = Instant.parse("2026-05-24T10:00:00Z");
        var entrada = List.of(
                new PedidoDeduplicacao("p1", "c1", new BigDecimal("100.00"), t0),
                new PedidoDeduplicacao("p2", "c1", new BigDecimal("100.00"), t0.plusSeconds(60)),
                new PedidoDeduplicacao("p3", "c1", new BigDecimal("100.00"), t0.plusSeconds(600)),
                new PedidoDeduplicacao("p4", "c2", new BigDecimal("100.00"), t0.plusSeconds(60)),
                new PedidoDeduplicacao("p5", "c1", new BigDecimal("250.00"), t0.plusSeconds(30))
        );
        var s = new Deduplicacao();
        s.deduplicar(entrada).forEach(System.out::println);

        System.out.println(s.descreverMensagem(new Criado(entrada.getFirst())));
        System.out.println(s.descreverMensagem(new Cancelado("p2", "fraude")));
        System.out.println(s.descreverMensagem(null));

        s.ultimosNVistosDescendente(entrada, 3).forEach(System.out::println);
    }

}