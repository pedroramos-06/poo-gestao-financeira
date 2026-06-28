package WalletFlow.sistemagestaofinanceira.controllers;

import WalletFlow.sistemagestaofinanceira.dto.NovaTransacaoDTO;
import WalletFlow.sistemagestaofinanceira.models.Transacao;
import WalletFlow.sistemagestaofinanceira.models.Usuario;
import WalletFlow.sistemagestaofinanceira.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/transacoes")
public class TransacaoController {
    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @GetMapping
    public String listar(@AuthenticationPrincipal Usuario usuario, Model model) {
        List<Transacao> transacoes = transacaoService.listarPorUsuario(usuario);
        model.addAttribute("transacoes", transacoes);

        return "transacoes/listar";
    }

    @GetMapping("/criar")
    public String criar(Model model) {
        model.addAttribute("transacao", new NovaTransacaoDTO());

        return "transacoes/criar";
    }

    @PostMapping
    public String inserir(@Valid @ModelAttribute("transacao") NovaTransacaoDTO request, BindingResult result, @AuthenticationPrincipal Usuario usuario) {
        if(result.hasErrors()) {
            return "transacoes/criar";
        }

        transacaoService.salvar(request, usuario);
        return "redirect:/transacoes";
    }

    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id) {
        transacaoService.excluir(id);

        return "redirect:/transacoes";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        try {
            Transacao t = transacaoService.buscarPorId(id);
            model.addAttribute("transacao", new NovaTransacaoDTO(t));

            return "transacoes/criar";
        } catch (RuntimeException e) {
            return "redirect:/transacoes";
        }
    }

    @PutMapping
    public String atualizar(@Valid @ModelAttribute("transacao") NovaTransacaoDTO request, BindingResult result, @AuthenticationPrincipal Usuario usuario) {
        if(result.hasErrors()) {
            return "transacoes/criar";
        }

        transacaoService.editar(request, usuario);
        return "redirect:/transacoes";
    }
}
