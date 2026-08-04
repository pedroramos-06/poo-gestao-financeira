package WalletFlow.sistemagestaofinanceira.service;

import WalletFlow.sistemagestaofinanceira.dto.FiltrosTransacaoDTO;
import WalletFlow.sistemagestaofinanceira.dto.NovaTransacaoDTO;
import WalletFlow.sistemagestaofinanceira.exceptions.AcessoNegadoException;
import WalletFlow.sistemagestaofinanceira.models.Categoria;
import WalletFlow.sistemagestaofinanceira.models.Transacao;
import WalletFlow.sistemagestaofinanceira.models.Usuario;
import WalletFlow.sistemagestaofinanceira.repository.CategoriaRepository;
import WalletFlow.sistemagestaofinanceira.repository.TransacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransacaoService {
    private final TransacaoRepository transacaoRepository;
    private final CategoriaRepository categoriaRepository; // 1. Injete a CategoriaRepository aqui

    public TransacaoService(TransacaoRepository transacaoRepository, CategoriaRepository categoriaRepository) {
        this.transacaoRepository = transacaoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public void salvar(NovaTransacaoDTO dto, Usuario usuario) {
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));

        Transacao transacao = dto.toEntity(categoria);
        transacao.setUsuario(usuario);

        transacaoRepository.save(transacao);
    }

    @Transactional(readOnly = true)
    public List<Transacao> listar(Long usuarioId, FiltrosTransacaoDTO filtros) {
        return transacaoRepository.listar(
            usuarioId,
            filtros.getDataInicio(),
            filtros.getDataFim(),
            filtros.getCategoria(),
            filtros.getTipo()
        );
    }

    @Transactional(readOnly = true)
    public Transacao buscarPorId(Long id, Long usuarioId) {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transação não encontrada"));

        if (!transacao.getUsuario().getId().equals(usuarioId)) {
            throw new AcessoNegadoException();
        }

        return transacao;
    }

    @Transactional
    public void excluir(Long id, Long usuarioId) {
        buscarPorId(id, usuarioId); //validar permissão

        transacaoRepository.deleteById(id);
    }

    @Transactional
    public void editar(NovaTransacaoDTO dto, Long usuarioId) {
        Transacao transacao = buscarPorId(dto.getId(), usuarioId);

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));

        transacao.setCategoria(categoria);
        transacao.setDescricao(dto.getDescricao());
        transacao.setValor(dto.getValor());
        transacao.setData(dto.getData());

        transacaoRepository.save(transacao);
    }

}
