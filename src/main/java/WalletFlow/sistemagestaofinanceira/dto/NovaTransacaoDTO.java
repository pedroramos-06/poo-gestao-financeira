package WalletFlow.sistemagestaofinanceira.dto;

import WalletFlow.sistemagestaofinanceira.enums.Categoria;
import WalletFlow.sistemagestaofinanceira.enums.TipoTransacao;
import WalletFlow.sistemagestaofinanceira.models.Transacao;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class NovaTransacaoDTO {
    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 75, message = "A descrição deve ter no máximo 75 caracteres")
    private String descricao;

    @Positive(message = "O valor deve ser maior que zero")
    private double valor;

    @NotNull(message = "O tipo da transação é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoTransacao tipo;

    @NotNull(message = "A categoria é obrigatória")
    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @NotNull(message = "A data é obrigatória")
    @PastOrPresent(message = "A data não pode ser futura")
    private LocalDate data;

    public Transacao toEntity() {
        return new Transacao(this.descricao, this.valor, this.tipo, this.categoria, this.data);
    }
}
