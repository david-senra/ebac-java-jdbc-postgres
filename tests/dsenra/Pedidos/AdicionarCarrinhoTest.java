package dsenra.Pedidos;

import dsenra.dao.ClienteDao;
import dsenra.dao.ProdutoDao;
import dsenra.domain.Cliente;
import dsenra.domain.Produto;
import dsenra.domain.mock.MockCliente;
import dsenra.domain.mock.MockProduto;
import dsenra.domain.pedidos.ProdutoCarrinho;
import dsenra.exception.ObjetoNaoEncontradoException;
import dsenra.fabricas.pedidos.FactoryPedidos;
import dsenra.fabricas.pedidos.IFactoryPedidos;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Objects;

public class AdicionarCarrinhoTest {

    private ClienteDao clienteDao;
    private ProdutoDao produtoDao;
    private Cliente mockCliente;
    private Produto mockProduto;
    private Produto mockProduto2;
    private final MockCliente mock = new MockCliente();
    private final IFactoryPedidos genericFactory = new FactoryPedidos();

    @Before
    public void init() {
        clienteDao = new ClienteDao();
        clienteDao.listaElementos().clear();
        clienteDao = new ClienteDao();
        produtoDao = new ProdutoDao();
        produtoDao.listaElementos().clear();
        produtoDao = new ProdutoDao();
        MockProduto mockPr = new MockProduto();
        mockCliente = mock.getMockCliente();
        mockProduto = mockPr.getMockProduto();
        mockProduto2 = mockPr.getMockProdutoNaoCadastrado();
    }
    @Test
    public void adicionarProdutoCarrinhoExpectSuccess() throws ObjetoNaoEncontradoException {
        clienteDao.cadastrar(mockCliente);
        produtoDao.cadastrar(mockProduto);
        produtoDao.cadastrar(mockProduto2);

        IFactoryPedidos genericFactory = new FactoryPedidos();
        genericFactory.adicionarCarrinho(mockCliente, mockProduto);
        genericFactory.adicionarCarrinho(mockCliente, mockProduto2);

        Assert.assertTrue(mockCliente.verCarrinho().stream()
                .anyMatch(produtoFisico -> produtoFisico.getNome().equals(mockProduto.getNome())));
        Assert.assertTrue(mockCliente.verCarrinho().stream()
                .anyMatch(produtoFisico -> produtoFisico.getNome().equals(mockProduto2.getNome())));
        clienteDao.listaElementos().clear();
        produtoDao.listaElementos().clear();
    }

    @Test
    public void adicionarProdutoRepetidoCarrinhoExpectSuccess() throws ObjetoNaoEncontradoException {
        clienteDao.cadastrar(mockCliente);
        produtoDao.cadastrar(mockProduto);

        genericFactory.adicionarCarrinho(mockCliente, mockProduto);
        genericFactory.adicionarCarrinho(mockCliente, mockProduto);

        Assert.assertEquals(1, mockCliente.verCarrinho().size());

        ProdutoCarrinho produtoCarrinho = mockCliente.verCarrinho().get(0);
        Assert.assertEquals(2, (long) produtoCarrinho.getQuantidade());
        Assert.assertEquals((Double) (produtoCarrinho.getQuantidade() * produtoCarrinho.getPreco()),
                produtoCarrinho.getPrecoQuantidade());
        clienteDao.listaElementos().clear();
        produtoDao.listaElementos().clear();
    }

    @Test
    public void adicionarProdutosMistosCarrinhoExpectSuccess() throws ObjetoNaoEncontradoException {
        clienteDao.cadastrar(mockCliente);
        produtoDao.cadastrar(mockProduto);
        produtoDao.cadastrar(mockProduto2);

        genericFactory.adicionarCarrinho(mockCliente, mockProduto);
        genericFactory.adicionarCarrinho(mockCliente, mockProduto);
        genericFactory.adicionarCarrinho(mockCliente, mockProduto2);

        Assert.assertEquals(2, mockCliente.verCarrinho().size());

        List<ProdutoCarrinho> produtosRepetidos = mockCliente.verCarrinho().stream()
                .filter(produto -> Objects.equals(produto.getNome(), mockProduto.getNome())).toList();
        Assert.assertEquals(2, (long) produtosRepetidos.get(0).getQuantidade());

        List<ProdutoCarrinho> produtoUnico = mockCliente.verCarrinho().stream()
                .filter(produto -> Objects.equals(produto.getNome(), mockProduto2.getNome())).toList();
        Assert.assertEquals(1, (long) produtoUnico.get(0).getQuantidade());
        clienteDao.listaElementos().clear();
        produtoDao.listaElementos().clear();
    }
}
