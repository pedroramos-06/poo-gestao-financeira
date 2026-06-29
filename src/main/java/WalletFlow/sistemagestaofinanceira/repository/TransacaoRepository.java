package WalletFlow.sistemagestaofinanceira.repository;

import WalletFlow.sistemagestaofinanceira.enums.Categoria;
import WalletFlow.sistemagestaofinanceira.enums.TipoTransacao;
import WalletFlow.sistemagestaofinanceira.models.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    @Query("""
        SELECT t FROM Transacao t WHERE t.usuario.id = :usuarioId
        AND (:dataInicio IS NULL OR t.data >= :dataInicio)
        AND (:dataFim IS NULL OR t.data <= :dataFim)
        AND (:categoria IS NULL OR t.categoria = :categoria)
        AND (:tipo IS NULL OR t.tipo = :tipo)
        ORDER BY data DESC
    """)
    List<Transacao> listar(
        @Param("usuarioId") Long usuarioId,
        @Param("dataInicio") LocalDate dataInicio,
        @Param("dataFim") LocalDate dataFim,
        @Param("categoria") Categoria categoria,
        @Param("tipo") TipoTransacao tipo
    );

    @Query("""
        SELECT COALESCE(SUM(t.valor), 0)
        FROM Transacao t
        WHERE t.usuario.id = :usuarioId
          AND t.tipo = :tipo
          AND t.data BETWEEN :inicio AND :fim
    """)
    double somarPorTipo(
            @Param("usuarioId") Long usuarioId,
            @Param("tipo") TipoTransacao tipo,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );

    @Query("""
        SELECT COALESCE(SUM(CASE WHEN t.tipo = 'ENTRADA' THEN t.valor ELSE 0 END), 0) -\s
               COALESCE(SUM(CASE WHEN t.tipo = 'SAIDA' THEN t.valor ELSE 0 END), 0)
        FROM Transacao t
        WHERE t.usuario.id = :usuarioId
    """)
    double getSaldo( @Param("usuarioId") Long usuarioId );
}
