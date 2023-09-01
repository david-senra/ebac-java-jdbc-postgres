package dsenra.Produtos;

import dsenra.dao.ProdutoDao;
import dsenra.domain.Produto;
import dsenra.domain.mock.MockProduto;
import dsenra.exception.ObjetoNaoEncontradoException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AtualizarProdutoTest {

    private ProdutoDao produtoDao;
    private Produto mockProduto;
    private Produto mockProdutoMesmosDados;
    private Produto mockProdutoRepetido;
    private Produto mockProdutoNaoCadastrado;


    @Before
    public void init() {
        produtoDao = new ProdutoDao();
        MockProduto mock = new MockProduto();
        mockProduto = mock.getMockProduto();
        mockProdutoNaoCadastrado = mock.getMockProdutoNaoCadastrado();
        mockProdutoMesmosDados = mock.getMockProdutoIdDiferenteTudoRepetido(mockProduto);
        mockProdutoRepetido = mock.getMockProdutoIdRepetido(mockProduto);
    }
    @Test
    public void atualizarProdutoExistenteExpectSuccess() throws ObjetoNaoEncontradoException {
        produtoDao.cadastrar(mockProduto);
        Assert.assertTrue(
                mockProduto.getNome().equals(mockProdutoMesmosDados.getNome()) &&
                        mockProduto.getDescricao().equals(mockProdutoMesmosDados.getDescricao()) &&
                        mockProduto.getPreco().equals(mockProdutoMesmosDados.getPreco())
        );

        mockProdutoRepetido.setNome("Anônimo");
        mockProdutoRepetido.setDescricao("Ninguém sabe o que é esse produto");
        mockProdutoRepetido.setPreco(269.80);
        produtoDao.atualizar(mockProdutoRepetido, mockProduto);

        Produto mockProdutoAlterado = produtoDao.buscar(mockProduto);

        Assert.assertTrue(
                mockProdutoAlterado.getNome().equals("Anônimo") &&
                        mockProdutoAlterado.getDescricao().equals("Ninguém sabe o que é esse produto") &&
                        mockProdutoAlterado.getPreco().equals(269.80)
        );
        Assert.assertEquals(mockProdutoAlterado.getId(), mockProduto.getId());
    }

    @Test(expected = RuntimeException.class)
    public void atualizarProdutoInexistenteExpectError() throws ObjetoNaoEncontradoException {
        produtoDao.cadastrar(mockProduto);

        Assert.assertTrue(
                mockProduto.getNome().equals(mockProdutoMesmosDados.getNome()) &&
                        mockProduto.getDescricao().equals(mockProdutoMesmosDados.getDescricao()) &&
                        mockProduto.getPreco().equals(mockProdutoMesmosDados.getPreco())
        );

        mockProdutoNaoCadastrado.setNome("Anônimo");
        mockProdutoNaoCadastrado.setDescricao("Ninguém sabe do que se trata.");
        mockProdutoNaoCadastrado.setPreco(999.99);
        produtoDao.atualizar(mockProdutoNaoCadastrado, mockProduto);
    }
}
