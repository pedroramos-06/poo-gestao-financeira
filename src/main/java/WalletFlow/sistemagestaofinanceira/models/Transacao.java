package WalletFlow.sistemagestaofinanceira.models;

import WalletFlow.sistemagestaofinanceira.Enums.Categoria;
import WalletFlow.sistemagestaofinanceira.Enums.TipoTransacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
public class Transacao {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @NotBlank
    @Max(75)
    private String descricao;

    @NotBlank
    @Positive
    private double valor;

    @NotBlank
    private TipoTransacao tipo;

    @NotBlank
    private Categoria categoria;

    @DateTimeFormat
    private LocalDate data;

    public Transacao(){}

    public Transacao(Long id, Usuario usuario, String descricao, double valor, TipoTransacao tipo, Categoria categoria, LocalDate data) {
        this.id = id;
        this.usuario = usuario;
        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
        this.categoria = categoria;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoTransacao tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setId(Long id) {
        this.id = id;
    }
}