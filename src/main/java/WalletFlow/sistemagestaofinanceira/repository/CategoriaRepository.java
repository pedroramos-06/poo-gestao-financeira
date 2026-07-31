package WalletFlow.sistemagestaofinanceira.repository;

import WalletFlow.sistemagestaofinanceira.enums.TipoTransacao;
import WalletFlow.sistemagestaofinanceira.models.Categoria;
import WalletFlow.sistemagestaofinanceira.models.Meta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByUsuarioIdOrderByTipoAscIdDesc(Long usuarioId);
    Optional<Categoria> findByUsuarioIdAndNomeAndTipo(Long usuarioId, String nome, TipoTransacao tipo);

    @Query("""
        SELECT c FROM Categoria c
        WHERE c.usuario.id = :usuarioId
        AND c.padrao = true
        AND c.tipo = :tipo
    """)
    Categoria findPadraoByUsuarioIdAndTipo(
            @Param("usuarioId") Long usuarioId,
            @Param("tipo") TipoTransacao tipo
    );
}
