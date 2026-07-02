package WalletFlow.sistemagestaofinanceira.repository;

import WalletFlow.sistemagestaofinanceira.models.Meta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface MetaRepository extends JpaRepository<Meta, Long> {
    List<Meta> findByUsuarioId(Long usuarioId);
    Optional<Meta> findByUsuarioIdAndData(Long usuarioId, YearMonth data);
}
