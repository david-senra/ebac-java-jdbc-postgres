package dsenra.fabricas;

import dsenra.dao.IProdutoDao;
import dsenra.domain.Produto;
import dsenra.exception.ObjetoNaoEncontradoException;
import dsenra.fabricas.generic.GenericFactory;

public class FactoryProdutos extends GenericFactory<Produto> implements IFactoryProdutos {
    private final IProdutoDao produtoDao;

    public FactoryProdutos(IProdutoDao produtoDao) {
        this.produtoDao = produtoDao;
    }

    @Override
    public boolean cadastrar(Produto produto) {
        try {
            produtoDao.cadastrar(produto);
        } catch (ObjetoNaoEncontradoException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
