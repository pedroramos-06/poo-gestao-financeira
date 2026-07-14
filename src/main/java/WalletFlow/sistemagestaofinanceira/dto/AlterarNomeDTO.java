package WalletFlow.sistemagestaofinanceira.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlterarNomeDTO {
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 5, max = 50, message = "O nome deve ter entre 5 e 50 caracteres")
    private String nome;
}
