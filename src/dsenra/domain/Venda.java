package dsenra.domain;

import dsenra.annotations.ColunaTabela;
import dsenra.annotations.Tabela;
import dsenra.annotations.TipoChave;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Tabela("tb_venda")
public class Venda implements IPersistente {

    public enum Status {
        INICIADA, CONCLUIDA, CANCELADA;

        public static Status getByName(String value) {
            for (Status status: Status.values()) {
                if (status.name().equals(value)) {
                    return status;
                }
            }
            return null;
        }
    }

    @ColunaTabela(dbName = "id", setJavaName = "setId")
    private Long id;

    @TipoChave("getCodigo")
    @ColunaTabela(dbName = "codigo", setJavaName = "setCodigo")
    private String codigo;

    @ColunaTabela(dbName = "id_cliente_fk", setJavaName = "setIdClienteFk")
    private Cliente cliente;

    //@ColunaTabela(dbName = "id", setJavaName = "setId")
    private Set<ProdutoQuantidade> produtos;

    private Set<ProdutoEstoque> estoqueProdutos;

    @ColunaTabela(dbName = "valor_total", setJavaName = "setValorTotal")
    private BigDecimal valorTotal;

    @ColunaTabela(dbName = "data_venda", setJavaName = "setDataVenda")
    private Instant dataVenda;

    @ColunaTabela(dbName = "status_venda", setJavaName = "setStatus")
    private Status status;

    public Venda() {
        produtos = new HashSet<>();
        estoqueProdutos = new HashSet<>();
    }

    public void adicionarProduto(Produto produto, Integer quantidade) {
        validarStatus();
        Optional<ProdutoQuantidade> op =
                produtos.stream().filter(filter -> filter.getProduto().getId().equals(produto.getId())).findAny();
        Optional<ProdutoEstoque> prodest =
                estoqueProdutos.stream().filter(filter -> filter.getProduto().getId().equals(produto.getId())).findAny();
        if (op.isPresent() && prodest.isPresent()) {
            ProdutoQuantidade produtoQtd = op.get();
            produtoQtd.adicionar(quantidade);
            ProdutoEstoque produtoEst = prodest.get();
            produtoEst.reduzirEstoque(quantidade);
        } else {
            ProdutoQuantidade prod = new ProdutoQuantidade();
            ProdutoEstoque est = new ProdutoEstoque();
            prod.setProduto(produto);
            prod.adicionar(quantidade);
            est.setProduto(produto);
            est.setEstoque(produto.getEstoque());
            est.reduzirEstoque(quantidade);
            produtos.add(prod);
            estoqueProdutos.add(est);
        }
        recalcularValorTotalVenda();
    }

    public void removerProduto(Produto produto, Integer quantidade) {
        validarStatus();
        Optional<ProdutoQuantidade> op =
                produtos.stream().filter(filter -> filter.getProduto().getId().equals(produto.getId())).findAny();
        Optional<ProdutoEstoque> prodest =
                estoqueProdutos.stream().filter(filter -> filter.getProduto().getId().equals(produto.getId())).findAny();

        if (op.isPresent() && prodest.isPresent()) {
            ProdutoQuantidade produtoQtd = op.get();
            ProdutoEstoque produtoEst = prodest.get();
            if (produtoQtd.getQuantidade()>quantidade) {
                produtoQtd.remover(quantidade);
                produtoEst.adicionarEstoque(quantidade);
                recalcularValorTotalVenda();
            } else {
                produtos.remove(op.get());
                estoqueProdutos.remove(prodest.get());
                recalcularValorTotalVenda();
            }

        }
    }

    private void validarStatus() {
        if (this.status == Status.CONCLUIDA) {
            throw new UnsupportedOperationException("IMPOSSÍVEL ALTERAR VENDA FINALIZADA");
        }
    }

    public void recalcularValorTotalVenda() {
        //validarStatus();
        BigDecimal valorTotal = BigDecimal.ZERO;
        for (ProdutoQuantidade prod : this.produtos) {
            valorTotal = valorTotal.add(prod.getValorTotal());
        }
        this.valorTotal = valorTotal;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void removerTodosProdutos() {
        validarStatus();
        produtos.clear();
        valorTotal = BigDecimal.ZERO;
    }

    public Integer getQuantidadeTotalProdutos() {
        // Soma a quantidade getQuantidade() de todos os objetos ProdutoQuantidade
        return produtos.stream()
                .reduce(0, (partialCountResult, prod) -> partialCountResult + prod.getQuantidade(), Integer::sum);
    }

    public void setProdutos(Set<ProdutoQuantidade> produtos) {
        this.produtos = produtos;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Set<ProdutoQuantidade> getProdutos() {
        return produtos;
    }

    public Set<ProdutoEstoque> getEstoqueProdutos() {
        return estoqueProdutos;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public Instant getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Instant dataTime) {
        this.dataVenda = dataTime;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
