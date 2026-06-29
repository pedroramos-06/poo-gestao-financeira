package WalletFlow.sistemagestaofinanceira.exceptions;

public class AcessoNegadoException extends Exception {
    public  AcessoNegadoException(){
        super("O usuário não possui acesso a esse recurso");
    }
    public AcessoNegadoException(String message) {
        super(message);
    }
}
