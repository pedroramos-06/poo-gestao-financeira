package WalletFlow.sistemagestaofinanceira.controllers;

import WalletFlow.sistemagestaofinanceira.dto.FiltrosTransacaoDTO;
import WalletFlow.sistemagestaofinanceira.dto.NovaTransacaoDTO;
import WalletFlow.sistemagestaofinanceira.exceptions.AcessoNegadoException;
import WalletFlow.sistemagestaofinanceira.exceptions.SaldoInsuficienteException;
import WalletFlow.sistemagestaofinanceira.models.Transacao;
import WalletFlow.sistemagestaofinanceira.models.Usuario;
import WalletFlow.sistemagestaofinanceira.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/transacoes")
public class TransacaoController {
    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @GetMapping
    public String listar(@AuthenticationPrincipal Usuario usuario, @ModelAttribute("filtros") FiltrosTransacaoDTO filtros, Model model) {
        try {
            List<Transacao> transacoes = transacaoService.listar(usuario, filtros);
            model.addAttribute("transacoes", transacoes);
            model.addAttribute("filtros", filtros);
            return "transacoes/listar";

        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao listar transações");
            return "transacoes/listar";
        }
    }

    @GetMapping("/criar")
    public String criar(Model model) {
        model.addAttribute("transacao", new NovaTransacaoDTO());
        return "transacoes/criar";
    }

    @PostMapping
    public String inserir(@Valid @ModelAttribute("transacao") NovaTransacaoDTO request,
                          BindingResult result, Model model,
                          @AuthenticationPrincipal Usuario usuario,
                          RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            return "transacoes/criar";
        }
        try{
            transacaoService.salvar(request, usuario);
            redirectAttributes.addFlashAttribute("sucesso", "Transação criada com sucesso!");
            return "redirect:/transacoes";

        } catch(SaldoInsuficienteException e){
            result.rejectValue("valor", "error.transacao", "Saldo insuficiente para essa transação");
            return "transacoes/criar";

        } catch (Exception e) {
            result.reject("error.transacao", "Um erro inesperado ocorreu, tente novamente!");
            return "transacoes/criar";
        }
    }

    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id,
                          @AuthenticationPrincipal Usuario usuario,
                          RedirectAttributes redirectAttributes) {
        try {
            transacaoService.excluir(id, usuario.getId());
            redirectAttributes.addFlashAttribute("sucesso", "Transação excluída com sucesso!");
            return "redirect:/transacoes";

        } catch (AcessoNegadoException e) {
            redirectAttributes.addFlashAttribute("erro", "Você não tem permissão para deletar esta transacao");
            return "redirect:/transacoes";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Um erro inesperado ocorreu, tente novamente!");
            return "redirect:/transacoes";
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id,
                         Model model,
                         @AuthenticationPrincipal Usuario usuario,
                         RedirectAttributes redirectAttributes) {
        try {
            Transacao t = transacaoService.buscarPorId(id, usuario.getId());
            model.addAttribute("transacao", new NovaTransacaoDTO(t));
            return "transacoes/criar";

        } catch (AcessoNegadoException e) {
            redirectAttributes.addFlashAttribute("erro", "Você não tem permissão para deletar esta transacao");
            return "redirect:/transacoes";

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", "Um erro inesperado ocorreu, tente novamente!");
            return "redirect:/transacoes";
        }
    }

    @PutMapping
    public String atualizar(@Valid @ModelAttribute("transacao") NovaTransacaoDTO request,
                            BindingResult result,
                            Model model,
                            @AuthenticationPrincipal Usuario usuario,
                            RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            return "transacoes/criar";
        }

        try {
            transacaoService.editar(request, usuario);
            redirectAttributes.addFlashAttribute("sucesso", "Transação atualizada com sucesso!");
            return "redirect:/transacoes";

        } catch (AcessoNegadoException e) {
            redirectAttributes.addFlashAttribute("erro", "Você não tem permissão para deletar esta transacao");
            return "redirect:/transacoes";

        } catch (SaldoInsuficienteException e) {
            result.rejectValue("valor", "error.transacao", "Saldo insuficiente para essa transação");
            return "transacoes/criar";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Um erro inesperado ocorreu, tente novamente!");
            return "redirect:/transacoes";
        }
    }
}
