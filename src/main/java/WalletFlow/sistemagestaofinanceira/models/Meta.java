package WalletFlow.sistemagestaofinanceira.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    private YearMonth data;

    public Meta(double valor, YearMonth data) {
        this.valor = valor;
        this.data = data;
    }
}