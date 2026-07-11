package WalletFlow.sistemagestaofinanceira.controllers;

import WalletFlow.sistemagestaofinanceira.dto.ConfiguracoesDTO;
import WalletFlow.sistemagestaofinanceira.models.Usuario;
import WalletFlow.sistemagestaofinanceira.security.SessaoVerificada;
import WalletFlow.sistemagestaofinanceira.service.ConfiguracoesService;
import WalletFlow.sistemagestaofinanceira.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/configuracoes")
public class ConfiguracoesController {
    private final ConfiguracoesService configuracoesService;
    private final UsuarioService usuarioService;
    private final SessaoVerificada sessaoVerificada;

    public ConfiguracoesController(ConfiguracoesService configuracoesService, UsuarioService usuarioService, SessaoVerificada sessaoVerificada) {
        this.configuracoesService = configuracoesService;
        this.usuarioService = usuarioService;
        this.sessaoVerificada = sessaoVerificada;
    }

    @GetMapping
    public String exibir(@AuthenticationPrincipal Usuario usuario, Model model) {
        model.addAttribute("configuracoes", new ConfiguracoesDTO(usuario.getMetaPadrao(), usuario.getTema()));
        model.addAttribute("tempoRestante", sessaoVerificada.tempoRestante());
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

    @GetMapping("/confirmar-senha")
    public String paginaConfirmar(@RequestParam String acao, Model model,
                                     RedirectAttributes redirectAttributes) {
        if (sessaoVerificada.isValida()) {
            redirectAttributes.addFlashAttribute("abrirModal", acao);
            return "redirect:/configuracoes";
        }
        model.addAttribute("acao", acao);
        return "configuracoes/confirmar-senha";
    }

    @PostMapping("/confirmar-senha")
    public String confirmarSenha(@RequestParam String acao, Model model,
                                 @RequestParam String senha,
                                 @AuthenticationPrincipal Usuario usuario,
                                 RedirectAttributes redirectAttributes) {
        if (!usuarioService.senhaValida(usuario, senha)) {
            model.addAttribute("erroSenha", "Senha incorreta. Tente novamente.");
            model.addAttribute("acao", acao);
            return "configuracoes/confirmar-senha";
        }

        sessaoVerificada.confirmar();
        redirectAttributes.addFlashAttribute("sucesso", "Sessão verificada com sucesso!");
        redirectAttributes.addFlashAttribute("abrirModal", acao);
        return "redirect:/configuracoes";
    }

    @PutMapping("/nome")
    public String alterarNome(@RequestParam String nome,
                              @AuthenticationPrincipal Usuario usuario,
                              RedirectAttributes redirectAttributes) {
        if (!sessaoVerificada.isValida()) {
            return "redirect:/configuracoes/confirmar-senha?acao=nome";
        }

        if (nome == null || nome.isBlank() || nome.length() < 5 || nome.length() > 50) {
            redirectAttributes.addFlashAttribute("erroNome", "O nome deve ter entre 5 e 50 caracteres");
            redirectAttributes.addFlashAttribute("nomeDigitado", nome);
            redirectAttributes.addFlashAttribute("abrirModal", "nome");
            return "redirect:/configuracoes";
        }

        configuracoesService.alterarNome(usuario, nome);
        redirectAttributes.addFlashAttribute("sucesso", "Nome alterado com sucesso!");
        return "redirect:/configuracoes";
    }

    @DeleteMapping("/excluir-dados")
    public String excluirDados(@AuthenticationPrincipal Usuario usuario,
                               RedirectAttributes redirectAttributes) {
        if (!sessaoVerificada.isValida()) {
            return "redirect:/configuracoes/confirmar-senha?acao=excluir-dados";
        }

        configuracoesService.excluirDados(usuario.getId());
        redirectAttributes.addFlashAttribute("sucesso", "Dados excluídos com sucesso!");
        return "redirect:/configuracoes";
    }
}
