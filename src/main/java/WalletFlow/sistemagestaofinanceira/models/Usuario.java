package WalletFlow.sistemagestaofinanceira.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Min(10)
    @Max(50)
    private String nome;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String senha;

    @OneToMany(mappedBy = "usuario")
    private List<Transacao> trasacoes;

    public Usuario(){}

    public Usuario(Long id, String nome, String email, String senha, List<Transacao> trasacoes) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.trasacoes = trasacoes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}

