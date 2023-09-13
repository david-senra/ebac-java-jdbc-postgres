package dsenra.Produtos;

import dsenra.dao.ProdutoDao;
import dsenra.domain.Produto;
import dsenra.domain.mock.MockProduto;
import dsenra.exception.DaoException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class ProdutoTest {


    private ProdutoDao produtoDao;
    private Produto mockProduto;
    private Produto mockProduto2;
    private Produto mockProdutoIdRepetido;
    private Produto mockProdutoRepetido;
    private Produto mockProdutoIdDiferenteTudoRepetido;

    @Before
    public void init() throws Exception {
        produtoDao = new ProdutoDao();
        MockProduto mock = new MockProduto();
        mockProduto = mock.getMockProduto();
        mockProduto2 = mock.getMockProdutoNaoCadastrado();
        mockProdutoIdRepetido = mock.getMockProdutoIdRepetido(mockProduto);
        mockProdutoIdRepetido.setId(mockProduto.getId());
        mockProdutoRepetido = mock.getMockProdutoIdRepetido(mockProduto);
        mockProdutoIdDiferenteTudoRepetido = mock.getMockProdutoIdDiferenteTudoRepetido(mockProduto);
    }

    @After
    public void end() throws Exception {
        produtoDao.limparProdutos();
    }

    @Test
    public void cadastrarProdutoNovoExpectSuccess() throws Exception {
        boolean countCadastrar = produtoDao.cadastrar(mockProduto);
        Assert.assertTrue(countCadastrar);
        Produto produtoEncontrado = produtoDao.buscar(mockProduto.getId());
        Assert.assertNotNull(produtoEncontrado);
        Assert.assertEquals(produtoEncontrado.getDescricao(), mockProduto.getDescricao());
        Assert.assertEquals(produtoEncontrado.getNome(), mockProduto.getNome());
    }

    @Test(expected = DaoException.class)
    public void cadastrarClienteIdJaExistenteExpectError() throws Exception {
        boolean countCadastrar = produtoDao.cadastrar(mockProduto);
        Assert.assertTrue(countCadastrar);
        if (mockProdutoIdRepetido.getId().equals(mockProdutoRepetido.getId())) {
            produtoDao.cadastrar(mockProdutoIdRepetido);
        }
    }

    @Test
    public void cadastrarClienteIdDiferenteMesmoNomeExpectSuccess() throws Exception {
        boolean countCadastrar = produtoDao.cadastrar(mockProduto);
        Assert.assertTrue(countCadastrar);
        boolean countCadastrar2 = produtoDao.cadastrar(mockProdutoIdDiferenteTudoRepetido);
        Assert.assertTrue(countCadastrar2);
        Produto clienteEncontrado = produtoDao.buscar(mockProduto.getId());
        Assert.assertNotNull(clienteEncontrado);
        Assert.assertEquals(clienteEncontrado.getDescricao(), mockProduto.getDescricao());
        Assert.assertEquals(clienteEncontrado.getNome(), mockProduto.getNome());
        Produto clienteEncontrado2 = produtoDao.buscar(mockProdutoIdDiferenteTudoRepetido.getId());
        Assert.assertNotNull(clienteEncontrado2);
        Assert.assertEquals(clienteEncontrado2.getDescricao(), mockProdutoIdDiferenteTudoRepetido.getDescricao());
        Assert.assertEquals(clienteEncontrado2.getNome(), mockProdutoIdDiferenteTudoRepetido.getNome());
        produtoDao.excluir(mockProduto.getId());
        List<Produto> listaClientes = (List<Produto>) produtoDao.listaElementos();
        Assert.assertNotNull(listaClientes);
        Assert.assertEquals(1, listaClientes.size());
        produtoDao.excluir(mockProdutoIdDiferenteTudoRepetido.getId());
        listaClientes = (List<Produto>) produtoDao.listaElementos();
        Assert.assertNotNull(listaClientes);
        Assert.assertEquals(0, listaClientes.size());
    }

    @Test
    public void buscarTodosTest() throws Exception {
        boolean countCadastrar = produtoDao.cadastrar(mockProduto);
        Assert.assertTrue(countCadastrar);
        boolean countCadastrar2 = produtoDao.cadastrar(mockProduto2);
        Assert.assertTrue(countCadastrar2);
        List<Produto> listaProdutos = (List<Produto>) produtoDao.listaElementos();
        Assert.assertNotNull(listaProdutos);
        Assert.assertEquals(2, listaProdutos.size());
        listaProdutos.forEach(cliente -> Assert.assertTrue(cliente.getNome().equals(mockProduto.getNome())
                || cliente.getNome().equals(mockProduto2.getNome())));
    }

    @Test
    public void atualizarTest() throws Exception {
        boolean countCadastrar = produtoDao.cadastrar(mockProduto);
        Assert.assertTrue(countCadastrar);

        Produto produtoEncontrado = produtoDao.buscar(mockProduto.getId());
        Assert.assertNotNull(produtoEncontrado);
        Assert.assertEquals(produtoEncontrado.getNome(), mockProduto.getNome());
        Assert.assertEquals(produtoEncontrado.getDescricao(), mockProduto.getDescricao());
        Assert.assertEquals(produtoEncontrado.getPreco().stripTrailingZeros(), mockProduto.getPreco().stripTrailingZeros());

        produtoEncontrado.setNome("Mario Bros");
        produtoEncontrado.setDescricao("Vulgo Luigi");
        produtoEncontrado.setPreco(BigDecimal.valueOf(99.99));
        produtoDao.atualizar(produtoEncontrado);

        Produto produtoEncontrado2 = produtoDao.buscar(produtoEncontrado.getId());
        Assert.assertNotNull(produtoEncontrado2);
        Assert.assertEquals(produtoEncontrado.getNome(), produtoEncontrado2.getNome());
        Assert.assertEquals(produtoEncontrado.getDescricao(), produtoEncontrado2.getDescricao());
        Assert.assertEquals(produtoEncontrado.getPreco(), produtoEncontrado2.getPreco());
    }
}
