package WalletFlow.sistemagestaofinanceira.service;

import WalletFlow.sistemagestaofinanceira.dto.DashboardDTO;
import WalletFlow.sistemagestaofinanceira.dto.ResumoCategoriaDTO;
import WalletFlow.sistemagestaofinanceira.enums.Categoria;
import WalletFlow.sistemagestaofinanceira.enums.TipoTransacao;
import WalletFlow.sistemagestaofinanceira.models.Meta;
import WalletFlow.sistemagestaofinanceira.repository.MetaRepository;
import WalletFlow.sistemagestaofinanceira.repository.TransacaoRepository;
import WalletFlow.sistemagestaofinanceira.repository.UsuarioRepository;
import WalletFlow.sistemagestaofinanceira.utils.MetaCorHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    private final TransacaoRepository transacaoRepository;
    private final MetaRepository metaRepository;
    private final CategoriaService categoriaService;
    private final UsuarioRepository usuarioRepository;

    public DashboardService(TransacaoRepository transacaoRepository, MetaRepository metaRepository, CategoriaService categoriaService, UsuarioRepository usuarioRepository) {
        this.transacaoRepository = transacaoRepository;
        this.metaRepository = metaRepository;
        this.categoriaService = categoriaService;
        this.usuarioRepository = usuarioRepository;
    }

    public DashboardDTO getResumo(Long usuarioId, YearMonth periodo) {
        BigDecimal entradas = getEntradas(usuarioId, periodo);
        BigDecimal saidas = getSaidas(usuarioId, periodo);
        BigDecimal meta = getMeta(usuarioId, periodo);
        double metaAtingida = calcularMetaAtingida(saidas, meta);
        BigDecimal saldo = transacaoRepository.getSaldo(usuarioId);
        List<ResumoCategoriaDTO> resumoGastosPorCategoria = getResumoGastosPorCategoria(usuarioId, periodo);

        return new DashboardDTO(
                periodo,
                saldo,
                entradas,
                saidas,
                meta,
                metaAtingida,
                MetaCorHelper.getCor(metaAtingida),
                resumoGastosPorCategoria
        );
    }

    public List<ResumoCategoriaDTO> getResumoGastosPorCategoria(Long usuarioId, YearMonth periodo){
        List<ResumoCategoriaDTO> resultado = new ArrayList<>();
        BigDecimal totalSaidas = getSaidas(usuarioId, periodo);
        List<Categoria> categorias = categoriaService.listarPorUsuario(usuarioId);

        for (Categoria categoria : categorias) {
            BigDecimal valorCategoria = transacaoRepository.somarPorTipo(
                    usuarioId,
                    categoria,
                    TipoTransacao.SAIDA,
                    periodo.atDay(1),
                    periodo.atEndOfMonth()
            );

            double percentual = 0.0;

            if (totalSaidas.compareTo(BigDecimal.ZERO) > 0 && valorCategoria.compareTo(BigDecimal.ZERO) > 0) {
                percentual = valorCategoria
                        .multiply(BigDecimal.valueOf(100))
                        .divide(totalSaidas, 4, RoundingMode.HALF_UP)
                        .doubleValue();
            }

            resultado.add(new ResumoCategoriaDTO(
                    categoria.getNome(),
                    valorCategoria,
                    percentual,
                    categoria.getCor()
            ));
        }
        return resultado;
    }

    private BigDecimal getEntradas(Long usuarioId, YearMonth periodo) {
        return transacaoRepository.somarPorTipo(
                usuarioId,
                null,
                TipoTransacao.ENTRADA,
                periodo.atDay(1),
                periodo.atEndOfMonth()
        );
    }

    private BigDecimal getSaidas(Long usuarioId, YearMonth periodo) {
        return transacaoRepository.somarPorTipo(
                usuarioId,
                null,
                TipoTransacao.SAIDA,
                periodo.atDay(1),
                periodo.atEndOfMonth()
        );
    }

    private BigDecimal getMeta(Long usuarioId, YearMonth periodo) {
        return metaRepository.findByUsuarioIdAndData(usuarioId, periodo)
                .map(Meta::getValor)
                .orElseGet(() -> usuarioRepository.getMetaPadraoById(usuarioId));
    }

    private double calcularMetaAtingida(BigDecimal saidas, BigDecimal meta) {
        if (meta.compareTo(BigDecimal.ZERO) <= 0) {
            return 0.0;
        }
        return saidas
                .multiply(BigDecimal.valueOf(100))
                .divide(meta, 4, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
