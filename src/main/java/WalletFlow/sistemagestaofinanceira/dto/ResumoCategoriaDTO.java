package WalletFlow.sistemagestaofinanceira.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResumoCategoriaDTO {
    @NotBlank
    private String categoria;

    @NotBlank
    private double valor;

    @NotBlank
    private double percentual;
}
