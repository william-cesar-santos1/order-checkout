package br.com.will.classes.meli.checkout.core.pedido;

public class PedidoEnriquecido {
    public final String id;
    public final boolean aprovadoNaFraude;
    public final int estoque;
    public final String previsaoFrete;

    public PedidoEnriquecido(String id, boolean aprovadoNaFraude, int estoque, String previsaoFrete) {
        this.id = id;
        this.aprovadoNaFraude = aprovadoNaFraude;
        this.estoque = estoque;
        this.previsaoFrete = previsaoFrete;
    }

    @Override
    public String toString() {
        return id + " fraude=" + aprovadoNaFraude + " estoque=" + estoque + " frete=" + previsaoFrete;
    }
}