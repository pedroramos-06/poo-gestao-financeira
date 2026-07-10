package WalletFlow.sistemagestaofinanceira.dto;

import WalletFlow.sistemagestaofinanceira.enums.Tema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracoesDTO {
    @NotNull(message = "O valor é obrigatório")
    @PositiveOrZero(message = "O valor deve ser positivo ou zero")
    @Digits(integer = 8, fraction = 2, message = "O valor deve ter no máximo 8 dígitos inteiros e 2 decimais")
    private BigDecimal metaPadrao;

    @NotNull(message = "Tema é obrigatório")
    private Tema tema;
}
