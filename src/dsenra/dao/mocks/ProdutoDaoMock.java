package dsenra.dao.mocks;

import dsenra.dao.IProdutoDao;
import dsenra.domain.Produto;

import java.util.Collection;
import java.util.Map;

public class ProdutoDaoMock implements IProdutoDao {

    @Override
    public boolean cadastrar(Produto produto) {
        return true;
    }

    @Override
    public Produto buscar(Produto produtoRecebido) {
        return produtoRecebido;
    }

    @Override
    public boolean excluir(Produto produto) {
        return true;
    }

    @Override
    public void atualizar(Produto objeto1, Produto objeto2) {

    }

    @Override
    public Collection<Produto> listaElementos() {
        return null;
    }
}
