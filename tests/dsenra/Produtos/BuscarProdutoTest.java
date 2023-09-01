package dsenra.Produtos;

import dsenra.dao.ProdutoDao;
import dsenra.domain.Produto;
import dsenra.domain.mock.MockProduto;
import dsenra.exception.ObjetoNaoEncontradoException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BuscarProdutoTest {

    private ProdutoDao produtoDao;
    private Produto mockProduto;
    private Produto mockProdutoAusente;

    @Before
    public void init() {
        produtoDao = new ProdutoDao();
        produtoDao.listaElementos().clear();
        produtoDao = new ProdutoDao();
        MockProduto mock = new MockProduto();
        mockProduto = mock.getMockProduto();
        mockProdutoAusente = mock.getMockProdutoNaoCadastrado();
    }
    @Test
    public void buscarProdutoExistenteExpectSuccess() throws ObjetoNaoEncontradoException {
        produtoDao.cadastrar(mockProduto);
        Produto produtoEncontradoMock = produtoDao.buscar(mockProduto);
        Assert.assertTrue(produtoDao.listaElementos().contains(produtoEncontradoMock));
        produtoDao.listaElementos().clear();
    }

    @Test(expected = RuntimeException.class)
    public void buscarProdutoNaoExistenteExpectNull() throws ObjetoNaoEncontradoException {
        produtoDao.cadastrar(mockProduto);
        Produto produtoEncontradoMock = produtoDao.buscar(mockProdutoAusente);
        Assert.assertNull(produtoEncontradoMock);
        produtoDao.listaElementos().clear();
    }
}
