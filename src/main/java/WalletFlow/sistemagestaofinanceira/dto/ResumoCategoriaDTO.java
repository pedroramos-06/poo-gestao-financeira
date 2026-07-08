package WalletFlow.sistemagestaofinanceira.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ResumoCategoriaDTO {
    private String categoria;
    private BigDecimal valor;
    private double percentual;
    private String cor;
}