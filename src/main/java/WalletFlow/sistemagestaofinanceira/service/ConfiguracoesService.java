package WalletFlow.sistemagestaofinanceira.service;

import WalletFlow.sistemagestaofinanceira.dto.ConfiguracoesDTO;
import WalletFlow.sistemagestaofinanceira.models.Usuario;
import WalletFlow.sistemagestaofinanceira.repository.MetaRepository;
import WalletFlow.sistemagestaofinanceira.repository.TransacaoRepository;
import WalletFlow.sistemagestaofinanceira.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfiguracoesService {
    private final UsuarioRepository usuarioRepository;
    private final MetaRepository metaRepository;
    private final TransacaoRepository transacaoRepository;

    public ConfiguracoesService(UsuarioRepository usuarioRepository, MetaRepository metaRepository, TransacaoRepository transacaoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.metaRepository = metaRepository;
        this.transacaoRepository = transacaoRepository;
    }

    public void editar(ConfiguracoesDTO dto, Usuario usuario) {
        usuario.setMetaPadrao(dto.getMetaPadrao());
        usuario.setTema(dto.getTema());

        usuarioRepository.save(usuario);
    }

    public void alterarNome(Usuario usuario, String nome) {
        usuario.setNome(nome);

        usuarioRepository.save(usuario);
    }

    @Transactional
    public void excluirDados(Long usuarioId) {
        metaRepository.deleteByUsuarioId(usuarioId);
        transacaoRepository.deleteByUsuarioId(usuarioId);
    }

    public String validarNome(String nome) {
        if (nome == null || nome.isBlank() || nome.length() < 5 || nome.length() > 50) {
            return "O nome deve ter entre 5 e 50 caracteres";
        }
        return null;
    }
}
