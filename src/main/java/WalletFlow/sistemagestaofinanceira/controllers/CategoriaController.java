package WalletFlow.sistemagestaofinanceira.controllers;

import WalletFlow.sistemagestaofinanceira.dto.NovaCategoriaDTO;
import WalletFlow.sistemagestaofinanceira.exceptions.CategoriaJaExisteException;
import WalletFlow.sistemagestaofinanceira.exceptions.MetaDuplicadaException;
import WalletFlow.sistemagestaofinanceira.models.Categoria;
import WalletFlow.sistemagestaofinanceira.models.Usuario;
import WalletFlow.sistemagestaofinanceira.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listar(@AuthenticationPrincipal Usuario usuario, Model model) {
        List<Categoria> categorias = categoriaService.listarPorUsuario(usuario.getId());
        model.addAttribute("categorias", categorias);
        return "categoria/listar";
    }

    @GetMapping("/criar")
    public String criar(Model model) {
        model.addAttribute("categoria", new NovaCategoriaDTO());
        return "categoria/criar";
    }

    @PostMapping
    public String inserir(@Valid @ModelAttribute("categoria") NovaCategoriaDTO request,
                          BindingResult result,
                          @AuthenticationPrincipal Usuario usuario,
                          RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            return "categoria/criar";
        }

        try {
            categoriaService.salvar(request, usuario);
            redirectAttributes.addFlashAttribute("sucesso", "Categoria criada com sucesso!");
            return "redirect:/categorias";
        } catch (CategoriaJaExisteException e) {
            result.rejectValue("nome", "error.categoria", e.getMessage());
            return "categoria/criar";
        }
    }

    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id,
                          @AuthenticationPrincipal Usuario usuario,
                          RedirectAttributes redirectAttributes) {

        categoriaService.excluir(id, usuario.getId());
        redirectAttributes.addFlashAttribute("sucesso", "Categoria excluída com sucesso!");
        return "redirect:/categorias";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id,
                         Model model,
                         @AuthenticationPrincipal Usuario usuario) {
        Categoria c = categoriaService.buscarPorId(id, usuario.getId());
        model.addAttribute("categoria", new NovaCategoriaDTO(c));
        return "categoria/criar";
    }

    @PutMapping
    public String atualizar(@Valid @ModelAttribute("categoria") NovaCategoriaDTO request,
                            BindingResult result,
                            @AuthenticationPrincipal Usuario usuario,
                            RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            return "categoria/criar";
        }

        try {
            categoriaService.editar(request, usuario.getId());
            redirectAttributes.addFlashAttribute("sucesso", "Categoria atualizada com sucesso!");
            return "redirect:/categorias";
        } catch (CategoriaJaExisteException e) {
            result.rejectValue("nome", "error.categoria", e.getMessage());
            return "categoria/criar";
        }
    }
}