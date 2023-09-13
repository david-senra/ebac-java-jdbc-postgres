package dsenra.services.generic;

import dsenra.dao.ClienteDao;
import dsenra.dao.ProdutoDao;
import dsenra.dao.generic.GenericDao;
import dsenra.domain.Cliente;
import dsenra.domain.IPersistente;
import dsenra.exception.DaoException;
import dsenra.exception.MaisDeUmRegistroException;
import dsenra.exception.TableException;
import dsenra.exception.TipoChaveNaoEncontradaException;

import java.sql.SQLException;


public class GenericService<T extends IPersistente> implements IGenericService<T> {

    private GenericDao<T, Long> genericDao;
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
            return genericDao.buscar(objeto.getId());
        } catch (MaisDeUmRegistroException | TableException | DaoException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean atualizar(T objeto1, T objeto2) throws SQLException, TipoChaveNaoEncontradaException, DaoException {
        genericDao = getDao(objeto1);
        genericDao.atualizar(objeto1);
        return true;
    }

    public GenericDao<T, Long> getDao(T objeto) {
        if (objeto.getClass() == Cliente.class) return (GenericDao<T, Long>) clienteDao;
        else return (GenericDao<T, Long>) produtoDao;
    }
}
