package WalletFlow.sistemagestaofinanceira.exceptions;

public class CategoriaJaExisteException extends Exception {
    public CategoriaJaExisteException() {
        super("Você já possui uma categoria com esse nome");
    }
}