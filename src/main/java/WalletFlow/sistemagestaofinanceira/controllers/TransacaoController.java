package WalletFlow.sistemagestaofinanceira.controllers;

import WalletFlow.sistemagestaofinanceira.dto.FiltrosTransacaoDTO;
import WalletFlow.sistemagestaofinanceira.dto.NovaTransacaoDTO;
import WalletFlow.sistemagestaofinanceira.models.Categoria;
import WalletFlow.sistemagestaofinanceira.models.Transacao;
import WalletFlow.sistemagestaofinanceira.models.Usuario;
import WalletFlow.sistemagestaofinanceira.service.CategoriaService;
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
    private final CategoriaService categoriaService;

    public TransacaoController(TransacaoService transacaoService, CategoriaService categoriaService) {
        this.transacaoService = transacaoService;
        this.categoriaService = categoriaService;
    }

    @ModelAttribute("categorias")
    public List<Categoria> adicionarCategorias(@AuthenticationPrincipal Usuario usuario) {
        return categoriaService.listarPorUsuario(usuario.getId());
    }

    @GetMapping
    public String listar(@AuthenticationPrincipal Usuario usuario,
                         @ModelAttribute("filtros") FiltrosTransacaoDTO filtros,
                         Model model) {
        List<Transacao> transacoes = transacaoService.listar(usuario.getId(), filtros);
        model.addAttribute("transacoes", transacoes);
        return "transacoes/listar";
    }

    @GetMapping("/criar")
    public String criar(@AuthenticationPrincipal Usuario usuario, Model model) {
        model.addAttribute("transacao", new NovaTransacaoDTO());
        return "transacoes/criar";
    }

    @PostMapping
    public String inserir(@Valid @ModelAttribute("transacao") NovaTransacaoDTO request,
                          BindingResult result, Model model,
                          @AuthenticationPrincipal Usuario usuario,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "transacoes/criar";
        }

        transacaoService.salvar(request, usuario);
        redirectAttributes.addFlashAttribute("sucesso", "Transação criada com sucesso!");
        return "redirect:/transacoes";
    }

    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id,
                          @AuthenticationPrincipal Usuario usuario,
                          RedirectAttributes redirectAttributes) {

        transacaoService.excluir(id, usuario.getId());
        redirectAttributes.addFlashAttribute("sucesso", "Transação excluída com sucesso!");
        return "redirect:/transacoes";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id,
                         Model model,
                         @AuthenticationPrincipal Usuario usuario) {

        Transacao t = transacaoService.buscarPorId(id, usuario.getId());
        model.addAttribute("transacao", new NovaTransacaoDTO(t));
        return "transacoes/criar";
    }

    @PutMapping
    public String atualizar(@Valid @ModelAttribute("transacao") NovaTransacaoDTO request,
                            BindingResult result,
                            Model model,
                            @AuthenticationPrincipal Usuario usuario,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "transacoes/criar";
        }

        transacaoService.editar(request, usuario.getId());
        redirectAttributes.addFlashAttribute("sucesso", "Transação atualizada com sucesso!");
        return "redirect:/transacoes";
    }
}
