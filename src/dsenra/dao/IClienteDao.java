package dsenra.dao;

import dsenra.dao.generic.IGenericDao;
import dsenra.domain.Cliente;


public interface IClienteDao extends IGenericDao<Cliente> {
    void atualizar(Cliente objeto1, Cliente Objeto2);
}
