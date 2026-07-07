package WalletFlow.sistemagestaofinanceira.dto;

import WalletFlow.sistemagestaofinanceira.models.Meta;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.YearMonth;

@Setter
@Getter
@NoArgsConstructor
public class NovaMetaDTO {
    private Long id; //Caso seja edição

    @NotNull(message = "O valor é obrigatório")
    @Positive(message = "O valor deve ser maior que zero")
    @Digits(integer = 8, fraction = 2, message = "O valor deve ter no máximo 8 dígitos inteiros e 2 decimais")
    private BigDecimal valor;

    @NotNull(message = "A data é obrigatória")
    private YearMonth data;

    public NovaMetaDTO(Meta meta) {
        this.id = meta.getId();
        this.valor = meta.getValor();
        this.data = meta.getData();
    }

    public Meta toEntity() {
        return new Meta(this.valor, this.data);
    }
}
