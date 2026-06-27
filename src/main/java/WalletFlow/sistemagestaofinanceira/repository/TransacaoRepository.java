package WalletFlow.sistemagestaofinanceira.repository;

import WalletFlow.sistemagestaofinanceira.models.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    List<Transacao> findByUsuarioId(Long usuarioId);
}
