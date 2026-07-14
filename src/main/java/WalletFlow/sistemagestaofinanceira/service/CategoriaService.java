package WalletFlow.sistemagestaofinanceira.service;

import WalletFlow.sistemagestaofinanceira.dto.NovaCategoriaDTO;

import WalletFlow.sistemagestaofinanceira.exceptions.AcessoNegadoException;
import WalletFlow.sistemagestaofinanceira.exceptions.CategoriaEmUsoException;
import WalletFlow.sistemagestaofinanceira.exceptions.CategoriaJaExisteException;
import WalletFlow.sistemagestaofinanceira.models.Categoria;
import WalletFlow.sistemagestaofinanceira.models.Usuario;
import WalletFlow.sistemagestaofinanceira.repository.CategoriaRepository;
import WalletFlow.sistemagestaofinanceira.repository.TransacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final TransacaoRepository transacaoRepository;

    public CategoriaService(CategoriaRepository categoriaRepository, TransacaoRepository transacaoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.transacaoRepository = transacaoRepository;
    }

    @Transactional
    public void salvar(NovaCategoriaDTO dto, Usuario usuario) throws CategoriaJaExisteException {
        Categoria categoria = dto.toEntity();
        categoria.setUsuario(usuario);

        if(categoriaRepository.findByUsuarioIdAndNome(usuario.getId(), dto.getNome()).isPresent()){
            throw new CategoriaJaExisteException();
        }
        categoriaRepository.save(categoria);
    }

    @Transactional(readOnly = true)
    public Categoria buscarPorId(Long id, Long usuarioId) {
        Categoria categoria = categoriaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));

        if(!categoria.getUsuario().getId().equals(usuarioId)){
            throw new AcessoNegadoException();
        }

        return categoria;
    }

    @Transactional(readOnly = true)
    public List<Categoria> listarPorUsuario(Long usuarioId) {
        return categoriaRepository.findByUsuarioId(usuarioId);
    }

    @Transactional
    public void excluir(Long id, Long usuarioId) {
        buscarPorId(id, usuarioId); //validar permissão

        if(!transacaoRepository.findByCategoriaId(id).isEmpty()){
            throw new CategoriaEmUsoException();
        }

        categoriaRepository.deleteById(id);
    }

    @Transactional
    public void editar(NovaCategoriaDTO dto, Long usuarioId) throws CategoriaJaExisteException {
        Categoria categoria = buscarPorId(dto.getId(), usuarioId); //validar permissão

        if(!dto.getNome().equals(categoria.getNome())) {
            if(categoriaRepository.findByUsuarioIdAndNome(usuarioId, dto.getNome()).isPresent()) {
                throw new CategoriaJaExisteException();
            }
        }

        categoria.setNome(dto.getNome());
        categoria.setTipo(dto.getTipo());
        categoria.setCor(dto.getCor());

        categoriaRepository.save(categoria);
    }
}
