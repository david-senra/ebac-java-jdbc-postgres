package dsenra.fabricas.pedidos;

import dsenra.domain.Cliente;
import dsenra.domain.Produto;
import dsenra.domain.pedidos.ProdutoCarrinho;

import java.util.List;

public interface IFactoryPedidos {
    void adicionarCarrinho(Cliente cliente, Produto produto);
    void removerCarrinho(Cliente cliente, Produto produto);
    void limparCarrinho(Cliente cliente);
    List<ProdutoCarrinho> mostrarCarrinho(Cliente cliente);
    void finalizarPedido(Cliente cliente);
    void mostrarPedidos(Cliente cliente);
}
