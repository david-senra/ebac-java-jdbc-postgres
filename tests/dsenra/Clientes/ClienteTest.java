package dsenra.Clientes;

import dsenra.dao.ClienteDao;
import dsenra.domain.Cliente;
import dsenra.domain.mock.MockCliente;
import dsenra.exception.DaoException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class ClienteTest {

    private ClienteDao clienteDao;
    private Cliente mockCliente;
    private Cliente mockCliente2;
    private Cliente mockClienteRepetido2;
    private Cliente mockClienteIdDiferenteTudoRepetido;

    @Before
    public void init() throws Exception {
        clienteDao = new ClienteDao();
        MockCliente mock = new MockCliente();
        mockCliente = mock.getMockCliente();
        mockCliente2 = mock.getMockClienteNaoCadastrado();
        Cliente mockClienteRepetido = mock.getMockClienteIdRepetido(mockCliente);
        mockClienteRepetido.setId(mockCliente.getId());
        mockClienteRepetido2 = mock.getMockClienteCPFRepetido(mockCliente);
        mockClienteIdDiferenteTudoRepetido = mock.getMockClienteIdDiferenteTudoRepetido(mockCliente);
    }

    @After
    public void end() throws Exception {
        clienteDao.limparClientes();
    }

    @Test
    public void cadastrarClienteNovoExpectSuccess() throws Exception {
        boolean countCadastrar = clienteDao.cadastrar(mockCliente);
        Assert.assertTrue(countCadastrar);
        Cliente clienteEncontrado = clienteDao.buscar(mockCliente.getId());
        Assert.assertNotNull(clienteEncontrado);
        Assert.assertEquals(clienteEncontrado.getCpf(), mockCliente.getCpf());
        Assert.assertEquals(clienteEncontrado.getNome(), mockCliente.getNome());
    }

    @Test(expected = DaoException.class)
    public void cadastrarClienteIdJaExistenteExpectError() throws Exception {
        boolean countCadastrar = clienteDao.cadastrar(mockCliente);
        Assert.assertTrue(countCadastrar);
        if (mockCliente.getCpf().equals(mockClienteRepetido2.getCpf())) {
            clienteDao.cadastrar(mockClienteRepetido2);
        }
    }

    @Test
    public void cadastrarClienteIdDiferenteMesmoNomeExpectSuccess() throws Exception {
        boolean countCadastrar = clienteDao.cadastrar(mockCliente);
        Assert.assertTrue(countCadastrar);
        boolean countCadastrar2 = clienteDao.cadastrar(mockClienteIdDiferenteTudoRepetido);
        Assert.assertTrue(countCadastrar2);
        Cliente clienteEncontrado = clienteDao.buscar(mockCliente.getId());
        Assert.assertNotNull(clienteEncontrado);
        Assert.assertEquals(clienteEncontrado.getCpf(), mockCliente.getCpf());
        Assert.assertEquals(clienteEncontrado.getNome(), mockCliente.getNome());
        Cliente clienteEncontrado2 = clienteDao.buscar(mockClienteIdDiferenteTudoRepetido.getId());
        Assert.assertNotNull(clienteEncontrado2);
        Assert.assertEquals(clienteEncontrado2.getCpf(), mockClienteIdDiferenteTudoRepetido.getCpf());
        Assert.assertEquals(clienteEncontrado2.getNome(), mockClienteIdDiferenteTudoRepetido.getNome());
        clienteDao.excluir(mockCliente.getId());
        List<Cliente> listaClientes = (List<Cliente>) clienteDao.listaElementos();
        Assert.assertNotNull(listaClientes);
        Assert.assertEquals(1, listaClientes.size());
        clienteDao.excluir(mockClienteIdDiferenteTudoRepetido.getId());
        listaClientes = (List<Cliente>) clienteDao.listaElementos();
        Assert.assertNotNull(listaClientes);
        Assert.assertEquals(0, listaClientes.size());
    }

    @Test
    public void buscarTodosTest() throws Exception {
        boolean countCadastrar = clienteDao.cadastrar(mockCliente);
        Assert.assertTrue(countCadastrar);
        boolean countCadastrar2 = clienteDao.cadastrar(mockCliente2);
        Assert.assertTrue(countCadastrar2);
        List<Cliente> listaClientes = (List<Cliente>) clienteDao.listaElementos();
        Assert.assertNotNull(listaClientes);
        Assert.assertEquals(2, listaClientes.size());
        listaClientes.forEach(cliente -> Assert.assertTrue(cliente.getNome().equals(mockCliente.getNome())
                                                            || cliente.getNome().equals(mockCliente2.getNome())));
    }

    @Test
    public void atualizarTest() throws Exception {
        boolean countCadastrar = clienteDao.cadastrar(mockCliente);
        Assert.assertTrue(countCadastrar);

        Cliente clienteEncontrado = clienteDao.buscar(mockCliente.getId());
        Assert.assertNotNull(clienteEncontrado);
        Assert.assertEquals(clienteEncontrado.getNome(), mockCliente.getNome());
        Assert.assertEquals(clienteEncontrado.getCpf(), mockCliente.getCpf());

        clienteEncontrado.setNome("Mario Bros");
        clienteEncontrado.setSobrenome("Vulgo Luigi");
        clienteEncontrado.setCpf("000-000-000.00");
        clienteDao.atualizar(clienteEncontrado);

        Cliente clienteEncontrado2 = clienteDao.buscar(clienteEncontrado.getId());
        Assert.assertNotNull(clienteEncontrado2);
        Assert.assertEquals(clienteEncontrado.getNome(), clienteEncontrado2.getNome());
        Assert.assertEquals(clienteEncontrado.getTelefone(), clienteEncontrado2.getTelefone());
        Assert.assertEquals(clienteEncontrado.getCpf(), clienteEncontrado2.getCpf());
    }
}
