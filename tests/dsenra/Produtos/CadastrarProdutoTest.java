package dsenra.Produtos;

import dsenra.dao.ProdutoDao;
import dsenra.domain.Produto;
import dsenra.domain.mock.MockProduto;
import dsenra.exception.ObjetoNaoEncontradoException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CadastrarProdutoTest {

    private ProdutoDao produtoDao;
    private Produto mockProduto;
    private Produto mockProdutoRepetido;
    private Produto mockProdutoIdDiferenteTudoRepetido;

    @Before
    public void init() {
        produtoDao = new ProdutoDao();
        produtoDao.listaElementos().clear();
        produtoDao = new ProdutoDao();
        MockProduto mock = new MockProduto();
        mockProduto = mock.getMockProduto();
        mockProdutoRepetido = mock.getMockProdutoIdRepetido(mockProduto);
        mockProdutoIdDiferenteTudoRepetido = mock.getMockProdutoIdDiferenteTudoRepetido(mockProduto);
    }
    @Test
    public void cadastrarProdutoNovoExpectSuccess() throws ObjetoNaoEncontradoException {
        produtoDao.cadastrar(mockProduto);
        Assert.assertTrue(produtoDao.listaElementos().contains(mockProduto));
        produtoDao.listaElementos().clear();
    }

    @Test(expected = RuntimeException.class)
    public void cadastrarProdutoJaExistenteExpectError() throws ObjetoNaoEncontradoException {
        produtoDao.cadastrar(mockProduto);
        if (mockProduto.getId().equals(mockProdutoRepetido.getId())) produtoDao.cadastrar(mockProdutoRepetido);
        produtoDao.listaElementos().clear();
    }

    @Test
    public void cadastrarProdutoIdDiferenteMesmoNomeExpectSuccess() throws ObjetoNaoEncontradoException {
        produtoDao.cadastrar(mockProduto);
        produtoDao.cadastrar(mockProdutoIdDiferenteTudoRepetido);

        Assert.assertTrue(produtoDao.listaElementos().contains(mockProduto));
        Assert.assertTrue(produtoDao.listaElementos().contains(mockProdutoIdDiferenteTudoRepetido));
        produtoDao.listaElementos().clear();
    }
}
