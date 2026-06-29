package WalletFlow.sistemagestaofinanceira.service;

import WalletFlow.sistemagestaofinanceira.dto.DashboardDTO;
import WalletFlow.sistemagestaofinanceira.enums.TipoTransacao;
import WalletFlow.sistemagestaofinanceira.models.Meta;
import WalletFlow.sistemagestaofinanceira.repository.MetaRepository;
import WalletFlow.sistemagestaofinanceira.repository.TransacaoRepository;
import org.springframework.stereotype.Service;

import java.time.YearMonth;

@Service
public class DashboardService {

    private final TransacaoRepository transacaoRepository;
    private final MetaRepository metaRepository;

    public DashboardService(TransacaoRepository transacaoRepository, MetaRepository metaRepository) {
        this.transacaoRepository = transacaoRepository;
        this.metaRepository = metaRepository;
    }

    public DashboardDTO getResumo(Long usuarioId, YearMonth periodo) {
        double entradas = getEntradas(usuarioId, periodo);
        double saidas = getSaidas(usuarioId, periodo);
        double meta = getMeta(usuarioId, periodo);
        double metaAtingida = calcularMetaAtingida(saidas, meta);
        double saldo = transacaoRepository.getSaldo(usuarioId);

        return new DashboardDTO(
                periodo,
                saldo,
                entradas,
                saidas,
                meta,
                metaAtingida
        );
    }

    private double getEntradas(Long usuarioId, YearMonth periodo) {
        return transacaoRepository.somarPorTipo(
                usuarioId,
                TipoTransacao.ENTRADA,
                periodo.atDay(1),
                periodo.atEndOfMonth()
        );
    }

    private double getSaidas(Long usuarioId, YearMonth periodo) {
        return transacaoRepository.somarPorTipo(
                usuarioId,
                TipoTransacao.SAIDA,
                periodo.atDay(1),
                periodo.atEndOfMonth()
        );
    }

    private double getMeta(Long usuarioId, YearMonth periodo) {
        return metaRepository.findByUsuarioIdAndData(usuarioId, periodo)
                .map(Meta::getValor)
                .orElse(0.0);
    }

    private double calcularMetaAtingida(double saidas, double meta) {
        if (meta <= 0) {
            return 0.0;
        }
        return (saidas * 100) / meta;
    }
}
