package WalletFlow.sistemagestaofinanceira.enums;

import lombok.Getter;

@Getter
public enum Categoria {
    SALARIO("Salário", "#4CAF50", TipoTransacao.ENTRADA),         // verde
    VENDA("Venda", "#009688", TipoTransacao.ENTRADA),             // verde-azulado

    ALIMENTACAO("Alimentação", "#FF9800", TipoTransacao.SAIDA),   // laranja
    TRANSPORTE("Transporte", "#2196F3", TipoTransacao.SAIDA),     // azul
    ASSINATURA("Assinatura", "#9C27B0", TipoTransacao.SAIDA),     // roxo
    ALUGUEL("Aluguel", "#795548", TipoTransacao.SAIDA),           // marrom
    AGUA("Água", "#00BCD4", TipoTransacao.SAIDA),                 // ciano
    ENERGIA("Energia", "#FFC107", TipoTransacao.SAIDA),           // amarelo
    LAZER("Lazer", "#E91E63", TipoTransacao.SAIDA);               // rosa

    private final String descricao;
    private final String corHex;
    private final TipoTransacao tipoTransacao;

    Categoria(String descricao, String corHex, TipoTransacao tipoTransacao) {
        this.descricao = descricao;
        this.corHex = corHex;
        this.tipoTransacao = tipoTransacao;
    }
}
