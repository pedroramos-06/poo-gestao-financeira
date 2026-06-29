package WalletFlow.sistemagestaofinanceira.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.YearMonth;

@Setter
@Getter
@AllArgsConstructor
public class DashboardDTO {
    @NotBlank
    private YearMonth periodo;

    @NotBlank
    private double saldo;

    @NotBlank
    private double totalEntradas;

    @NotBlank
    private double totalSaidas;

    @NotBlank
    private double meta;

    @NotBlank
    private double metaAtingida;
}
