package WalletFlow.sistemagestaofinanceira.service;

import WalletFlow.sistemagestaofinanceira.dto.NovaCategoriaDTO;

import WalletFlow.sistemagestaofinanceira.enums.TipoTransacao;
import WalletFlow.sistemagestaofinanceira.exceptions.AcessoNegadoException;
import WalletFlow.sistemagestaofinanceira.exceptions.CategoriaJaExisteException;
import WalletFlow.sistemagestaofinanceira.exceptions.CategoriaProtegidaException;
import WalletFlow.sistemagestaofinanceira.models.Categoria;
import WalletFlow.sistemagestaofinanceira.models.Transacao;
import WalletFlow.sistemagestaofinanceira.models.Usuario;
import WalletFlow.sistemagestaofinanceira.repository.CategoriaRepository;
import WalletFlow.sistemagestaofinanceira.repository.TransacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final TransacaoRepository transacaoRepository;

    public CategoriaService(CategoriaRepository categoriaRepository, TransacaoRepository transacaoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.transacaoRepository = transacaoRepository;
    }

    @Transactional
    public void salvar(NovaCategoriaDTO dto, Usuario usuario) throws CategoriaJaExisteException {
        Categoria categoria = dto.toEntity();
        categoria.setUsuario(usuario);

        if(categoriaRepository.findByUsuarioIdAndNome(usuario.getId(), dto.getNome()).isPresent()){
            throw new CategoriaJaExisteException();
        }
        categoriaRepository.save(categoria);
    }

    @Transactional(readOnly = true)
    public Categoria buscarPorId(Long id, Long usuarioId) {
        Categoria categoria = categoriaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));

        if(!categoria.getUsuario().getId().equals(usuarioId)){
            throw new AcessoNegadoException();
        }

        return categoria;
    }

    @Transactional(readOnly = true)
    public List<Categoria> listarPorUsuario(Long usuarioId) {
        return categoriaRepository.findByUsuarioId(usuarioId);
    }

    @Transactional
    public void excluir(Long id, Long usuarioId) throws CategoriaProtegidaException {
        Categoria categoria = buscarPorId(id, usuarioId); //validar permissão

        if (categoria.isPadrao()) {
            throw new CategoriaProtegidaException();
        }

        List<Transacao> transacoes = transacaoRepository.findByCategoriaId(id);
        transacoes.forEach(t -> t.setCategoria(categoriaRepository.findPadraoByUsuarioIdAndTipo(usuarioId, t.getTipo())));
        transacaoRepository.saveAll(transacoes);

        categoriaRepository.deleteById(id);
    }

    @Transactional
    public void editar(NovaCategoriaDTO dto, Long usuarioId) throws CategoriaJaExisteException {
        Categoria categoria = buscarPorId(dto.getId(), usuarioId); //validar permissão

        if (categoria.isPadrao()) {
            throw new CategoriaProtegidaException();
        }

        if(!dto.getNome().equals(categoria.getNome())) {
            if(categoriaRepository.findByUsuarioIdAndNome(usuarioId, dto.getNome()).isPresent()) {
                throw new CategoriaJaExisteException();
            }
        }

        categoria.setNome(dto.getNome());
        categoria.setTipo(dto.getTipo());
        categoria.setCor(dto.getCor());

        categoriaRepository.save(categoria);
    }

    @Transactional
    public void criarCategoriasPadrao(Usuario usuario) {
        List<Categoria> padroes = List.of(
                new Categoria("Outros", TipoTransacao.ENTRADA, "#343232", true),
                new Categoria("Salário", TipoTransacao.ENTRADA, "#198754"),
                new Categoria("Freelance", TipoTransacao.ENTRADA, "#20c997"),
                new Categoria("Alimentação", TipoTransacao.SAIDA, "#dc3545"),
                new Categoria("Transporte", TipoTransacao.SAIDA, "#fd7e14"),
                new Categoria("Aluguel", TipoTransacao.SAIDA, "#6f42c1"),
                new Categoria("Lazer", TipoTransacao.SAIDA, "#0dcaf0"),
                new Categoria("Outros", TipoTransacao.SAIDA, "#343232", true)
        );

        padroes.forEach(c -> c.setUsuario(usuario));
        categoriaRepository.saveAll(padroes);
    }
}
