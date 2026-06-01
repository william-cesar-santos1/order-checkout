package br.com.will.classes.meli.checkout.core.cupom;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class ValidadorCupom {

    public ResultadoValidacao validarCupom(Cupom cupom, PedidoCupom pedidoCupom, LocalDate hoje) {
        List<String> motivos = new ArrayList<>();

        if (hoje.isAfter(cupom.getValidoAte())) {
            motivos.add(formatarMotivo("EXPIRADO", cupom.getValidoAte().toString()));
        }
        if (pedidoCupom.getValor().compareTo(cupom.getValorMinimo()) < 0) {
            motivos.add(formatarMotivo("VALOR_MINIMO", cupom.getValorMinimo().toPlainString()));
        }
        boolean valido = false;
        for (String c : pedidoCupom.getCategorias()) {
            if (cupom.getCategoriasElegiveis().contains(c)) {
                valido = true;
                break;
            }
        }
        if (!valido) {
            motivos.add(formatarMotivo("CATEGORIA", String.join(",", cupom.getCategoriasElegiveis())));
        }

        return motivos.isEmpty() ? ResultadoValidacao.ok() : ResultadoValidacao.falha(motivos);
    }

    private String formatarMotivo(String tipo, String detalhe) {
        if (tipo.equals("EXPIRADO")) {
            return "Cupom expirado em " + detalhe;
        } else if (tipo.equals("VALOR_MINIMO")) {
            return "PedidoCupom abaixo do valor mínimo R$ " + detalhe;
        } else if (tipo.equals("CATEGORIA")) {
            return "Categorias elegíveis: " + detalhe;
        } else {
            return "Motivo desconhecido: " + tipo;
        }
    }

    public static void main(String[] args) {
        ValidadorCupom v = new ValidadorCupom();
        Cupom meli10 = new Cupom("MELI10", LocalDate.of(2026, 12, 31),
                new BigDecimal("100.00"), new HashSet<>(Arrays.asList("eletronicos", "casa")));

        PedidoCupom ok = new PedidoCupom(new BigDecimal("250.00"), new HashSet<>(Arrays.asList("eletronicos")));
        PedidoCupom barato = new PedidoCupom(new BigDecimal("50.00"), new HashSet<>(Arrays.asList("eletronicos")));
        PedidoCupom outraCategoria = new PedidoCupom(new BigDecimal("250.00"), new HashSet<>(Arrays.asList("moda")));

        LocalDate hoje = LocalDate.of(2026, 5, 24);
        System.out.println(v.validarCupom(meli10, ok, hoje));
        System.out.println(v.validarCupom(meli10, barato, hoje));
        System.out.println(v.validarCupom(meli10, outraCategoria, hoje));
    }

}