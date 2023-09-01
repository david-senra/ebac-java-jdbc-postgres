package dsenra.domain;

import dsenra.domain.pedidos.Carrinho;
import dsenra.domain.pedidos.Pedido;
import dsenra.domain.pedidos.ProdutoCarrinho;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Cliente implements IPersistente {
    protected Long id;
    private String nome;
    private String sobrenome;
    private String cpf;
    private String telefone;
    private String endereco;
    private Long numeroEndereco;
    private String bairro;
    private String cidade;
    private String estado;
    private final Carrinho carrinho = new Carrinho();
    private final List<Pedido> listaPedidos = new ArrayList<>();

    public Cliente () {

    }

    public Cliente (Long id, String nome, String sobrenome, String cpf, String telefone, String endereco, String cidade, String estado) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.endereco = endereco;
        this.cidade = cidade;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    @Override
    public Cliente getObjectData() {
        return this;
    }

    public String getNome() {
        return nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public Long getNumeroEndereco() {
        return numeroEndereco;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setNumeroEndereco(Long numeroEndereco) {
        this.numeroEndereco = numeroEndereco;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<ProdutoCarrinho> verCarrinho() {
        if (carrinho.getListaProdutos().size() > 0) return carrinho.getListaProdutos();
        else return null;
    }

    public ProdutoCarrinho buscarNoCarrinho(Produto produto) {
        Stream<ProdutoCarrinho> resultadoBusca = carrinho.getListaProdutos().stream()
                .filter(produtoFisico -> produtoFisico.getNome().equals(produto.getNome()));
        return resultadoBusca.toList().get(0);
    }

    public void limparCarrinho() {
        carrinho.limparCarrinho();
    }

    public void adicionarProduto(Produto produto) {
        carrinho.adicionarProduto(produto);
    }

    public void removerProduto(Produto produto) {
        carrinho.removerProduto(produto);
    }

    public void adicionarPedido() {
        listaPedidos.add(new Pedido(listaPedidos.size(), "Hoje", "Agora", carrinho));
        carrinho.limparCarrinho();
    }
}
