package dsenra;

import dsenra.Fabricas.ClientFactoryTest;
import dsenra.Fabricas.ProdutoFactoryTest;
import dsenra.Pedidos.AdicionarCarrinhoTest;
import dsenra.Pedidos.LimparCarrinhoTest;
import dsenra.Pedidos.RemoverCarrinhoTest;
import dsenra.Pedidos.VerCarrinhoVazioTest;
import dsenra.Servicos.ClientServiceTest;
import dsenra.Servicos.ProdutoServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ AdicionarCarrinhoTest.class, RemoverCarrinhoTest.class, LimparCarrinhoTest.class, VerCarrinhoVazioTest.class,
                    ClientServiceTest.class, ProdutoServiceTest.class,
                    ClientFactoryTest.class, ProdutoFactoryTest.class})
public class AllTests {
}
