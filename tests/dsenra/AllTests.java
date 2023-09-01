package dsenra;

import dsenra.Clientes.AtualizarClienteTest;
import dsenra.Clientes.BuscarClienteTest;
import dsenra.Clientes.CadastrarClienteTest;
import dsenra.Clientes.ExcluirClienteTest;
import dsenra.Fabricas.ClientFactoryTest;
import dsenra.Fabricas.ProdutoFactoryTest;
import dsenra.Pedidos.AdicionarCarrinhoTest;
import dsenra.Pedidos.LimparCarrinhoTest;
import dsenra.Pedidos.RemoverCarrinhoTest;
import dsenra.Pedidos.VerCarrinhoVazioTest;
import dsenra.Produtos.AtualizarProdutoTest;
import dsenra.Produtos.BuscarProdutoTest;
import dsenra.Produtos.CadastrarProdutoTest;
import dsenra.Produtos.ExcluirProdutoTest;
import dsenra.Servicos.ClientServiceTest;
import dsenra.Servicos.ProdutoServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ AtualizarClienteTest.class, BuscarClienteTest.class, CadastrarClienteTest.class, ExcluirClienteTest.class,
                    AtualizarProdutoTest.class, BuscarProdutoTest.class, CadastrarProdutoTest.class, ExcluirProdutoTest.class,
                    AdicionarCarrinhoTest.class, RemoverCarrinhoTest.class, LimparCarrinhoTest.class, VerCarrinhoVazioTest.class,
                    ClientServiceTest.class, ProdutoServiceTest.class,
                    ClientFactoryTest.class, ProdutoFactoryTest.class})
public class AllTests {
}
