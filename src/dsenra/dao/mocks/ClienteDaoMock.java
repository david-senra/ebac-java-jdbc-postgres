package dsenra.dao.mocks;

import dsenra.dao.IClienteDao;
import dsenra.domain.Cliente;

import java.util.Collection;

public class ClienteDaoMock implements IClienteDao {

    @Override
    public boolean cadastrar(Cliente objeto) {
        return true;
    }

    @Override
    public Cliente buscar(Cliente clienteRecebido) {
        return clienteRecebido;
    }

    @Override
    public boolean excluir(Cliente objeto) {
        return true;
    }

    @Override
    public void atualizar(Cliente objeto1, Cliente objeto2) {

    }

    @Override
    public Collection<Cliente> listaElementos() {
        return null;
    }
}
