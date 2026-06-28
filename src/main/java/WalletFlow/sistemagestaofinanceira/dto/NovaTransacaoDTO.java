package WalletFlow.sistemagestaofinanceira.dto;

import WalletFlow.sistemagestaofinanceira.enums.Categoria;
import WalletFlow.sistemagestaofinanceira.enums.TipoTransacao;
import WalletFlow.sistemagestaofinanceira.models.Transacao;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class NovaTransacaoDTO {
    private Long id; //Caso seja edição

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
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate data;

    public NovaTransacaoDTO(Transacao transacao) {
        this.id = transacao.getId();
        this.descricao = transacao.getDescricao();
        this.valor = transacao.getValor();
        this.tipo = transacao.getTipo();
        this.categoria = transacao.getCategoria();
        this.data = transacao.getData();
    }

    public Transacao toEntity() {
        return new Transacao(this.descricao, this.valor, this.tipo, this.categoria, this.data);
    }
}
