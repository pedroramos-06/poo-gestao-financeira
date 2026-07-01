package WalletFlow.sistemagestaofinanceira.service;

import WalletFlow.sistemagestaofinanceira.dto.NovaMetaDTO;
import WalletFlow.sistemagestaofinanceira.exceptions.AcessoNegadoException;
import WalletFlow.sistemagestaofinanceira.exceptions.MetaDuplicadaException;
import WalletFlow.sistemagestaofinanceira.models.Meta;
import WalletFlow.sistemagestaofinanceira.models.Usuario;
import WalletFlow.sistemagestaofinanceira.repository.MetaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MetaService {
    private final MetaRepository metaRepository;

    public MetaService(MetaRepository metaRepository) {
        this.metaRepository = metaRepository;
    }

    @Transactional
    public void salvar(NovaMetaDTO dto, Usuario usuario) throws MetaDuplicadaException {
        Meta meta = dto.toEntity();
        meta.setUsuario(usuario);

        if(metaRepository.findByUsuarioIdAndData(usuario.getId(), dto.getData()).isPresent()){
            throw new MetaDuplicadaException();
        }
        metaRepository.save(meta);
    }

    @Transactional(readOnly = true)
    public Meta buscarPorId(Long id, Long usuarioId) throws AcessoNegadoException {
        Meta meta = metaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Meta não encontrada"));

        if(!meta.getUsuario().getId().equals(usuarioId)){
            throw  new AcessoNegadoException();
        }

        return meta;
    }

    @Transactional(readOnly = true)
    public List<Meta> listarPorUsuario(Long usuarioId) {
        return metaRepository.findByUsuarioId(usuarioId);
    }

    @Transactional
    public void excluir(Long id, Long usuarioId) throws AcessoNegadoException {
        buscarPorId(id, usuarioId); //validar permissão

        metaRepository.deleteById(id);
    }

    @Transactional
    public void editar(NovaMetaDTO dto, Long usuarioId) throws AcessoNegadoException{
        Meta meta = buscarPorId(dto.getId(), usuarioId);

        meta.setData(dto.getData());
        meta.setValor(dto.getValor());

        metaRepository.save(meta);
    }
}