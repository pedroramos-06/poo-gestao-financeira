package WalletFlow.sistemagestaofinanceira.repository;

import WalletFlow.sistemagestaofinanceira.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    @Query("SELECT u.metaPadrao FROM Usuario u WHERE u.id = :usuarioId")
    BigDecimal getMetaPadraoById(@Param("usuarioId") Long usuarioId);
}