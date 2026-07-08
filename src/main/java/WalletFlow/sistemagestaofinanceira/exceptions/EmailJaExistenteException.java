package WalletFlow.sistemagestaofinanceira.exceptions;

public class EmailJaExistenteException extends RuntimeException{
    public EmailJaExistenteException(){
        super("Este email já está registrado.");
    }
}
