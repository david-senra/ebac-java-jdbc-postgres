package dsenra.domain.pedidos;

import dsenra.domain.Produto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Carrinho {
    private final Map<Long, ProdutoCarrinho> listaProdutos = new HashMap<>();
    private BigDecimal precoTotal;

    public void adicionarProduto(Produto produto) {
        ProdutoCarrinho produtoEncontrado = listaProdutos.get(produto.getId());
        if (produtoEncontrado == null) listaProdutos.put(produto.getId(), new ProdutoCarrinho(produto));
        else listaProdutos.get(produto.getId()).aumentaQuantidade();
    }

    public void removerProduto(Produto produto) {
        ProdutoCarrinho produtoEncontrado = listaProdutos.get(produto.getId());
        if (produtoEncontrado.getQuantidade() > 1) listaProdutos.get(produto.getId()).reduzQuantidade();
        else listaProdutos.remove(produto.getId());
    }

    public Long getQuantityProduto(Produto produto) {
        ProdutoCarrinho produtoEncontrado = listaProdutos.get(produto.getId());
        return produtoEncontrado.getQuantidade();
    }

    public BigDecimal getPrecoTotalProduto(Produto produto) {
        ProdutoCarrinho produtoEncontrado = listaProdutos.get(produto.getId());
        return produtoEncontrado.getPrecoQuantidade();
    }

    public BigDecimal getPrecoTotal() {
        precoTotal = BigDecimal.valueOf(0d);
        listaProdutos.forEach((key, produto) -> precoTotal.add(produto.getPrecoQuantidade()));
        return precoTotal;
    }

    public List<ProdutoCarrinho> getListaProdutos() {
        return listaProdutos.values().stream().toList();
    }

    public void limparCarrinho() {
        listaProdutos.clear();
    }
}
