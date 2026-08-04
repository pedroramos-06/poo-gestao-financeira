package WalletFlow.sistemagestaofinanceira.exceptions;

public class CategoriaProtegidaException extends RuntimeException {
    public CategoriaProtegidaException() {
        super("Essa categoria não pode ser alterada");
    }
}