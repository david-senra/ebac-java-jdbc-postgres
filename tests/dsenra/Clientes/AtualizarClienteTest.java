package dsenra.Clientes;

import dsenra.dao.ClienteDao;
import dsenra.domain.Cliente;
import dsenra.domain.mock.MockCliente;
import dsenra.exception.ObjetoNaoEncontradoException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AtualizarClienteTest {

    private ClienteDao clienteDao;
    private Cliente mockCliente = new Cliente();
    private Cliente mockClienteMesmosDados = new Cliente();
    private Cliente mockClienteRepetido = new Cliente();
    private Cliente mockClienteNaoCadastrado = new Cliente();
    private final MockCliente mock = new MockCliente();

    @Before
    public void init() {
        clienteDao = new ClienteDao();
        clienteDao.listaElementos().clear();
        clienteDao = new ClienteDao();
        mockCliente = mock.getMockCliente();
        mockClienteMesmosDados = mock.getMockClienteIdDiferenteTudoRepetido(mockCliente);
        mockClienteNaoCadastrado = mock.getMockClienteNaoCadastrado();
        mockClienteRepetido = mock.getMockClienteIdRepetido(mockCliente);
    }

    @Test
    public void atualizarClienteExistenteExpectSuccess() throws ObjetoNaoEncontradoException {
        clienteDao.cadastrar(mockCliente);
        Assert.assertTrue(
                mockCliente.getNome().equals(mockClienteMesmosDados.getNome()) &&
                        mockCliente.getSobrenome().equals(mockClienteMesmosDados.getSobrenome()) &&
                        mockCliente.getCpf().equals(mockClienteMesmosDados.getCpf()) &&
                        mockCliente.getTelefone().equals(mockClienteMesmosDados.getTelefone()) &&
                        mockCliente.getEndereco().equals(mockClienteMesmosDados.getEndereco()) &&
                        mockCliente.getCidade().equals(mockClienteMesmosDados.getCidade()) &&
                        mockCliente.getEstado().equals(mockClienteMesmosDados.getEstado())
        );

        mockClienteRepetido.setNome("Anônimo");
        mockClienteRepetido.setSobrenome("Incógnito");
        mockClienteRepetido.setCpf("555.555.555-55");
        mockClienteRepetido.setTelefone("(11) 91111-1111");
        mockClienteRepetido.setEndereco("Rua dos Bobos");
        mockClienteRepetido.setCidade("Cidade Inexistente");
        mockClienteRepetido.setEstado("WW");
        clienteDao.atualizar(mockClienteRepetido, mockCliente);

        Cliente mockClienteAlterado = clienteDao.buscar(mockCliente);
        Assert.assertTrue(
                mockClienteAlterado.getNome().equals("Anônimo") &&
                        mockClienteAlterado.getSobrenome().equals("Incógnito") &&
                        mockClienteAlterado.getTelefone().equals("(11) 91111-1111") &&
                        mockClienteAlterado.getEndereco().equals("Rua dos Bobos") &&
                        mockClienteAlterado.getCidade().equals("Cidade Inexistente") &&
                        mockClienteAlterado.getEstado().equals("WW")
        );
        Assert.assertEquals(mockClienteAlterado.getId(), mockCliente.getId());
        clienteDao.listaElementos().clear();
    }

    @Test(expected = RuntimeException.class)
    public void atualizarClienteInexistenteExpectError() throws ObjetoNaoEncontradoException {
        clienteDao.cadastrar(mockCliente);
        Assert.assertTrue(
                mockCliente.getNome().equals(mockClienteMesmosDados.getNome()) &&
                        mockCliente.getSobrenome().equals(mockClienteMesmosDados.getSobrenome()) &&
                        mockCliente.getCpf().equals(mockClienteMesmosDados.getCpf()) &&
                        mockCliente.getTelefone().equals(mockClienteMesmosDados.getTelefone()) &&
                        mockCliente.getEndereco().equals(mockClienteMesmosDados.getEndereco()) &&
                        mockCliente.getCidade().equals(mockClienteMesmosDados.getCidade()) &&
                        mockCliente.getEstado().equals(mockClienteMesmosDados.getEstado())
        );

        mockClienteNaoCadastrado.setNome("Anônimo");
        mockClienteNaoCadastrado.setSobrenome("Incógnito");
        mockClienteNaoCadastrado.setCpf("555.555.555-55");
        mockClienteNaoCadastrado.setTelefone("(11) 91111-1111");
        mockClienteNaoCadastrado.setEndereco("Rua dos Bobos");
        mockClienteNaoCadastrado.setCidade("Cidade Inexistente");
        mockClienteNaoCadastrado.setEstado("WW");
        clienteDao.atualizar(mockClienteNaoCadastrado, mockCliente);
        clienteDao.listaElementos().clear();
    }
}
