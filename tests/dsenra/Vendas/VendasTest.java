package dsenra.Vendas;

import dsenra.dao.ClienteDao;
import dsenra.dao.ProdutoDao;
import dsenra.dao.VendaDao;
import dsenra.domain.Cliente;
import dsenra.domain.Produto;
import dsenra.domain.Venda;
import dsenra.domain.mock.MockCliente;
import dsenra.domain.mock.MockProduto;
import dsenra.domain.mock.MockVenda;
import dsenra.exception.DaoException;
import dsenra.exception.MaisDeUmRegistroException;
import dsenra.exception.TableException;
import dsenra.exception.TipoChaveNaoEncontradaException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class VendasTest {


    private ClienteDao clienteDao;
    private Cliente mockCliente;
    private Cliente mockCliente2;
    private Cliente mockClienteIdRepetido;


    private ProdutoDao produtoDao;
    private Produto mockProduto;
    private Produto mockProduto2;
    private Produto mockProdutoIdRepetido;

    private VendaDao vendaDao;

    private MockVenda mockVe = new MockVenda();
    private Venda mockVenda;
    private Venda mockVenda2;

    @Before
    public void init() throws Exception {
        produtoDao = new ProdutoDao();
        clienteDao = new ClienteDao();
        vendaDao = new VendaDao();
        MockProduto mockPr = new MockProduto();
        MockCliente mockCl = new MockCliente();
        MockVenda mockVe = new MockVenda();

        mockCliente = mockCl.getMockCliente();
        mockCliente2 = mockCl.getMockClienteNaoCadastrado();
        mockClienteIdRepetido = mockCl.getMockClienteIdRepetido(mockCliente);
        clienteDao.cadastrar(mockCliente);
        clienteDao.cadastrar(mockCliente2);

        mockProduto = mockPr.getMockProduto();
        mockProduto2 = mockPr.getMockProdutoNaoCadastrado();
        mockProdutoIdRepetido = mockPr.getMockProdutoIdRepetido(mockProduto);
        produtoDao.cadastrar(mockProduto);
        produtoDao.cadastrar(mockProduto2);

        mockVenda2 = mockVe.getVenda02();
    }

    @After
    public void end() throws Exception {
        vendaDao.limparProdutosEstoque();
        vendaDao.limparProdutosQuantidade();
        vendaDao.limparVendas();
        clienteDao.limparClientes();
        produtoDao.limparProdutos();

    }

    @Test
    public void CadastrarAlterarNovaVenda() throws TipoChaveNaoEncontradaException, DaoException, MaisDeUmRegistroException, TableException {
        mockVenda = mockVe.getVenda01();
        mockVenda.setCliente(mockCliente);
        Assert.assertEquals(mockVenda.getCliente().getNome(), mockCliente.getNome());
        mockVenda.adicionarProduto(mockProduto, 2);
        mockVenda.adicionarProduto(mockProduto2, 1);

        List<String> listaProdutos = new ArrayList<>();
        mockVenda.getProdutos().forEach(produto -> listaProdutos.add(produto.getProduto().getNome()));
        Assert.assertTrue(listaProdutos.contains(mockProduto.getNome()));
        Assert.assertTrue(listaProdutos.contains(mockProduto2.getNome()));

        vendaDao.cadastrar(mockVenda);

        Venda vendaConsultada = vendaDao.buscar(mockVenda.getCodigo());
        Assert.assertNotNull(vendaConsultada);

        Assert.assertEquals(mockVenda.getCodigo(), vendaConsultada.getCodigo());
        Assert.assertEquals(mockVenda.getValorTotal(),mockProduto.getPreco().multiply(BigDecimal.valueOf(2)).add(mockProduto2.getPreco()));
        Assert.assertEquals(mockVenda.getStatus(), Venda.Status.INICIADA);

        mockVenda.adicionarProduto(mockProduto2, 1);
        Assert.assertEquals(4, (int) mockVenda.getQuantidadeTotalProdutos());
        BigDecimal valorTotalTemporario = mockProduto.getPreco().multiply(BigDecimal.valueOf(2)).add(mockProduto2.getPreco().multiply(BigDecimal.valueOf(2)));
        Assert.assertEquals(mockVenda.getValorTotal(),valorTotalTemporario);

        mockVenda.removerProduto(mockProduto, 1);
        Assert.assertEquals(3, (int) mockVenda.getQuantidadeTotalProdutos());
        BigDecimal valorTotalTemporario2 = mockProduto.getPreco().add(mockProduto2.getPreco().multiply(BigDecimal.valueOf(2)));
        Assert.assertEquals(mockVenda.getValorTotal(),valorTotalTemporario2);

        mockVenda.removerTodosProdutos();
        Assert.assertEquals(0, (int) mockVenda.getQuantidadeTotalProdutos());
        BigDecimal valorZerado = BigDecimal.ZERO;
        Assert.assertEquals(mockVenda.getValorTotal(),valorZerado);
    }

    @Test
    public void CancelarVenda() throws DaoException, TipoChaveNaoEncontradaException {
        mockVenda = mockVe.getVenda01();
        mockVenda.setCliente(mockCliente);
        Assert.assertEquals(mockVenda.getCliente().getNome(), mockCliente.getNome());

        mockVenda.adicionarProduto(mockProduto, 2);
        mockVenda.adicionarProduto(mockProduto2, 1);

        vendaDao.cadastrar(mockVenda);
        Assert.assertEquals(mockVenda.getStatus(), Venda.Status.INICIADA);

        vendaDao.cancelarVenda(mockVenda);
        Assert.assertEquals(mockVenda.getStatus(), Venda.Status.CANCELADA);
    }

    @Test
    public void FinalizarVenda() throws DaoException, TipoChaveNaoEncontradaException {
        mockVenda = mockVe.getVenda01();
        mockVenda.setCliente(mockCliente);
        Assert.assertEquals(mockVenda.getCliente().getNome(), mockCliente.getNome());

        mockVenda.adicionarProduto(mockProduto, 2);
        mockVenda.adicionarProduto(mockProduto2, 1);

        vendaDao.cadastrar(mockVenda);
        Assert.assertEquals(mockVenda.getStatus(), Venda.Status.INICIADA);

        vendaDao.finalizarVenda(mockVenda);
        Assert.assertEquals(mockVenda.getStatus(), Venda.Status.CONCLUIDA);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void AdicionarProdutoSemEstoqueExpectError() {
        mockVenda = mockVe.getVenda01();
        mockVenda.setCliente(mockCliente);
        Assert.assertEquals(mockVenda.getCliente().getNome(), mockCliente.getNome());

        mockVenda.adicionarProduto(mockProduto, 2);
        mockProduto.setEstoque(3); // Artificialmente diminuindo o estoque.
        mockVenda.adicionarProduto(mockProduto, 5); // Adicionando qtd acima do estoque...
    }
}
