package br.com.will.classes.meli.checkout.core.cupom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultadoValidacao {
    private final boolean valido;
    private final List<String> motivos;

    private ResultadoValidacao(boolean valido, List<String> motivos) {
        this.valido = valido;
        this.motivos = Collections.unmodifiableList(new ArrayList<>(motivos));
    }

    public static ResultadoValidacao ok() {
        return new ResultadoValidacao(true, Collections.emptyList());
    }

    public static ResultadoValidacao falha(List<String> motivos) {
        return new ResultadoValidacao(false, motivos);
    }

    public boolean isValido() {
        return valido;
    }

    public List<String> getMotivos() {
        return motivos;
    }

    @Override
    public String toString() {
        return valido ? "OK" : "FALHA" + motivos;
    }
}