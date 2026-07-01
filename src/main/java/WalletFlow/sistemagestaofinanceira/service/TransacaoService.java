package WalletFlow.sistemagestaofinanceira.service;

import WalletFlow.sistemagestaofinanceira.dto.FiltrosTransacaoDTO;
import WalletFlow.sistemagestaofinanceira.dto.NovaTransacaoDTO;
import WalletFlow.sistemagestaofinanceira.exceptions.AcessoNegadoException;
import WalletFlow.sistemagestaofinanceira.models.Transacao;
import WalletFlow.sistemagestaofinanceira.models.Usuario;
import WalletFlow.sistemagestaofinanceira.repository.TransacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransacaoService {
    private final TransacaoRepository transacaoRepository;

    public TransacaoService(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }
    @Transactional
    public void salvar(NovaTransacaoDTO dto, Usuario usuario) {
        Transacao transacao = dto.toEntity();
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
    public Transacao buscarPorId(Long id, Long usuarioId) throws AcessoNegadoException {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transação não encontrada"));

        if (!transacao.getUsuario().getId().equals(usuarioId)) {
            throw new AcessoNegadoException();
        }

        return transacao;
    }

    @Transactional
    public void excluir(Long id, Long usuarioId) throws AcessoNegadoException{
        buscarPorId(id, usuarioId); //validar permissão

        transacaoRepository.deleteById(id);
    }

    @Transactional
    public void editar(NovaTransacaoDTO dto, Long usuarioId) throws AcessoNegadoException {
        Transacao transacao = buscarPorId(dto.getId(), usuarioId);

        transacao.setCategoria(dto.getCategoria());
        transacao.setTipo(dto.getTipo());
        transacao.setDescricao(dto.getDescricao());
        transacao.setValor(dto.getValor());
        transacao.setData(dto.getData());

        transacaoRepository.save(transacao);
    }

}
