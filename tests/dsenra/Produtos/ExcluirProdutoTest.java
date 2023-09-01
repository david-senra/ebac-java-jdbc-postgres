package dsenra.Produtos;

import dsenra.dao.ProdutoDao;
import dsenra.domain.Produto;
import dsenra.domain.mock.MockProduto;
import dsenra.exception.ObjetoNaoEncontradoException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExcluirProdutoTest {

    private ProdutoDao produtoDao;
    private Produto mockProduto;
    private Produto mockProdutoAusente;

    @Before
    public void init() {
        produtoDao = new ProdutoDao();
        MockProduto mock = new MockProduto();
        mockProduto = mock.getMockProduto();
        mockProdutoAusente = mock.getMockProdutoNaoCadastrado();
    }
    @Test
    public void excluirClienteExistenteExpectSuccess() throws ObjetoNaoEncontradoException {
        produtoDao.cadastrar(mockProduto);
        Assert.assertTrue(produtoDao.listaElementos().contains(mockProduto));

        produtoDao.excluir(mockProduto);
        Assert.assertFalse(produtoDao.listaElementos().contains(mockProduto));
    }

    @Test (expected = RuntimeException.class)
    public void excluirClienteNaoExistenteExpectSuccess() throws ObjetoNaoEncontradoException {
        produtoDao.cadastrar(mockProduto);
        Assert.assertTrue(produtoDao.listaElementos().contains(mockProduto));

        produtoDao.excluir(mockProdutoAusente);
    }
}
