package WalletFlow.sistemagestaofinanceira.controllers;

import WalletFlow.sistemagestaofinanceira.dto.ConfiguracoesDTO;
import WalletFlow.sistemagestaofinanceira.models.Usuario;
import WalletFlow.sistemagestaofinanceira.service.ConfiguracoesService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/configuracoes")
public class ConfiguracoesController {
    private final ConfiguracoesService configuracoesService;

    public ConfiguracoesController(ConfiguracoesService configuracoesService) {
        this.configuracoesService = configuracoesService;
    }

    @GetMapping
    public String exibir(@AuthenticationPrincipal Usuario usuario, Model model) {
        model.addAttribute("configuracoes", new ConfiguracoesDTO(usuario.getMetaPadrao(), usuario.getTema()));
        return "configuracoes/configuracoes";
    }

    @PutMapping
    public String atualizar(@Valid @ModelAttribute("configuracoes") ConfiguracoesDTO request,
                            BindingResult result,
                            @AuthenticationPrincipal Usuario usuario,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "configuracoes/configuracoes";
        }

        configuracoesService.editar(request, usuario);
        redirectAttributes.addFlashAttribute("sucesso", "Configurações atualizadas com sucesso!");
        return "redirect:/configuracoes";
    }
}
