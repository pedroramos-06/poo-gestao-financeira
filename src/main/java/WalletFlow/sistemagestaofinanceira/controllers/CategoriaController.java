package WalletFlow.sistemagestaofinanceira.controllers;

import WalletFlow.sistemagestaofinanceira.models.Usuario;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping('/categorias')
public class CategoriaController {
    @GetMapping
    public String listar(@AuthenticationPrincipal Usuario usuario, Model model) {
        try {

        } catch (Exception e) {

        }
    }
}
