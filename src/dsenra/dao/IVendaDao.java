package dsenra.dao;

import dsenra.dao.generic.IGenericDao;
import dsenra.domain.Venda;
import dsenra.exception.DaoException;
import dsenra.exception.TipoChaveNaoEncontradaException;

public interface IVendaDao extends IGenericDao<Venda, String> {

    public void finalizarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DaoException;

    public void cancelarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DaoException;
}
