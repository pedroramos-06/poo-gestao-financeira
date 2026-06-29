package WalletFlow.sistemagestaofinanceira.service;

import WalletFlow.sistemagestaofinanceira.dto.FiltrosTransacaoDTO;
import WalletFlow.sistemagestaofinanceira.dto.NovaTransacaoDTO;
import WalletFlow.sistemagestaofinanceira.enums.TipoTransacao;
import WalletFlow.sistemagestaofinanceira.exceptions.AcessoNegadoException;
import WalletFlow.sistemagestaofinanceira.exceptions.SaldoInsuficienteException;
import WalletFlow.sistemagestaofinanceira.models.Transacao;
import WalletFlow.sistemagestaofinanceira.models.Usuario;
import WalletFlow.sistemagestaofinanceira.repository.TransacaoRepository;
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
    public void salvar(NovaTransacaoDTO dto, Usuario usuario) throws SaldoInsuficienteException {
        Transacao transacao = dto.toEntity();
        transacao.setUsuario(usuario);
        validarTransacao(transacao);
        transacaoRepository.save(transacao);
    }

    @Transactional(readOnly = true)
    public List<Transacao> listar(Usuario usuario, FiltrosTransacaoDTO filtros) {
        return transacaoRepository.listar(
            usuario.getId(),
            filtros.getDataInicio(),
            filtros.getDataFim(),
            filtros.getCategoria(),
            filtros.getTipo()
        );
    }

    @Transactional(readOnly = true)
    public Transacao buscarPorId(Long id, Long usuarioId) throws AcessoNegadoException {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transação não encontrada"));

        if (!transacao.getUsuario().getId().equals(usuarioId)) {
            throw new AcessoNegadoException();
        }

        return transacao;
    }

    @Transactional
    public void excluir(Long id, Long usuarioId) throws AcessoNegadoException{
        Transacao transacao = buscarPorId(id, usuarioId);

        transacaoRepository.deleteById(id);
    }

    @Transactional
    public void editar(NovaTransacaoDTO dto, Usuario usuario) throws AcessoNegadoException, SaldoInsuficienteException {
        Transacao antiga = buscarPorId(dto.getId(), usuario.getId());
        Transacao transacao = dto.toEntity();
        transacao.setUsuario(usuario);
        transacao.setId(dto.getId());
        validarTransacao(transacao, antiga.getValor(), antiga.getTipo());

        transacaoRepository.save(transacao);
    }

    private void validarTransacao(Transacao t) throws SaldoInsuficienteException{
        validarTransacao(t, 0.0, null);
    }

    private void validarTransacao(Transacao t, double valorAntigo, TipoTransacao tipoAntigo) throws SaldoInsuficienteException {
        if (t.getTipo() == TipoTransacao.SAIDA) {
            double saldoAtual = transacaoRepository.getSaldo(t.getUsuario().getId());
            double saldoDisponivel = saldoAtual;

            if (t.getId() != null) {
                if (tipoAntigo == TipoTransacao.SAIDA) {
                    saldoDisponivel += valorAntigo;
                } else if (tipoAntigo == TipoTransacao.ENTRADA) {
                    saldoDisponivel -= valorAntigo;
                }
            }

            if (t.getValor() > saldoDisponivel) {
                throw new SaldoInsuficienteException();
            }
        }
    }
}
