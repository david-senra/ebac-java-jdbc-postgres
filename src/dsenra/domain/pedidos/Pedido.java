package dsenra.domain.pedidos;

import java.util.Random;

public class Pedido {
    private final Long numeroPedido;
    private final String codigoPedido;
    private final String diaPedido;
    private final String horaPedido;
    private final String listagemPedidos;

    public Pedido(int numeroPedido, String diaPedido, String horaPedido, Carrinho carrinho) {
        this.codigoPedido = setCodigoPedido();
        this.numeroPedido = (long) numeroPedido;
        this.diaPedido = diaPedido;
        this.horaPedido = horaPedido;
        this.listagemPedidos = setListagemPedidos(carrinho);
    }

    private String setCodigoPedido() {
        StringBuilder pedido = new StringBuilder("#");
        for (int i = 0; i < 7; i++) {
            pedido.append(new Random().nextLong(9));
        }
        return pedido.toString();
    }

    private String setListagemPedidos(Carrinho carrinho) {
        final String[] stringFrase = {null};
        carrinho.getListaProdutos().forEach(produto -> stringFrase[0] += produto.getNome()
                                + " - " + "qty: " + produto.getQuantidade() + "x " + " Preço: "
                                + produto.getPreco() + ", Total: " + produto.getPrecoQuantidade());
        return stringFrase[0];
    }

    public String toString() {
        return numeroPedido.toString() + ")" + "PEDIDO NÚMERO: " + codigoPedido
                + "\nData e Hora: " + diaPedido + " às " + horaPedido
                + listagemPedidos;
    }
}
