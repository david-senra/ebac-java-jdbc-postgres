package dsenra.domain.pedidos;

import dsenra.domain.Produto;

public class ProdutoCarrinho extends Produto {
    private Long quantidade = 1L;

    private final Produto produto;

    public ProdutoCarrinho(Produto produto) {
        this.codigo = produto.getId();
        this.nome = produto.getNome();
        this.descricao = produto.getDescricao();
        this.preco = produto.getPreco();
        this.produto = produto;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public Double getPrecoQuantidade() {
        return getQuantidade() * getPreco();
    }

    public Produto getProduto() {
        return produto;
    }

    public void aumentaQuantidade() {
        quantidade++;
    }

    public void reduzQuantidade() {
        quantidade--;
    }
}
