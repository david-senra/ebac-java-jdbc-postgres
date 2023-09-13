package dsenra.dao;

import dsenra.JDBC.ConnectionFactory;
import dsenra.dao.generic.GenericDao;
import dsenra.domain.Cliente;
import dsenra.exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class ClienteDao extends GenericDao <Cliente, Long> implements IClienteDao {

    public ClienteDao() {
        super();
    }

    @Override
    public Class<Cliente> getTipoClasse() {
        return Cliente.class;
    }

    @Override
    public void atualizarDados(Cliente entity, Cliente entityCadastrado) {
        entityCadastrado.setNome(entity.getNome());
        entityCadastrado.setSobrenome(entity.getSobrenome());
        entityCadastrado.setCpf(entity.getCpf());
        entityCadastrado.setTelefone(entity.getTelefone());
        entityCadastrado.setEndereco(entity.getEndereco());
        entityCadastrado.setNumeroEndereco(entity.getNumeroEndereco());
        entityCadastrado.setCidade(entity.getCidade());
        entityCadastrado.setEstado(entity.getEstado());
    }

    @Override
    protected String getQueryInsercao() {
        return "insert into tb_cliente (id, nome, sobrenome, cpf, telefone, endereco, numero_endereco, cidade, estado) " +
                "values (nextval('sq_cliente'), ?, ?, ?, ?, ?, ?, ?, ?) ";
    }

    @Override
    protected void setParametrosQueryInsercao(PreparedStatement stmInsert, Cliente entity) throws SQLException {
        stmInsert.setString(1, entity.getNome());
        stmInsert.setString(2, entity.getSobrenome());
        stmInsert.setString(3, entity.getCpf());
        stmInsert.setString(4, entity.getTelefone());
        stmInsert.setString(5, entity.getEndereco());
        stmInsert.setLong(6, entity.getNumeroEndereco());
        stmInsert.setString(7, entity.getCidade());
        stmInsert.setString(8, entity.getEstado());
    }

    @Override
    protected String getQueryAtualizacao() {
        return "update tb_cliente " +
                "set nome = ?, " +
                "sobrenome = ?, " +
                "cpf = ?, " +
                "telefone = ?, " +
                "endereco = ?, " +
                "numero_endereco = ?, " +
                "cidade = ?, " +
                "estado = ? " +
                "where id = ?";
    }

    @Override
    protected void setParametrosQueryAtualizacao(PreparedStatement stmUpdate, Cliente entity) throws SQLException {
        stmUpdate.setString(1, entity.getNome());
        stmUpdate.setString(2, entity.getSobrenome());
        stmUpdate.setString(3, entity.getCpf());
        stmUpdate.setString(4, entity.getTelefone());
        stmUpdate.setString(5, entity.getEndereco());
        stmUpdate.setLong(6, entity.getNumeroEndereco());
        stmUpdate.setString(7, entity.getCidade());
        stmUpdate.setString(8, entity.getEstado());
        stmUpdate.setLong(9, entity.getId());
    }

    @Override
    protected String getQueryExclusao() {
        return "delete from tb_cliente where id = ?";
    }

    @Override
    protected void setParametrosQueryExclusao(PreparedStatement stmDelete, Long valor) throws SQLException {
        stmDelete.setLong(1, valor);
    }

    @Override
    protected void setParametrosQuerySelect(PreparedStatement stmSelect, Long valor) throws SQLException {
        stmSelect.setLong(1, valor);
    }

    protected void closeConnection(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        try {
            if (resultSet != null && !resultSet.isClosed()) {
                resultSet.close();
            }
            if (statement != null && !statement.isClosed()) {
                statement.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public void limparClientes() throws Exception {
        listaElementos().forEach(cli -> {
            try {
                excluir(cli.getId());
            } catch (DaoException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
