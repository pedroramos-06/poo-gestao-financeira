package WalletFlow.sistemagestaofinanceira.exceptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleNotFound(EntityNotFoundException e,
                                 HttpServletRequest request,
                                 RedirectAttributes redirectAttributes) {
        log.warn("Entidade não encontrada [{}]: {}", request.getRequestURI(), e.getMessage());
        redirectAttributes.addFlashAttribute("erro", "Registro não encontrado");
        return redirectBackTo(request);
    }

    @ExceptionHandler(AcessoNegadoException.class)
    public String handleAcessoNegado(AcessoNegadoException e,
                                     HttpServletRequest request,
                                     RedirectAttributes redirectAttributes) {
        log.warn("Acesso negado [{}]: {}", request.getRequestURI(), e.getMessage());
        redirectAttributes.addFlashAttribute("erro", "Você não tem permissão para essa ação");
        return redirectBackTo(request);
    }

    @ExceptionHandler(CategoriaProtegidaException.class)
    public String handleCategoriaEmUso(CategoriaProtegidaException e,
                                     HttpServletRequest request,
                                     RedirectAttributes redirectAttributes) {
        log.warn("Categoria protegida[{}]: {}", request.getRequestURI(), e.getMessage());
        redirectAttributes.addFlashAttribute("erro", e.getMessage());
        return redirectBackTo(request);
    }

    @ExceptionHandler(EditarTipoCategoriaException.class)
    public String handleEditarTipoCategoria( EditarTipoCategoriaException e,
                                       HttpServletRequest request,
                                       RedirectAttributes redirectAttributes) {
        log.warn("Tentativa de editar tipo de categoria[{}]: {}", request.getRequestURI(), e.getMessage());
        redirectAttributes.addFlashAttribute("erro", e.getMessage());
        return redirectBackTo(request);
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception e,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {
        log.error("Erro inesperado [{}]", request.getRequestURI(), e);
        redirectAttributes.addFlashAttribute("erro", "Um erro inesperado ocorreu, tente novamente!");
        return redirectBackTo(request);
    }

    private String redirectBackTo(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri.startsWith("/transacoes")) {
            return "redirect:/transacoes";
        }
        if (uri.startsWith("/metas")) {
            return "redirect:/metas";
        }
        if (uri.startsWith("/configuracoes")) {
            return "redirect:/configuracoes";
        }
        if (uri.startsWith("/categorias")) {
            return "redirect:/categorias";
        }
        return "redirect:/dashboard";
    }

}