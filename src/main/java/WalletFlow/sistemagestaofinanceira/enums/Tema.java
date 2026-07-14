package WalletFlow.sistemagestaofinanceira.enums;

import lombok.Getter;

@Getter
public enum Tema {
    CLARO("light"),
    ESCURO("dark");

    private final String valor;

    Tema(String valor) {
        this.valor = valor;
    }
}
