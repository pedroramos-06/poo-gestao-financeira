package WalletFlow.sistemagestaofinanceira.service;

import WalletFlow.sistemagestaofinanceira.dto.FiltrosTransacaoDTO;
import WalletFlow.sistemagestaofinanceira.dto.NovaTransacaoDTO;
import WalletFlow.sistemagestaofinanceira.models.Transacao;
import WalletFlow.sistemagestaofinanceira.models.Usuario;
import WalletFlow.sistemagestaofinanceira.repository.TransacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransacaoService {
    private final TransacaoRepository transacaoRepository;

    public TransacaoService(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    public void salvar(NovaTransacaoDTO dto, Usuario usuario) {
        Transacao transacao = dto.toEntity();
        transacao.setUsuario(usuario);

        transacaoRepository.save(transacao);
    }

    public List<Transacao> listar(Usuario usuario, FiltrosTransacaoDTO filtros) {
        return transacaoRepository.listar(
            usuario.getId(),
            filtros.getDataInicio(),
            filtros.getDataFim(),
            filtros.getCategoria(),
            filtros.getTipo()
        );
    }

    public Transacao buscarPorId(Long id) {
        return transacaoRepository.findById(id).orElseThrow(() -> new RuntimeException("Transação não encontrada"));
    }

    public void excluir(Long id) {
        transacaoRepository.deleteById(id);
    }

    public void editar(NovaTransacaoDTO dto, Usuario usuario) {
        Transacao transacao = dto.toEntity();
        transacao.setUsuario(usuario);
        transacao.setId(dto.getId());

        transacaoRepository.save(transacao);
    }
}
