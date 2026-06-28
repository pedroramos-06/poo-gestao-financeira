package WalletFlow.sistemagestaofinanceira.dto;

import WalletFlow.sistemagestaofinanceira.models.Meta;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.time.YearMonth;

@Setter
@Getter
public class NovaMetaDTO {
    @Positive(message = "O valor deve ser maior que zero")
    private double valor;

    @NotNull(message = "A data é obrigatória")
    private YearMonth data;

    public Meta toEntity() {
        return new Meta(this.valor, this.data);
    }
}
