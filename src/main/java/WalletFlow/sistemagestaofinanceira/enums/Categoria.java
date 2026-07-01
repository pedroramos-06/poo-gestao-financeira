package WalletFlow.sistemagestaofinanceira.enums;

import lombok.Getter;

@Getter
public enum Categoria {
    ALIMENTACAO("Alimentação", "#FF9800"),   // laranja
    SALARIO("Salário", "#4CAF50"),           // verde
    TRANSPORTE("Transporte", "#2196F3"),     // azul
    ASSINATURA("Assinatura", "#9C27B0"),     // roxo
    ALUGUEL("Aluguel", "#795548"),           // marrom
    AGUA("Água", "#00BCD4"),                 // ciano
    ENERGIA("Energia", "#FFC107"),           // amarelo
    LAZER("Lazer", "#E91E63");               // rosa

    private final String descricao;
    private final String corHex;

    Categoria(String descricao, String corHex) {
        this.descricao = descricao;
        this.corHex = corHex;
    }
}
