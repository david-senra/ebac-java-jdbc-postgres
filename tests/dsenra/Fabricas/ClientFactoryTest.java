package dsenra.Fabricas;

import dsenra.dao.ClienteDao;
import dsenra.domain.Cliente;
import dsenra.domain.IPersistente;
import dsenra.exception.DaoException;
import dsenra.fabricas.generic.GenericFactory;
import dsenra.services.generic.GenericService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClientFactoryTest<T extends IPersistente> {
    private Cliente cliente;
    private final ClienteDao clienteDao = new ClienteDao();
    private final GenericService<Cliente> service;
    private GenericFactory<T> factory;

    public ClientFactoryTest() {
        service = new GenericService<>();
        factory = new GenericFactory<>();
    }

    @Before
    public void init() throws DaoException {
        clienteDao.listaElementos().clear();
        cliente = new Cliente(1L,
                "Chico",
                "Bento",
                "123.123.123-12",
                "(41) 98642-4287",
                "Rua das Glórias",
                880L,
                "Natal",
                "RN");
    }

    @Test
    public void CadastrarClienteExpectsSuccess() throws DaoException {
        factory.cadastrar((T) cliente);
        Cliente clienteConsultado = (Cliente) service.buscar((Cliente) cliente);
        Assert.assertNotNull(clienteConsultado);
        cliente = null;
        factory = null;
        clienteDao.listaElementos().clear();
    }

    @Test
    public void ExcluirClienteExpectsSuccess() throws DaoException {
        factory.cadastrar((T) cliente);
        Cliente clienteConsultado = service.buscar(cliente);
        boolean retorno = factory.excluir((T) clienteConsultado);
        Assert.assertTrue(retorno);
        cliente = null;
        factory = null;
        clienteDao.listaElementos().clear();
    }

    @Test(expected = RuntimeException.class)
    public void ExcluirClienteNaoExistenteExpectsError() throws DaoException {
        boolean retorno = factory.excluir((T) cliente);
        Assert.assertTrue(retorno);
        factory = null;
        clienteDao.listaElementos().clear();
    }
}
