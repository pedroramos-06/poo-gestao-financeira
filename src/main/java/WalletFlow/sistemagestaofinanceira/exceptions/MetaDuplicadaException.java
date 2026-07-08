package WalletFlow.sistemagestaofinanceira.exceptions;

public class MetaDuplicadaException extends RuntimeException {
    public MetaDuplicadaException() {
        super("Já existe uma meta cadastrada para esse mês");
    }
    public MetaDuplicadaException(String message) {
        super(message);
    }
}
