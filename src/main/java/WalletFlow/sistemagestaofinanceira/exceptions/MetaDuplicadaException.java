package WalletFlow.sistemagestaofinanceira.exceptions;

public class MetaDuplicadaException extends Exception {
    public MetaDuplicadaException() {
        super("Já existe uma meta cadastrada para esse mês");
    }
    public MetaDuplicadaException(String message) {
        super(message);
    }
}
