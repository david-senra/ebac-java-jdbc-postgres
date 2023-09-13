package dsenra.dao;

import dsenra.dao.generic.GenericDao;
import dsenra.domain.ProdutoEstoque;
import dsenra.domain.ProdutoQuantidade;
import dsenra.domain.Venda;
import dsenra.exception.DaoException;
import dsenra.exception.MaisDeUmRegistroException;
import dsenra.exception.TableException;
import dsenra.exception.TipoChaveNaoEncontradaException;
import dsenra.factories.ProdutoQuantidadeFactory;
import dsenra.factories.VendaFactory;

import java.sql.*;
import java.util.*;

import static dsenra.JDBC.ConnectionFactory.getConnection;

public class VendaDao extends GenericDao<Venda, String> implements IVendaDao {

    @Override
    public Class<Venda> getTipoClasse() {
        return Venda.class;
    }

    @Override
    public void excluir(String valor) {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

    @Override
    public boolean cadastrar(Venda entity) throws TipoChaveNaoEncontradaException, DaoException {
        Connection connection = null;
        PreparedStatement stm = null;
        try {
            connection = getConnection();
            stm = connection.prepareStatement(getQueryInsercao(), Statement.RETURN_GENERATED_KEYS);
            setParametrosQueryInsercao(stm, entity);
            int rowsAffected = stm.executeUpdate();

            if(rowsAffected > 0) {
                try (ResultSet rs = stm.getGeneratedKeys()){
                    if (rs.next()) {
                        entity.setId(rs.getLong(1));
                    }
                }

                for (ProdutoQuantidade prod : entity.getProdutos()) {
                    stm = connection.prepareStatement(getQueryInsercaoProdQuant());
                    setParametrosQueryInsercaoProdQuant(stm, entity, prod);
                    stm.executeUpdate();
                }

                for (ProdutoEstoque prodEst : entity.getEstoqueProdutos()) {
                    stm = connection.prepareStatement(getQueryInsercaoEstoque());
                    setParametrosQueryInsercaoEstoque(stm, prodEst);
                    stm.executeUpdate();
                }

                return true;
            }

        } catch (SQLException e) {
            throw new DaoException("erro cadastrando objeto ", e);
        } finally {
            closeConnection(connection, stm, null);
        }
        return false;
    }

    @Override
    protected String getQueryInsercao() {
        return "insert into tb_venda " +
                "(id, codigo, id_cliente_fk, valor_total, data_venda, status_venda) " +
                "values (nextval('sq_venda'), ?, ?, ?, ?, ?)";
    }

    @Override
    protected void setParametrosQueryInsercao(PreparedStatement stmInsert, Venda entity) throws SQLException {
        stmInsert.setString(1, entity.getCodigo());
        stmInsert.setLong(2, entity.getCliente().getId());
        stmInsert.setBigDecimal(3, entity.getValorTotal());
        stmInsert.setTimestamp(4, Timestamp.from(entity.getDataVenda()));
        stmInsert.setString(5, entity.getStatus().name());
    }

    private String getQueryInsercaoProdQuant() {
        return "insert into tb_produto_quantidade " +
                "(id, id_produto_fk, id_venda_fk, quantidade, valor_total)" +
                "values (nextval('sq_produto_quantidade'), ?, ?, ?, ?)";
    }

    private void setParametrosQueryInsercaoProdQuant(PreparedStatement statement, Venda venda, ProdutoQuantidade prod) throws SQLException {
        statement.setLong(1, prod.getProduto().getId());
        statement.setLong(2, venda.getId());
        statement.setInt(3, prod.getQuantidade());
        statement.setBigDecimal(4, prod.getValorTotal());
    }

    private String getQueryInsercaoEstoque() {
        return "insert into tb_estoque " +
                "(id, id_produto_fk, estoque)" +
                "values (nextval('sq_estoque'), ?, ?)";
    }

    private void setParametrosQueryInsercaoEstoque(PreparedStatement statement, ProdutoEstoque prodest) throws SQLException {
        statement.setLong(1, prodest.getProduto().getId());
        statement.setInt(2, prodest.getEstoque());
    }

    @Override
    public Venda buscar(String valor) throws MaisDeUmRegistroException, TableException, DaoException {
        //TODO pode ser feito desta forma
//		Venda venda = super.consultar(valor);
//		Cliente cliente = clienteDAO.consultar(venda.getCliente().getId());
//		venda.setCliente(cliente);
//		return venda;

        //TODO Ou pode ser feito com join
        StringBuilder sb = sqlBaseSelect();
        sb.append("where v.codigo = ? ");
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            //validarMaisDeUmRegistro();
            connection = getConnection();
            stm = connection.prepareStatement(sb.toString());
            setParametrosQuerySelect(stm, valor);
            rs = stm.executeQuery();
            if (rs.next()) {
                Venda venda = VendaFactory.convert(rs);
                buscarAssociacaoVendaProdutos(connection, venda);
                return venda;
            }

        } catch (SQLException e) {
            throw new DaoException("erro buscando objeto ", e);
        } finally {
            closeConnection(connection, stm, rs);
        }
        return null;

    }

