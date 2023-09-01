package dsenra.Clientes;

import dsenra.dao.ClienteDao;
import dsenra.domain.Cliente;
import dsenra.domain.mock.MockCliente;
import dsenra.exception.ObjetoNaoEncontradoException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CadastrarClienteTest {

    private ClienteDao clienteDao;
    private Cliente mockCliente;
    private Cliente mockClienteRepetido;
    private Cliente mockClienteIdDiferenteTudoRepetido;

    @Before
    public void init() {
        clienteDao = new ClienteDao();
        clienteDao.listaElementos().clear();
        clienteDao = new ClienteDao();
        MockCliente mock = new MockCliente();
        mockCliente = mock.getMockCliente();
        mockClienteRepetido = mock.getMockClienteIdRepetido(mockCliente);
        mockClienteIdDiferenteTudoRepetido = mock.getMockClienteIdDiferenteTudoRepetido(mockCliente);
    }

    @Test
    public void cadastrarClienteNovoExpectSuccess() throws ObjetoNaoEncontradoException {
        clienteDao.cadastrar(mockCliente);
        Assert.assertTrue(clienteDao.listaElementos().contains(mockCliente));
        clienteDao.excluir(mockCliente);
        clienteDao.listaElementos().clear();
    }

    @Test(expected = RuntimeException.class)
    public void cadastrarClienteJaExistenteExpectError() throws ObjetoNaoEncontradoException {
        clienteDao.cadastrar(mockCliente);
        if (mockCliente.getId().equals(mockClienteRepetido.getId())) {
            clienteDao.cadastrar(mockClienteRepetido);
        }
        clienteDao.listaElementos().clear();
    }

    @Test
    public void cadastrarClienteIdDiferenteMesmoNomeExpectSuccess() throws ObjetoNaoEncontradoException {
        clienteDao.cadastrar(mockCliente);
        clienteDao.cadastrar(mockClienteIdDiferenteTudoRepetido);
        Assert.assertTrue(clienteDao.listaElementos().contains(mockCliente));
        Assert.assertTrue(clienteDao.listaElementos().contains(mockClienteIdDiferenteTudoRepetido));
        clienteDao.excluir(mockCliente);
        clienteDao.excluir(mockClienteIdDiferenteTudoRepetido);
        clienteDao.listaElementos().clear();
    }
}
