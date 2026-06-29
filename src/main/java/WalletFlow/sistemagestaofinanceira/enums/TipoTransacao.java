package WalletFlow.sistemagestaofinanceira.enums;

import lombok.Getter;

@Getter
public enum TipoTransacao {
    ENTRADA("Entrada"),
    SAIDA("Saída");

    private final String descricao;

    TipoTransacao(String descricao) {
        this.descricao = descricao;
    }
}
