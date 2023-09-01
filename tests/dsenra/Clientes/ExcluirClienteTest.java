package dsenra.Clientes;

import dsenra.dao.ClienteDao;
import dsenra.domain.Cliente;
import dsenra.domain.mock.MockCliente;
import dsenra.exception.ObjetoNaoEncontradoException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExcluirClienteTest {

    private ClienteDao clienteDao;
    private Cliente mockCliente;
    private final MockCliente mock = new MockCliente();

    @Before
    public void init() {
        clienteDao = new ClienteDao();
        clienteDao.listaElementos().clear();
        clienteDao = new ClienteDao();
        mockCliente = mock.getMockCliente();
    }

    @Test
    public void excluirClienteExistenteExpectSuccess() throws ObjetoNaoEncontradoException {
        clienteDao.cadastrar(mockCliente);
        Assert.assertTrue(clienteDao.listaElementos().contains(mockCliente));

        clienteDao.excluir(mockCliente);
        Assert.assertFalse(clienteDao.listaElementos().contains(mockCliente));
    }

    @Test (expected = RuntimeException.class)
    public void excluirClienteNaoExistenteExpectError() throws ObjetoNaoEncontradoException {
        Cliente mockClienteNaoCadastrado = mock.getMockClienteNaoCadastrado();
        clienteDao.cadastrar(mockCliente);
        Assert.assertTrue(clienteDao.listaElementos().contains(mockCliente));
        clienteDao.excluir(mockClienteNaoCadastrado);
    }
}
