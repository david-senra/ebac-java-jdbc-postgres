package dsenra.dao;

import dsenra.dao.generic.GenericDao;
import dsenra.dao.generic.SingletonMap;
import dsenra.domain.Produto;
import dsenra.exception.ObjetoNaoEncontradoException;

import java.util.HashMap;
import java.util.Map;

public class ProdutoDao extends GenericDao<Produto> implements IProdutoDao {
    private final Map<Long, Produto> listProdutos = new HashMap<>();

    public ProdutoDao() {
        super();
        Map<Long, Produto> mapaInterno =
                (Map<Long, Produto>) SingletonMap.getInstance().getMap().get(getTipoClasse());
        if (mapaInterno == null) {
            mapaInterno = new HashMap<>();
            SingletonMap.getInstance().getMap().put(getTipoClasse(), mapaInterno);
        }
    }

    @Override
    public Class<Produto> getTipoClasse() {
        return Produto.class;
    }

    @Override
    public void atualizar(Produto produto, Produto produtoCadastrado) {
        Produto produtoEncontrado;
        produtoEncontrado = (Produto) SingletonMap.getInstance().getMap().get(getTipoClasse()).get(produto.getId());
        if (produtoEncontrado != null) {
            produtoEncontrado.setNome(produto.getObjectData().getNome());
            produtoEncontrado.setDescricao(produto.getObjectData().getDescricao());
            produtoEncontrado.setPreco(produto.getObjectData().getPreco());
        }
        else {
            try {
                throw new ObjetoNaoEncontradoException("Produto não encontrado!");
            } catch (ObjetoNaoEncontradoException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
