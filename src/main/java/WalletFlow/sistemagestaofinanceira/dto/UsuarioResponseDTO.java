package WalletFlow.sistemagestaofinanceira.dto;

import WalletFlow.sistemagestaofinanceira.enums.Tema;
import WalletFlow.sistemagestaofinanceira.models.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class UsuarioResponseDTO {
    private String nome;
    private String email;
    private LocalDateTime dataCriacao;
    private Tema tema;

    public UsuarioResponseDTO(Usuario usuario) {
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.dataCriacao = usuario.getDataCriacao();
        this.tema = usuario.getTema();
    }
}
