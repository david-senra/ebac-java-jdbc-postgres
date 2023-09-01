package dsenra.Pedidos;

import dsenra.dao.ClienteDao;
import dsenra.dao.ProdutoDao;
import dsenra.domain.Cliente;
import dsenra.domain.Produto;
import dsenra.domain.mock.MockCliente;
import dsenra.domain.mock.MockProduto;
import dsenra.exception.ObjetoNaoEncontradoException;
import dsenra.fabricas.pedidos.FactoryCarrinho;
import dsenra.fabricas.pedidos.IFactoryCarrinho;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class VerCarrinhoVazioTest {

    private ClienteDao clienteDao;
    private ProdutoDao produtoDao;
    private Cliente mockCliente;
    private Produto mockProduto;
    private Produto mockProduto2;
    private final MockCliente mockCl = new MockCliente();
    private final MockProduto mockPr = new MockProduto();
    private final IFactoryCarrinho genericFactory = new FactoryCarrinho();

    @Before
    public void init() {
        clienteDao = new ClienteDao();
        clienteDao.listaElementos().clear();
        clienteDao = new ClienteDao();
        produtoDao = new ProdutoDao();
        produtoDao.listaElementos().clear();
        produtoDao = new ProdutoDao();
        mockCliente = mockCl.getMockCliente();
        mockProduto = mockPr.getMockProduto();
        mockProduto2 = mockPr.getMockProdutoNaoCadastrado();
    }

    @Test
    public void verCarrinhoOriginalmenteVazio() throws ObjetoNaoEncontradoException {
        clienteDao.cadastrar(mockCliente);
        Assert.assertNull(mockCliente.verCarrinho());
        clienteDao.listaElementos().clear();
        produtoDao.listaElementos().clear();
    }
    @Test
    public void verCarrinhoOriginalmenteCheioDepoisVazio() throws ObjetoNaoEncontradoException {
        clienteDao.cadastrar(mockCliente);
        produtoDao.cadastrar(mockProduto);
        produtoDao.cadastrar(mockProduto2);

        genericFactory.adicionarCarrinho(mockCliente, mockProduto);
        genericFactory.adicionarCarrinho(mockCliente, mockProduto2);
        Assert.assertEquals(2, mockCliente.verCarrinho().size());

        genericFactory.removerCarrinho(mockCliente, mockProduto);
        genericFactory.removerCarrinho(mockCliente, mockProduto2);
        Assert.assertNull(mockCliente.verCarrinho());
        clienteDao.listaElementos().clear();
        produtoDao.listaElementos().clear();
    }
}
