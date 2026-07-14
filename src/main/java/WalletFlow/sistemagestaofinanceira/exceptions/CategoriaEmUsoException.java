package WalletFlow.sistemagestaofinanceira.exceptions;

public class CategoriaEmUsoException extends RuntimeException {
    public CategoriaEmUsoException() {
        super("Não é possível excluir essa categoria pois existem transações vinculadas a ela");
    }
}
