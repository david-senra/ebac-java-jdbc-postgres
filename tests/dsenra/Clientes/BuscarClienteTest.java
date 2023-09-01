package dsenra.Clientes;

import dsenra.dao.ClienteDao;
import dsenra.domain.Cliente;
import dsenra.domain.mock.MockCliente;
import dsenra.exception.ObjetoNaoEncontradoException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BuscarClienteTest {

    private ClienteDao clienteDao;
    private Cliente mockCliente;
    private Cliente mockClienteAusente;

    @Before
    public void init() {
        clienteDao = new ClienteDao();
        clienteDao.listaElementos().clear();
        clienteDao = new ClienteDao();
        MockCliente mock = new MockCliente();
        mockCliente = mock.getMockCliente();
        mockClienteAusente = mock.getMockClienteNaoCadastrado();
    }

    @Test
    public void buscarClienteExistenteExpectSuccess() throws ObjetoNaoEncontradoException {
        clienteDao.cadastrar(mockCliente);
        Cliente clienteEncontradoMock = clienteDao.buscar(mockCliente);
        Assert.assertNotNull(clienteEncontradoMock);
        Assert.assertTrue(clienteDao.listaElementos().contains(clienteEncontradoMock));
    }

    @Test(expected = RuntimeException.class)
    public void buscarClienteNaoExistenteExpectNull() throws ObjetoNaoEncontradoException {
        clienteDao.cadastrar(mockCliente);
        Cliente clienteEncontradoMock = clienteDao.buscar(mockClienteAusente);
        Assert.assertNull(clienteEncontradoMock);
    }
}
