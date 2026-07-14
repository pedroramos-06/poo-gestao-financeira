package WalletFlow.sistemagestaofinanceira.service;

import WalletFlow.sistemagestaofinanceira.dto.NovoUsuarioDTO;
import WalletFlow.sistemagestaofinanceira.enums.TipoTransacao;
import WalletFlow.sistemagestaofinanceira.exceptions.EmailJaExistenteException;
import WalletFlow.sistemagestaofinanceira.models.Categoria;
import WalletFlow.sistemagestaofinanceira.models.Usuario;
import WalletFlow.sistemagestaofinanceira.repository.CategoriaRepository;
import WalletFlow.sistemagestaofinanceira.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final HttpServletRequest request;
    private final CategoriaRepository categoriaRepository;

    public UsuarioService(UsuarioRepository repository,
                          PasswordEncoder passwordEncoder,
                          HttpServletRequest request,
                          CategoriaRepository categoriaRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.request = request;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public Usuario register(NovoUsuarioDTO dto) throws EmailJaExistenteException{
        Usuario usuario = dto.toEntity();

        if(repository.findByEmail(dto.getEmail()).isPresent()){
            throw new EmailJaExistenteException();
        }

        String senhaHash = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaHash);

        Usuario usuarioSalvo = repository.save(usuario);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                usuarioSalvo,
                null,
                usuarioSalvo.getAuthorities()
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        request.getSession().setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                context
        );

        criarCategoriasPadrao(usuario);
        return usuarioSalvo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    public boolean senhaValida(Usuario usuario, String senhaDigitada) {
        return passwordEncoder.matches(senhaDigitada, usuario.getSenha());
    }

    private void criarCategoriasPadrao(Usuario usuario) {
        List<Categoria> padroes = List.of(
                new Categoria("Salário", TipoTransacao.ENTRADA, "#198754"),
                new Categoria("Freelance", TipoTransacao.ENTRADA, "#20c997"),
                new Categoria("Alimentação", TipoTransacao.SAIDA, "#dc3545"),
                new Categoria("Transporte", TipoTransacao.SAIDA, "#fd7e14"),
                new Categoria("Aluguel", TipoTransacao.SAIDA, "#6f42c1"),
                new Categoria("Lazer", TipoTransacao.SAIDA, "#0dcaf0")
        );

        padroes.forEach(c -> c.setUsuario(usuario));
        categoriaRepository.saveAll(padroes);
    }
}
