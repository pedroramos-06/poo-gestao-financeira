package WalletFlow.sistemagestaofinanceira.service;

import WalletFlow.sistemagestaofinanceira.dto.ConfiguracoesDTO;
import WalletFlow.sistemagestaofinanceira.models.Usuario;
import WalletFlow.sistemagestaofinanceira.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class ConfiguracoesService {
    private final UsuarioRepository usuarioRepository;

    public ConfiguracoesService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void editar(ConfiguracoesDTO dto, Usuario usuario) {
        usuario.setMetaPadrao(dto.getMetaPadrao());
        usuario.setTema(dto.getTema());

        usuarioRepository.save(usuario);
    }
}
