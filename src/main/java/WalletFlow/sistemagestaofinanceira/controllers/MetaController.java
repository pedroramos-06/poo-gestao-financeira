package WalletFlow.sistemagestaofinanceira.controllers;

import WalletFlow.sistemagestaofinanceira.dto.NovaMetaDTO;
import WalletFlow.sistemagestaofinanceira.models.Meta;
import WalletFlow.sistemagestaofinanceira.models.Usuario;
import WalletFlow.sistemagestaofinanceira.service.MetaService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
        List<Meta> metas = metaService.listarPorUsuario(usuario);
        model.addAttribute("metas", metas);

        return "meta/listar";
    }

    @GetMapping("/criar")
    public String criar(Model model){
        model.addAttribute("meta", new NovaMetaDTO());
        return "meta/criar";
    }

    @PostMapping
    public String inserir(@Valid @ModelAttribute("meta") NovaMetaDTO request, BindingResult result, @AuthenticationPrincipal Usuario usuario){
        if(result.hasErrors()) {
            return "meta/criar";
        }

        try{
            metaService.salvar(request, usuario);
        } catch (DataIntegrityViolationException e){
            result.rejectValue("data","error.meta", "Já existe uma meta cadastrada para esse email" );
        } catch (Exception e){
            result.rejectValue(null,"error.meta", "Um erro inesperado ocorreu, tente novamente!" );
        }

        return "redirect:/metas";
    }

    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id) {
        metaService.excluir(id);

        return "redirect:/metas";
    }
}
