package WalletFlow.sistemagestaofinanceira.exceptions;

public class SaldoInsuficienteException extends Exception{
    public SaldoInsuficienteException(){
        super("Saldo insuficiente para essa transação!");
    }
}
