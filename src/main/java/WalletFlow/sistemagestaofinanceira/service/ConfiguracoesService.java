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
    private final CategoriaService categoriaService;

    public ConfiguracoesService(UsuarioRepository usuarioRepository, MetaRepository metaRepository, TransacaoRepository transacaoRepository, CategoriaService categoriaService) {
        this.usuarioRepository = usuarioRepository;
        this.metaRepository = metaRepository;
        this.transacaoRepository = transacaoRepository;
        this.categoriaService = categoriaService;
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
        categoriaService.resetar(usuarioId);
    }
}
