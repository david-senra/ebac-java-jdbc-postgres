package dsenra.Servicos;

import dsenra.dao.*;
import dsenra.domain.Cliente;
import dsenra.domain.IPersistente;
import dsenra.fabricas.generic.GenericFactory;
import dsenra.services.generic.GenericService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClientServiceTest<T extends IPersistente> {
    private Cliente cliente;
    private final ClienteDao clienteDao = new ClienteDao();
    private final GenericService<T> service;
    private GenericFactory<T> factory;

    public ClientServiceTest() {
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
    public void BuscarClienteExpectsSuccess() {
        factory.cadastrar((T) cliente);
        Cliente clienteConsultado = (Cliente) service.buscar((T) cliente);
        Assert.assertNotNull(clienteConsultado);
        cliente = null;
        factory = null;
        clienteDao.listaElementos().clear();
    }

    @Test
    public void AtualizarClienteExpectsSuccess() {
        factory.cadastrar((T) cliente);
        Cliente clienteConsultado = (Cliente) service.buscar((T) cliente);
        Assert.assertNotNull(clienteConsultado);
        boolean retorno = service.atualizar((T) clienteConsultado, (T) clienteConsultado);
        Assert.assertTrue(retorno);
    }
}
