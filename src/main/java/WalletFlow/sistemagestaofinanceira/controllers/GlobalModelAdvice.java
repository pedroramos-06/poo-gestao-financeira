package WalletFlow.sistemagestaofinanceira.controllers;

import WalletFlow.sistemagestaofinanceira.dto.UsuarioResponseDTO;
import WalletFlow.sistemagestaofinanceira.models.Usuario;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAdvice {

    @ModelAttribute("usuario")
    public UsuarioResponseDTO adicionarUsuario(@AuthenticationPrincipal Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return new UsuarioResponseDTO(usuario);
    }
}