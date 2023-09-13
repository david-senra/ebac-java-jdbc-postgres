package dsenra.fabricas;

import dsenra.dao.ProdutoDao;
import dsenra.domain.Produto;
import dsenra.exception.DaoException;
import dsenra.exception.TipoChaveNaoEncontradaException;
import dsenra.fabricas.generic.GenericFactory;

public class FactoryProdutos extends GenericFactory<Produto> implements IFactoryProdutos {
    private final ProdutoDao produtoDao;

    public FactoryProdutos(ProdutoDao produtoDao) {
        this.produtoDao = produtoDao;
    }

    @Override
    public boolean cadastrar(Produto produto) {
        try {
            produtoDao.cadastrar(produto);
        } catch (TipoChaveNaoEncontradaException | DaoException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
