package br.com.will.classes.meli.checkout.core.cupom;

import br.com.will.classes.meli.checkout.core.cupom.motivofalha.MotivoFalha;

import java.util.List;

public record ResultadoValidacao(boolean valido, List<MotivoFalha> motivos) {

    public ResultadoValidacao {
        motivos = List.copyOf(motivos);
    }

    public static ResultadoValidacao ok() {
        return new ResultadoValidacao(true, List.of());
    }

    public static ResultadoValidacao falha(List<MotivoFalha> motivos) {
        return new ResultadoValidacao(false, motivos);
    }

}