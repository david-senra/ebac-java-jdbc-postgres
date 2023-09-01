package dsenra.fabricas.generic;

import dsenra.dao.ClienteDao;
import dsenra.dao.ProdutoDao;
import dsenra.dao.generic.GenericDao;
import dsenra.domain.Cliente;
import dsenra.domain.IPersistente;
import dsenra.exception.ObjetoNaoEncontradoException;

public class GenericFactory<T extends IPersistente> implements IGenericFactory<T> {

    private GenericDao<T> genericDao;
    private final ClienteDao clienteDao;
    private final ProdutoDao produtoDao;

    public GenericFactory() {
        clienteDao = new ClienteDao();
        produtoDao = new ProdutoDao();
    }

    @Override
    public boolean cadastrar(T objeto) {
        genericDao = getDao(objeto);
        try {
            genericDao.cadastrar(objeto);
        } catch (ObjetoNaoEncontradoException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean excluir(T objeto) {
        genericDao = getDao(objeto);
        try {
            genericDao.excluir(objeto);
        } catch (ObjetoNaoEncontradoException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public GenericDao<T> getDao(T objeto) {
        if (objeto.getClass() == Cliente.class) return (GenericDao<T>) clienteDao;
        else return (GenericDao<T>) produtoDao;
    }
}
