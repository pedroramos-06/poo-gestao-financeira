package WalletFlow.sistemagestaofinanceira.service;

import WalletFlow.sistemagestaofinanceira.dto.DashboardDTO;
import WalletFlow.sistemagestaofinanceira.enums.TipoTransacao;
import WalletFlow.sistemagestaofinanceira.models.Meta;
import WalletFlow.sistemagestaofinanceira.repository.MetaRepository;
import WalletFlow.sistemagestaofinanceira.repository.TransacaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;

@Service
public class DashboardService {

    private final TransacaoRepository transacaoRepository;
    private final MetaRepository metaRepository;

    public DashboardService(TransacaoRepository transacaoRepository, MetaRepository metaRepository) {
        this.transacaoRepository = transacaoRepository;
        this.metaRepository = metaRepository;
    }

    public DashboardDTO buscarResumo(Long usuarioId, YearMonth periodo) {

        LocalDate inicio = periodo.atDay(1);
        LocalDate fim = periodo.atEndOfMonth();

        double entradas = transacaoRepository.somarPorTipo(
                usuarioId,
                TipoTransacao.ENTRADA,
                inicio,
                fim
        );
        double saidas = transacaoRepository.somarPorTipo(
                usuarioId,
                TipoTransacao.SAIDA,
                inicio,
                fim
        );
        Optional<Meta> metaOptional = metaRepository.findByData(periodo);

        double meta = 0.0;
        double metaAtingida = 0.0;

        if (metaOptional.isPresent()) {
            meta = metaOptional.get().getValor();

            if (meta > 0) {
                metaAtingida = (saidas * 100) / meta;
            }
        }

        double saldo = entradas - saidas;

        return new DashboardDTO(
                saldo,
                entradas,
                saidas,
                meta,
                metaAtingida
        );
    }
}
