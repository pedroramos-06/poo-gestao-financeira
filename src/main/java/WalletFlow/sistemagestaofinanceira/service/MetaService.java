package WalletFlow.sistemagestaofinanceira.service;

import WalletFlow.sistemagestaofinanceira.dto.NovaMetaDTO;
import WalletFlow.sistemagestaofinanceira.models.Meta;
import WalletFlow.sistemagestaofinanceira.models.Usuario;
import WalletFlow.sistemagestaofinanceira.repository.MetaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetaService {
    private final MetaRepository metaRepository;

    public MetaService(MetaRepository metaRepository) {
        this.metaRepository = metaRepository;
    }

    public void salvar(NovaMetaDTO dto, Usuario usuario) {
        Meta meta = dto.toEntity();
        meta.setUsuario(usuario);

        metaRepository.save(meta);
    }

    public List<Meta> listarPorUsuario(Usuario usuario) {
        return metaRepository.findByUsuarioId(usuario.getId());
    }

    public void excluir(Long id) {
        metaRepository.deleteById(id);
    }
}
