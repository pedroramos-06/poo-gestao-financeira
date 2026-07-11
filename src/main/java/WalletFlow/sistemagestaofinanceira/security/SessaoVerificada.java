package WalletFlow.sistemagestaofinanceira.security;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
@SessionScope
public class SessaoVerificada {
    private LocalDateTime confirmadaEm;
    private static final long VALIDADE_MINUTOS = 5;

    public void confirmar() {
        this.confirmadaEm = LocalDateTime.now();
    }

    public boolean isValida() {
        return confirmadaEm != null && confirmadaEm.isAfter(LocalDateTime.now().minusMinutes(VALIDADE_MINUTOS));
    }

    public long tempoRestante() {
        if (confirmadaEm == null) return 0;
        long segundosPassados = Duration.between(confirmadaEm, LocalDateTime.now()).getSeconds();
        long restante = (VALIDADE_MINUTOS * 60) - segundosPassados;
        return Math.max(restante, 0);
    }
}
