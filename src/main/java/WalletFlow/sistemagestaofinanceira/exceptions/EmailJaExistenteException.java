package WalletFlow.sistemagestaofinanceira.exceptions;

public class EmailJaExistenteException extends Exception{
    public EmailJaExistenteException(){
        super("Este email já está registrado.");
    }
}
