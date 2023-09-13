package dsenra.dao;

import dsenra.JDBC.ConnectionFactory;
import dsenra.dao.generic.GenericDao;
import dsenra.domain.Produto;
import dsenra.exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

public class ProdutoDao extends GenericDao<Produto, Long> implements IProdutoDao {

    public ProdutoDao() {
        super();
    }

    @Override
    public Class<Produto> getTipoClasse() {
        return Produto.class;
    }

    @Override
    public void atualizarDados(Produto entity, Produto entityCadastrado) {
        entityCadastrado.setNome(entity.getNome());
        entityCadastrado.setDescricao(entity.getDescricao());
        entityCadastrado.setPreco(entity.getPreco());
    }

    @Override
    protected String getQueryInsercao() {
        return "insert into tb_produto (id, codigo, nome, descricao, preco, estoque) " +
                "values (nextval('sq_produto'), ?, ?, ?, ?, ?) ";
    }

    @Override
    protected void setParametrosQueryInsercao(PreparedStatement stmInsert, Produto entity) throws SQLException {
        stmInsert.setLong(1, entity.getId());
        stmInsert.setString(2, entity.getNome());
        stmInsert.setString(3, entity.getDescricao());
        stmInsert.setBigDecimal(4, entity.getPreco());
        stmInsert.setInt(5, entity.getEstoque());
    }

    @Override
    protected String getQueryAtualizacao() {
        return "update tb_produto " +
                "set nome = ?, " +
                "descricao = ?, " +
                "preco = ? " +
                "where id = ?";
    }

    @Override
    protected void setParametrosQueryAtualizacao(PreparedStatement stmUpdate, Produto entity) throws SQLException {
        stmUpdate.setString(1, entity.getNome());
        stmUpdate.setString(2, entity.getDescricao());
        stmUpdate.setBigDecimal(3, entity.getPreco());
        stmUpdate.setLong(4, entity.getId());
    }

    @Override
    protected String getQueryExclusao() {
        return "delete from tb_produto where id = ?";
    }

    @Override
    protected void setParametrosQueryExclusao(PreparedStatement stmDelete, Long valor) throws SQLException {
        stmDelete.setLong(1, valor);
    }

    @Override
    protected void setParametrosQuerySelect(PreparedStatement stmSelect, Long valor) throws SQLException {
        stmSelect.setLong(1, valor);
    }

    public void limparProdutos() throws Exception {
        listaElementos().forEach(prod -> {
            try {
                excluir(prod.getId());
            } catch (DaoException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
