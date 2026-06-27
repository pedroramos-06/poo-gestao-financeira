package WalletFlow.sistemagestaofinanceira.dto;

import WalletFlow.sistemagestaofinanceira.models.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NovoUsuarioDTO {
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 5, max = 50, message = "O nome deve ter entre 5 e 50 caracteres")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    private String email;

    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    public Usuario toEntity(){
        return new Usuario(this.getNome(), this.getEmail(), this.getSenha());
    }
}
