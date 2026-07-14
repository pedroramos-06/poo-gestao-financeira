package WalletFlow.sistemagestaofinanceira.models;

import WalletFlow.sistemagestaofinanceira.enums.TipoTransacao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "nome"}))
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private TipoTransacao tipo;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cor;

    public Categoria(String nome, TipoTransacao tipo, String cor) {
        this.nome = nome;
        this.tipo = tipo;
        this.cor = cor;
    }
}
