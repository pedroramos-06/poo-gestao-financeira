package WalletFlow.sistemagestaofinanceira.dto;

import WalletFlow.sistemagestaofinanceira.enums.TipoTransacao;
import WalletFlow.sistemagestaofinanceira.models.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class NovaCategoriaDTO {
    private Long id;

    @NotBlank
    @Size(max = 20, message = "O nome deve ter no máximo 20 caracteres")
    private String nome;

    @NotNull(message = "O tipo da categoria é obrigatório")
    private TipoTransacao tipo;

    @NotBlank
    private String cor;

    public NovaCategoriaDTO(String nome, TipoTransacao tipo, String cor) {
        this.nome = nome;
        this.tipo = tipo;
        this.cor = cor;
    }

    public NovaCategoriaDTO(Categoria c) {
        this.id = c.getId();
        this.nome = c.getNome();
        this.tipo = c.getTipo();
        this.cor = c.getCor();
    }

    public Categoria toEntity() {
        return new Categoria(this.nome, this.tipo, this.cor);
    }
}
