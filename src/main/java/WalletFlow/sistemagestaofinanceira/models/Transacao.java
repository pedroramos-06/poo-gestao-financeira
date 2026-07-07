package WalletFlow.sistemagestaofinanceira.models;

import WalletFlow.sistemagestaofinanceira.enums.Categoria;
import WalletFlow.sistemagestaofinanceira.enums.TipoTransacao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private String descricao;

    @Column(precision = 10, scale = 2) //Valor máximo: 99.999.999,99
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    private TipoTransacao tipo;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    private LocalDate data;

    public Transacao(String descricao, BigDecimal valor, TipoTransacao tipo, Categoria categoria, LocalDate data) {
        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
        this.categoria = categoria;
        this.data = data;
    }
}