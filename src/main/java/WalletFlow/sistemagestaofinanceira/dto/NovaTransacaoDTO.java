package WalletFlow.sistemagestaofinanceira.dto;

import WalletFlow.sistemagestaofinanceira.enums.Categoria;
import WalletFlow.sistemagestaofinanceira.enums.TipoTransacao;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class NovaTransacaoDTO {
    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 75, message = "A descrição deve ter no máximo 75 caracteres")
    private String descricao;

    @Positive(message = "O valor deve ser maior que zero")
    private double valor;

    @NotNull(message = "O tipo da transação é obrigatório")
    private TipoTransacao tipo;

    @NotNull(message = "A categoria é obrigatória")
    private Categoria categoria;

    @NotNull(message = "A data é obrigatória")
    @PastOrPresent(message = "A data não pode ser futura")
    private LocalDate data;

    @NotNull(message = "O usuário é obrigatório")
    private Long usuarioId;
}
