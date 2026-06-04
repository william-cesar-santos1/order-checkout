package br.com.will.classes.meli.checkout.core.pedido.processar;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.ThreadLocalRandom;

public class ProcessadorPedidos {

    public PedidoEnriquecido enriquecer(String idPedido) throws InterruptedException {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Subtask<Boolean> fraude = scope.fork(() -> chamarServicoFraude(idPedido));
            Subtask<Integer> estoque = scope.fork(() -> chamarServicoEstoque(idPedido));
            Subtask<String> frete = scope.fork(() -> chamarServicoFrete(idPedido));

            scope.join();
            try {
                scope.throwIfFailed();
            } catch (Exception e) {
                throw new RuntimeException("Falha ao enriquecer " + idPedido, e);
            }

            return new PedidoEnriquecido(idPedido, fraude.get(), estoque.get(), frete.get());
        }
    }

    public List<PedidoEnriquecido> processarLote(List<String> idsPedido) throws InterruptedException {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            List<Subtask<PedidoEnriquecido>> tarefas = new ArrayList<>();
            for (String id : idsPedido) {
                tarefas.add(scope.fork(() -> enriquecer(id)));
            }
            scope.join();
            try {
                scope.throwIfFailed();
            } catch (Exception e) {
                throw new RuntimeException("Falha em pelo menos um pedido", e);
            }
            return tarefas.stream().map(Subtask::get).toList();
        }
    }

    // --- Serviços externos simulados ---
    private boolean chamarServicoFraude(String idPedido) throws InterruptedException {
        Thread.sleep(dormirEntre(100, 200));
        return !idPedido.endsWith("9");
    }

    private int chamarServicoEstoque(String idPedido) throws InterruptedException {
        Thread.sleep(dormirEntre(100, 200));
        return Math.abs(idPedido.hashCode() % 50);
    }

    private String chamarServicoFrete(String idPedido) throws InterruptedException {
        Thread.sleep(dormirEntre(100, 200));
        return "PAC " + ((Math.abs(idPedido.hashCode()) % 7) + 1) + " dias";
    }

    private long dormirEntre(int minMs, int maxMs) {
        return ThreadLocalRandom.current().nextInt(minMs, maxMs);
    }

    public static void main(String[] args) throws Exception {
        var proc = new ProcessadorPedidos();
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < 200; i++) ids.add("PED-" + i);

        long ini = System.nanoTime();
        var resultado = proc.processarLote(ids);
        long fim = System.nanoTime();

        System.out.println("Total processado: " + resultado.size());
        System.out.println("Wall time: " + Duration.ofNanos(fim - ini).toMillis() + " ms");
    }

}