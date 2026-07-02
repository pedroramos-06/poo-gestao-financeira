package WalletFlow.sistemagestaofinanceira.service;

import WalletFlow.sistemagestaofinanceira.dto.NovoUsuarioDTO;
import WalletFlow.sistemagestaofinanceira.exceptions.EmailJaExistenteException;
import WalletFlow.sistemagestaofinanceira.models.Usuario;
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

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final HttpServletRequest request;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder, HttpServletRequest request) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.request = request;
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

        return usuarioSalvo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }
}
