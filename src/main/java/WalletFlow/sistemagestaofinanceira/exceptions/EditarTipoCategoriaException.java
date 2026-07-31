package WalletFlow.sistemagestaofinanceira.exceptions;

public class EditarTipoCategoriaException extends RuntimeException {
    public EditarTipoCategoriaException() {
        super("Não é possível editar o tipo de uma categoria");
    }
}
