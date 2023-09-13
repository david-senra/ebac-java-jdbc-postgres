package dsenra.domain;

import dsenra.annotations.ColunaTabela;
import dsenra.annotations.Tabela;

import java.math.BigDecimal;

@Tabela("tb_produto_quantidade")
public class ProdutoQuantidade {

    @ColunaTabela(dbName = "id", setJavaName = "setId")
    private Long id;

    //@ColunaTabela(dbName = "id", setJavaName = "setId")
    private Produto produto;

    @ColunaTabela(dbName = "quantidade", setJavaName = "setQuantidade")
    private Integer quantidade;

    @ColunaTabela(dbName = "valor_total", setJavaName = "setValorTotal")
    private BigDecimal valorTotal;

    public ProdutoQuantidade () {
        this.quantidade = 0;
        this.valorTotal = BigDecimal.ZERO;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal (BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void adicionar(Integer quantidade) {
        if (quantidade >= produto.estoque) {
            throw new UnsupportedOperationException("Estoque insuficiente!");
        }
        this.quantidade += quantidade;
        BigDecimal novoValor = this.produto.getPreco().multiply(BigDecimal.valueOf(quantidade));
        this.valorTotal = this.valorTotal.add(novoValor);
    }

    public void remover(Integer quantidade) {
        this.quantidade -= quantidade;
        BigDecimal novoValor = this.produto.getPreco().multiply(BigDecimal.valueOf(quantidade));
        this.valorTotal = this.valorTotal.subtract(novoValor);
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
