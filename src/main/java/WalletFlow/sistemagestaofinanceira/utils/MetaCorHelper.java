package WalletFlow.sistemagestaofinanceira.utils;

public class MetaCorHelper {
    public static String getCor(double metaAtingida) {
        if (metaAtingida >= 90) return "danger";
        else if (metaAtingida >= 75) return "warning";
        else return "success";
    }
}