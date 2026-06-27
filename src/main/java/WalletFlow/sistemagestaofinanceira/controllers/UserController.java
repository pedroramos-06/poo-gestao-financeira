package WalletFlow.sistemagestaofinanceira.controllers;

import WalletFlow.sistemagestaofinanceira.dto.NovoUsuarioDTO;
import WalletFlow.sistemagestaofinanceira.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/user")
public class UserController {

    private final UsuarioService usuarioService;

    public UserController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String formLogin() {
        return "user/login";
    }

    @GetMapping("/register")
    public String formRegister(Model model) {
        model.addAttribute("usuario", new NovoUsuarioDTO());
        return "user/register";
    }

    @PostMapping("/register")
    public String register (@Valid @ModelAttribute("usuario")NovoUsuarioDTO DTO, BindingResult result, Model model) {
        if(result.hasErrors()){
            return "user/register";
        }

        try {
            usuarioService.register(DTO);
        } catch (DataIntegrityViolationException e) {
            result.rejectValue("email", "error.usuario", "E-mail já cadastrado");
            return "user/register";
        }

        return "/home"; //retornar a pagina inicial outro endpoint (talvez listar transações)
    }
}
