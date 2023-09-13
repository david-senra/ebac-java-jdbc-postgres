package dsenra.services.generic;


import dsenra.domain.IPersistente;
import dsenra.exception.DaoException;
import dsenra.exception.TipoChaveNaoEncontradaException;

import java.sql.SQLException;

public interface IGenericService<T extends IPersistente> {
    T buscar(T objeto);
    boolean atualizar(T objeto1, T objeto2) throws SQLException, TipoChaveNaoEncontradaException, DaoException;
}
