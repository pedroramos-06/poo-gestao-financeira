package WalletFlow.sistemagestaofinanceira.enums;

import lombok.Getter;

@Getter
public enum Categoria {
    ALIMENTACAO("Alimentação"),
    SALARIO("Salário"),
    TRANSPORTE("Transporte"),
    ASSINATURA("Assinatura"),
    ALUGUEL("Aluguel"),
    AGUA("Água"),
    ENERGIA("Energia"),
    LAZER("Lazer");

    private final String descricao;

    Categoria(String descricao) {
        this.descricao = descricao;
    }
}
