package WalletFlow.sistemagestaofinanceira.controllers;

import WalletFlow.sistemagestaofinanceira.dto.NovaMetaDTO;
import WalletFlow.sistemagestaofinanceira.exceptions.AcessoNegadoException;
import WalletFlow.sistemagestaofinanceira.exceptions.MetaDuplicadaException;
import WalletFlow.sistemagestaofinanceira.models.Meta;
import WalletFlow.sistemagestaofinanceira.models.Usuario;
import WalletFlow.sistemagestaofinanceira.service.MetaService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/metas")
public class MetaController {

    private final MetaService metaService;

    public MetaController(MetaService metaService) {
        this.metaService = metaService;
    }

    @GetMapping
    public String listar(@AuthenticationPrincipal Usuario usuario, Model model) {
        try {
            List<Meta> metas = metaService.listarPorUsuario(usuario);
            model.addAttribute("metas", metas);
            return "meta/listar";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao listar metas");
            return "meta/listar";
        }
    }

    @GetMapping("/criar")
    public String criar(Model model) {
        model.addAttribute("meta", new NovaMetaDTO());
        return "meta/criar";
    }

    @PostMapping
    public String inserir(@Valid @ModelAttribute("meta") NovaMetaDTO request,
                          BindingResult result,
                          @AuthenticationPrincipal Usuario usuario,
                          RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            return "meta/criar";
        }

        try {
            metaService.salvar(request, usuario);
            redirectAttributes.addFlashAttribute("sucesso", "Meta criada com sucesso!");
            return "redirect:/metas";

        } catch (MetaDuplicadaException e) {
            result.rejectValue("data", "error.meta", "Já foi registrada uma meta para esse mês");
            return "meta/criar";

        } catch (Exception e) {
            result.reject("error.meta", "Um erro inesperado ocorreu, tente novamente!");
            return "meta/criar";
        }
    }

    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id,
                          @AuthenticationPrincipal Usuario usuario,
                          RedirectAttributes redirectAttributes) {
        try {
            metaService.excluir(id, usuario.getId());
            redirectAttributes.addFlashAttribute("sucesso", "Meta excluída com sucesso!");
            return "redirect:/metas";

        } catch (AcessoNegadoException e) {
            redirectAttributes.addFlashAttribute("erro", "Você não tem permissão para deletar esta meta");
            return "redirect:/metas";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Um erro inesperado ocorreu, tente novamente!");
            return "redirect:/metas";
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id,
                         Model model,
                         @AuthenticationPrincipal Usuario usuario,
                         RedirectAttributes redirectAttributes) {
        try {
            Meta m = metaService.buscarPorId(id, usuario.getId());
            model.addAttribute("meta", new NovaMetaDTO(m));
            return "meta/criar";

        } catch (AcessoNegadoException e) {
            redirectAttributes.addFlashAttribute("erro", "Você não tem permissão para editar essa meta");
            return "redirect:/metas";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Meta não encontrada");
            return "redirect:/metas";
        }
    }

    @PutMapping
    public String atualizar(@Valid @ModelAttribute("meta") NovaMetaDTO request,
                            BindingResult result,
                            @AuthenticationPrincipal Usuario usuario,
                            RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            return "meta/criar";
        }

        try {
            metaService.editar(request, usuario);
            redirectAttributes.addFlashAttribute("sucesso", "Meta atualizada com sucesso!");
            return "redirect:/metas";

        } catch (AcessoNegadoException e) {
            redirectAttributes.addFlashAttribute("erro", "Você não tem permissão para editar essa meta");
            return "redirect:/metas";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Um erro inesperado ocorreu, tente novamente!");
            return "redirect:/metas";
        }
    }
}