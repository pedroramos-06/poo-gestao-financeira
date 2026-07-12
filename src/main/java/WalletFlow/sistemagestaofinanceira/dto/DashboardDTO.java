package WalletFlow.sistemagestaofinanceira.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class DashboardDTO {
    private YearMonth periodo;
    private BigDecimal saldo;
    private BigDecimal totalEntradas;
    private BigDecimal totalSaidas;
    private BigDecimal meta;
    private double metaAtingida;
    private String metaCor;
    private List<ResumoCategoriaDTO> resumoGastosPorCategoria;
}
