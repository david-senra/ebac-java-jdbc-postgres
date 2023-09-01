package dsenra.Fabricas;

import dsenra.dao.*;
import dsenra.domain.Cliente;
import dsenra.domain.IPersistente;
import dsenra.fabricas.FactoryClientes;
import dsenra.fabricas.generic.GenericFactory;
import dsenra.services.generic.GenericService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClientFactoryTest<T extends IPersistente> {
    private Cliente cliente;
    private final ClienteDao clienteDao = new ClienteDao();
    private final GenericService<T> service;
    private GenericFactory<T> factory;

    public ClientFactoryTest() {
        service = new GenericService<>();
        factory = new GenericFactory<>();
    }

    @Before
    public void init() {
        clienteDao.listaElementos().clear();
        cliente = null;
        cliente = new Cliente(1L,
                "Chico",
                "Bento",
                "123.123.123-12",
                "(41) 98642-4287",
                "Rua das Glórias",
                "Natal",
                "RN");
    }

    @Test
    public void CadastrarClienteExpectsSuccess() {
        factory.cadastrar((T) cliente);
        Cliente clienteConsultado = (Cliente) service.buscar((T) cliente);
        Assert.assertNotNull(clienteConsultado);
        cliente = null;
        factory = null;
        clienteDao.listaElementos().clear();
    }

    @Test
    public void ExcluirClienteExpectsSuccess() {
        factory.cadastrar((T) cliente);
        Cliente clienteConsultado = (Cliente) service.buscar((T) cliente);
        boolean retorno = factory.excluir((T) clienteConsultado);
        Assert.assertTrue(retorno);
        cliente = null;
        factory = null;
        clienteDao.listaElementos().clear();
    }

    @Test(expected = RuntimeException.class)
    public void ExcluirClienteNaoExistenteExpectsError() {
        boolean retorno = factory.excluir((T) cliente);
        Assert.assertTrue(retorno);
        factory = null;
        clienteDao.listaElementos().clear();
    }
}
