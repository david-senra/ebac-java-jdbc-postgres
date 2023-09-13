package dsenra.domain;

import dsenra.annotations.ColunaTabela;
import dsenra.annotations.Tabela;
import dsenra.annotations.TipoChave;

import java.math.BigDecimal;

@Tabela("tb_produto")
public class Produto implements IPersistente {

    @TipoChave("getId")
    @ColunaTabela(dbName = "id", setJavaName = "setId")
    protected Long codigo;

    @ColunaTabela(dbName = "nome", setJavaName = "setNome")
    protected String nome;

    @ColunaTabela(dbName = "descricao", setJavaName = "setDescricao")
    protected String descricao;

    @ColunaTabela(dbName = "preco", setJavaName = "setPreco")
    protected BigDecimal preco;

    @ColunaTabela(dbName = "estoque", setJavaName = "setEstoque")
    protected Integer estoque;

    public Produto() {

    }

    public Produto(Long codigo, String nome, String descricao, BigDecimal preco, Integer estoque) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    @Override
    public Long getId() {
        return codigo;
    }

    @Override
    public void setId(Long codigo) {
        this.codigo = codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }
}
