package dsenra.domain;

import dsenra.annotations.ColunaTabela;


public class ProdutoEstoque {

    @ColunaTabela(dbName = "id", setJavaName = "setId")
    private Long id;

    private Produto produto;

    @ColunaTabela(dbName = "quantidade", setJavaName = "setQuantidade")
    private Integer estoque;

    public ProdutoEstoque () {
        this.estoque = 0;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }



    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }


    public void adicionarEstoque(Integer qtdAdicionar) {
        estoque += qtdAdicionar;
    }

    public void reduzirEstoque(Integer qtdReduzir) {
        estoque -= qtdReduzir;
    }
}
