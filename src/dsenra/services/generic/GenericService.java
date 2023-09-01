package dsenra.services.generic;

import dsenra.dao.ClienteDao;
import dsenra.dao.ProdutoDao;
import dsenra.dao.generic.GenericDao;
import dsenra.domain.Cliente;
import dsenra.domain.IPersistente;
import dsenra.exception.ObjetoNaoEncontradoException;


public class GenericService<T extends IPersistente> implements IGenericService<T> {

    private GenericDao<T> genericDao;
    private final ClienteDao clienteDao;
    private final ProdutoDao produtoDao;

    public GenericService() {
        clienteDao = new ClienteDao();
        produtoDao = new ProdutoDao();
    }

    @Override
    public T buscar(T objeto) {
        genericDao = getDao(objeto);
        try {
            return genericDao.buscar(objeto);
        } catch (ObjetoNaoEncontradoException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean atualizar(T objeto1, T objeto2) {
        genericDao = getDao(objeto1);
        genericDao.atualizar(objeto1, objeto2);
        return true;
    }

    public GenericDao<T> getDao(T objeto) {
        if (objeto.getClass() == Cliente.class) return (GenericDao<T>) clienteDao;
        else return (GenericDao<T>) produtoDao;
    }
}
