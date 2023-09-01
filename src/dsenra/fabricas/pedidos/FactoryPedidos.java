package dsenra.fabricas.pedidos;

import dsenra.domain.Cliente;
import dsenra.domain.Produto;
import dsenra.domain.pedidos.ProdutoFisico;

import java.util.List;

public class FactoryPedidos implements IFactoryPedidos {
    @Override
    public void adicionarCarrinho(Cliente cliente, Produto produto) {
        cliente.adicionarProduto(produto);
    }

    @Override
    public void removerCarrinho(Cliente cliente, Produto produto) {
        Produto produtoEncontrado = cliente.buscarNoCarrinho(produto);
        if (produtoEncontrado == null) throw new RuntimeException("Produto não existe no carrinho!");
        else cliente.removerProduto(produto);
    }

    @Override
    public void limparCarrinho(Cliente cliente) {
        cliente.limparCarrinho();
    }

    @Override
    public List<ProdutoFisico> mostrarCarrinho(Cliente cliente) {
        return cliente.verCarrinho();
    }

    @Override
    public void finalizarPedido(Cliente cliente) {
        cliente.adicionarPedido();
    }

    @Override
    public void mostrarPedidos(Cliente cliente) {

    }
}
