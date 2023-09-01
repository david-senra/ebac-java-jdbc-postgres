package dsenra.Servicos;

import dsenra.dao.*;
import dsenra.domain.Produto;
import dsenra.domain.IPersistente;
import dsenra.fabricas.FactoryProdutos;
import dsenra.services.generic.GenericService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProdutoServiceTest<T extends IPersistente> {
    private Produto produto;
    private final ProdutoDao produtoDao = new ProdutoDao();
    private final GenericService<T> service;
    private FactoryProdutos factoryProdutos;

    public ProdutoServiceTest() {
        service = new GenericService<>();
    }

    @Before
    public void init() {
        produtoDao.listaElementos().clear();
        factoryProdutos = new FactoryProdutos(produtoDao);
        produto = null;
        produto = new Produto(1L,
                "Lápis de Cor",
                "Precisamos escrever nessa vida.",
                2.80);
    }

    @Test
    public void BuscarProdutoExpectsSuccess() {
        factoryProdutos.cadastrar(produto);
        Produto produtoConsultado = (Produto) service.buscar((T) produto);
        Assert.assertNotNull(produtoConsultado);
        produto = null;
        produtoDao.listaElementos().clear();
    }

    @Test
    public void AtualizarProdutoExpectsSuccess() {
        factoryProdutos.cadastrar(produto);
        Produto produtoConsultado = (Produto) service.buscar((T) produto);
        boolean retorno = service.atualizar((T) produtoConsultado, (T) produtoConsultado);
        Assert.assertTrue(retorno);
    }
}
