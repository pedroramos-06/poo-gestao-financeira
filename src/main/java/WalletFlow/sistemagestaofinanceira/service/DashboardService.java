package WalletFlow.sistemagestaofinanceira.service;

import WalletFlow.sistemagestaofinanceira.dto.DashboardDTO;
import WalletFlow.sistemagestaofinanceira.dto.ResumoCategoriaDTO;
import WalletFlow.sistemagestaofinanceira.enums.Categoria;
import WalletFlow.sistemagestaofinanceira.enums.TipoTransacao;
import WalletFlow.sistemagestaofinanceira.models.Meta;
import WalletFlow.sistemagestaofinanceira.repository.MetaRepository;
import WalletFlow.sistemagestaofinanceira.repository.TransacaoRepository;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

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
        List<ResumoCategoriaDTO> resumoGastosPorCategoria = getResumoGastosPorCategoria(usuarioId, periodo);

        return new DashboardDTO(
                periodo,
                saldo,
                entradas,
                saidas,
                meta,
                metaAtingida,
                resumoGastosPorCategoria
        );
    }

    public List<ResumoCategoriaDTO> getResumoGastosPorCategoria(Long usuarioId, YearMonth periodo){
        List<ResumoCategoriaDTO> resultado = new ArrayList<>();
        double totalSaidas = getSaidas(usuarioId, periodo);

        for (Categoria categoria : Categoria.values()) {
            double valorCategoria = transacaoRepository.somarPorTipo(
                    usuarioId,
                    categoria,
                    TipoTransacao.SAIDA,
                    periodo.atDay(1),
                    periodo.atEndOfMonth()
            );

            double percentual = 0.0;

            if (totalSaidas > 0 && valorCategoria > 0) {
                percentual = (valorCategoria / totalSaidas) * 100;
            }

            resultado.add(new ResumoCategoriaDTO(
                    categoria.getDescricao(),
                    valorCategoria,
                    percentual,
                    categoria.getCorHex()
            ));
        }
        return resultado;
    }

    private double getEntradas(Long usuarioId, YearMonth periodo) {
        return transacaoRepository.somarPorTipo(
                usuarioId,
                null,
                TipoTransacao.ENTRADA,
                periodo.atDay(1),
                periodo.atEndOfMonth()
        );
    }

    private double getSaidas(Long usuarioId, YearMonth periodo) {
        return transacaoRepository.somarPorTipo(
                usuarioId,
                null,
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
