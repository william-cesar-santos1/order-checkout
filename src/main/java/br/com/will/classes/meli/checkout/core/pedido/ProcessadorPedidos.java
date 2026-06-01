package br.com.will.classes.meli.checkout.core.pedido;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ProcessadorPedidos {

    private final ExecutorService executor = Executors.newFixedThreadPool(50);

    public PedidoEnriquecido enriquecer(String idPedido) throws InterruptedException, ExecutionException {
        Future<Boolean> fraude = executor.submit(() -> chamarServicoFraude(idPedido));
        Future<Integer> estoque = executor.submit(() -> chamarServicoEstoque(idPedido));
        Future<String> frete = executor.submit(() -> chamarServicoFrete(idPedido));
        // get() em sequência. Se fraude lançar, as outras continuam rodando até terminarem (vazamento).
        return new PedidoEnriquecido(idPedido, fraude.get(), estoque.get(), frete.get());
    }

    public List<PedidoEnriquecido> processarLote(List<String> idsPedido) throws InterruptedException, ExecutionException {
        List<Future<PedidoEnriquecido>> futures = new ArrayList<>();
        for (String id : idsPedido) {
            futures.add(executor.submit(() -> enriquecer(id)));
        }
        List<PedidoEnriquecido> resultado = new ArrayList<>();
        for (Future<PedidoEnriquecido> f : futures) {
            resultado.add(f.get());
        }
        return resultado;
    }

    public void encerrar() {
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // --- Serviços externos simulados (cada um durmindo 100-200ms) ---

    private boolean chamarServicoFraude(String idPedido) throws InterruptedException {
        Thread.sleep(dormirEntre(100, 200));
        return !idPedido.endsWith("9"); // simula 10% de reprovação determinística
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
        ProcessadorPedidos proc = new ProcessadorPedidos();
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            ids.add("PED-" + i);
        }

        long ini = System.nanoTime();
        List<PedidoEnriquecido> resultado = proc.processarLote(ids);
        long fim = System.nanoTime();

        System.out.println("Total processado: " + resultado.size());
        System.out.println("Wall time: " + Duration.ofNanos(fim - ini).toMillis() + " ms");
        proc.encerrar();
    }
}