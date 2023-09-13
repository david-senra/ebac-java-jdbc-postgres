package dsenra.dao.generic;

import dsenra.domain.IPersistente;
import dsenra.exception.DaoException;
import dsenra.exception.MaisDeUmRegistroException;
import dsenra.exception.TableException;
import dsenra.exception.TipoChaveNaoEncontradaException;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;

public interface IGenericDao <T extends IPersistente, E extends Serializable> {
    boolean cadastrar(T objeto) throws TipoChaveNaoEncontradaException, DaoException;
    T buscar(E valor) throws MaisDeUmRegistroException, TableException, DaoException;
    void excluir(E valor) throws DaoException, SQLException;
    void atualizar(T objeto) throws TipoChaveNaoEncontradaException, DaoException, SQLException;
    Collection<T> listaElementos() throws DaoException;
}
