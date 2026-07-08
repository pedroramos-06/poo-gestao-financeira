package WalletFlow.sistemagestaofinanceira.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.YearMonth;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "data"}))
public class Meta {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false, precision = 10, scale = 2) //Valor máximo: 99.999.999,99
    private BigDecimal valor;

    @Column(nullable = false)
    private YearMonth data;

    public Meta(BigDecimal valor, YearMonth data) {
        this.valor = valor;
        this.data = data;
    }
}