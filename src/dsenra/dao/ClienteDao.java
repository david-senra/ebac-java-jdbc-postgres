package dsenra.dao;

import dsenra.dao.generic.GenericDao;
import dsenra.dao.generic.SingletonMap;
import dsenra.domain.Cliente;
import dsenra.exception.ObjetoNaoEncontradoException;

import java.util.HashMap;
import java.util.Map;

public class ClienteDao extends GenericDao<Cliente> implements IClienteDao {
    private final Map<Long, Cliente> listClientes = new HashMap<>();

    public ClienteDao() {
        super();
        Map<Long, Cliente> mapaInterno =
                (Map<Long, Cliente>) SingletonMap.getInstance().getMap().get(getTipoClasse());
        if (mapaInterno == null) {
            mapaInterno = new HashMap<>();
            SingletonMap.getInstance().getMap().put(getTipoClasse(), mapaInterno);
        }
    }

    @Override
    public Class<Cliente> getTipoClasse() {
        return Cliente.class;
    }

    @Override
    public void atualizar(Cliente cliente, Cliente clienteCadastrado) {
        Cliente clienteEncontrado;
        clienteEncontrado = (Cliente) SingletonMap.getInstance().getMap().get(getTipoClasse()).get(cliente.getId());
        if (clienteEncontrado != null) {
            clienteCadastrado.setNome(cliente.getNome());
            clienteCadastrado.setSobrenome(cliente.getSobrenome());
            clienteCadastrado.setTelefone(cliente.getTelefone());
            clienteCadastrado.setEndereco(cliente.getEndereco());
            clienteCadastrado.setCidade(cliente.getCidade());
            clienteCadastrado.setEstado(cliente.getEstado());
        }
        else try {
            throw new ObjetoNaoEncontradoException("Cliente não existe!");
        } catch (ObjetoNaoEncontradoException e) {
            throw new RuntimeException(e);
        }
    }
}