    @Override
    public Collection<Venda> listaElementos() throws DaoException {
        List<Venda> lista = new ArrayList<>();
        StringBuilder sb = sqlBaseSelect();

        try {
            Connection connection = getConnection();
            PreparedStatement stm = connection.prepareStatement(sb.toString());
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Venda venda = VendaFactory.convert(rs);
                buscarAssociacaoVendaProdutos(connection, venda);
                lista.add(venda);
            }

        } catch (SQLException e) {
            throw new DaoException("erro buscando objetos ", e);
        }
        return lista;
    }

    private StringBuilder sqlBaseSelect() {
        StringBuilder sb = new StringBuilder();
        sb.append("select v.id as id_venda, v.codigo, v.valor_total, v.data_venda, v.status_venda, ");
        sb.append("c.id as id_cliente, c.nome, c.sobrenome, c.cpf, c.telefone, c.endereco, c.numero_endereco, c.cidade, c.estado ");
        sb.append("from tb_venda v ");
        sb.append("inner join tb_cliente c on v.id_cliente_fk = c.id ");
        return sb;
    }

    private void buscarAssociacaoVendaProdutos(Connection connection, Venda venda)
            throws DaoException {
        PreparedStatement stmProd = null;
        ResultSet rsProd = null;
        try {
            String sbProd = "select pq.id, pq.quantidade, pq.valor_total, " +
                    "p.id as id_produto, p.codigo, p.nome, p.descricao, p.preco " +
                    "from tb_produto_quantidade pq " +
                    "inner join tb_produto p on p.id = pq.id_produto_fk " +
                    "where pq.id_venda_fk = ?";
            stmProd = connection.prepareStatement(sbProd);
            stmProd.setLong(1, venda.getId());
            rsProd = stmProd.executeQuery();
            Set<ProdutoQuantidade> produtos = new HashSet<>();
            while(rsProd.next()) {
                ProdutoQuantidade prodQ = ProdutoQuantidadeFactory.convert(rsProd);
                produtos.add(prodQ);
            }
            venda.setProdutos(produtos);
            venda.recalcularValorTotalVenda();
        } catch (SQLException e) {
            throw new DaoException("erro consultando objeto ", e);
        } finally {
            closeConnection(connection, stmProd, rsProd);
        }
    }

    @Override
    public void cancelarVenda(Venda venda) throws DaoException {
        Connection connection = null;
        PreparedStatement stm = null;
        try {
            String sql = "update tb_venda set status_venda = ? where id = ?";
            connection = getConnection();
            stm = connection.prepareStatement(sql);
            stm.setString(1, Venda.Status.CANCELADA.name());
            stm.setLong(2, venda.getId());
            stm.executeUpdate();
            venda.setStatus(Venda.Status.CANCELADA);

        } catch (SQLException e) {
            throw new DaoException("erro atualizando objeto ", e);
        } finally {
            closeConnection(connection, stm, null);
        }
    }

    @Override
    public void finalizarVenda(Venda venda) throws DaoException {

        Connection connection = null;
        PreparedStatement stm = null;
        try {
            String sql = "UPDATE TB_VENDA SET STATUS_VENDA = ? WHERE ID = ?";
            connection = getConnection();
            stm = connection.prepareStatement(sql);
            stm.setString(1, Venda.Status.CONCLUIDA.name());
            stm.setLong(2, venda.getId());
            stm.executeUpdate();
            venda.setStatus(Venda.Status.CONCLUIDA);

        } catch (SQLException e) {
            throw new DaoException("ERRO ATUALIZANDO OBJETO ", e);
        } finally {
            closeConnection(connection, stm, null);
        }
    }

    @Override
    public void atualizarDados(Venda entity, Venda entityCadastrado) {
        entityCadastrado.setCodigo(entity.getCodigo());
        entityCadastrado.setStatus(entity.getStatus());
    }

    @Override
    protected String getQueryExclusao() {
        throw new UnsupportedOperationException("não é possível excluir vendas");
    }

    @Override
    protected String getQueryAtualizacao() {
        throw new UnsupportedOperationException("não é possível atualizar vendas finalizadas");
    }

    @Override
    protected void setParametrosQueryExclusao(PreparedStatement stmDelete, String valor) {
        throw new UnsupportedOperationException("não é possível excluir vendas");
    }

    @Override
    protected void setParametrosQueryAtualizacao(PreparedStatement stmUpdate, Venda entity) {
        throw new UnsupportedOperationException("não é possível atualizar vendas finalizadas");
    }

    @Override
    protected void setParametrosQuerySelect(PreparedStatement stmSelect, String valor) throws SQLException {
        stmSelect.setString(1, valor);
    }

    public void limparProdutosQuantidade() throws DaoException {
        String sqlProd = "truncate table tb_produto_quantidade;";
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            stm = connection.prepareStatement(sqlProd);
            stm.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("erro excluindo produto ", e);
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
                if (stm != null && !stm.isClosed()) {
                    stm.close();
                }
                if (connection != null && !stm.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void limparProdutosEstoque() throws DaoException {
        String sqlProd = "truncate table tb_estoque;";
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            stm = connection.prepareStatement(sqlProd);
            stm.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("erro excluindo estoque ", e);
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
                if (stm != null && !stm.isClosed()) {
                    stm.close();
                }
                if (connection != null && !stm.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void limparVendas() throws DaoException {
        String sqlV = "delete from tb_venda";
        Connection connection1 = null;
        PreparedStatement stm1 = null;
        ResultSet rs1 = null;
        try {
            connection1 = getConnection();
            stm1 = connection1.prepareStatement(sqlV);
            stm1.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("erro excluindo vendas ", e);
        } finally {
            try {
                if (rs1 != null && !rs1.isClosed()) {
                    rs1.close();
                }
                if (stm1 != null && !stm1.isClosed()) {
                    stm1.close();
                }
                if (connection1 != null && !connection1.isClosed()) {
                    connection1.close();
                }
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }
}
