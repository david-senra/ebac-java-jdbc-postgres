package dsenra.Fabricas;

import dsenra.dao.IProdutoDao;
import dsenra.dao.mocks.ProdutoDaoMock;
import dsenra.domain.Produto;
import dsenra.fabricas.FactoryProdutos;
import dsenra.fabricas.IFactoryProdutos;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProdutoFactoryTest {
    private final IFactoryProdutos produtoFactory;
    private Produto produto;

    public ProdutoFactoryTest() {
        IProdutoDao produtoDao = new ProdutoDaoMock();
        produtoFactory = new FactoryProdutos(produtoDao);
    }

    @Before
    public void init() {
        produto = new Produto();
        produto.setNome("Xícara");
        produto.setDescricao("Uma xícara para um bom café");
        produto.setPreco(14.90);
    }

    @Test
    public void CadastrarClienteExpectsSuccess() {
        boolean retorno = produtoFactory.cadastrar(produto);
        Assert.assertTrue(retorno);
    }

}
