package dsenra.dao.generic;

import dsenra.annotations.ColunaTabela;
import dsenra.annotations.Tabela;
import dsenra.annotations.TipoChave;
import dsenra.domain.IPersistente;
import dsenra.exception.*;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static dsenra.JDBC.ConnectionFactory.getConnection;

public abstract class GenericDao <T extends IPersistente, E extends Serializable> implements IGenericDao <T, E> {
    public abstract Class<T> getTipoClasse();
    public abstract void atualizarDados (T entity, T entityCadastrado);
    protected abstract String getQueryInsercao();
    protected abstract String getQueryExclusao();
    protected abstract String getQueryAtualizacao();
    protected abstract void setParametrosQueryInsercao(PreparedStatement stmInsert, T entity) throws SQLException;
    protected abstract void setParametrosQueryExclusao(PreparedStatement stmDelete, E valor) throws SQLException;
    protected abstract void setParametrosQueryAtualizacao(PreparedStatement stmUpdate, T entity) throws SQLException;
    protected abstract void setParametrosQuerySelect(PreparedStatement stmSelect, E valor) throws SQLException;

    public GenericDao() {
    }

    public E getChave(T entity) throws TipoChaveNaoEncontradaException {
        Field[] fields = entity.getClass().getDeclaredFields();
        E returnValue;
        for (Field field : fields) {
            if (field.isAnnotationPresent(TipoChave.class)) {
                TipoChave tipoChave = field.getAnnotation(TipoChave.class);
                String nomeMetodo = tipoChave.value();
                try {
                    Method method = entity.getClass().getMethod(nomeMetodo);
                    returnValue = (E) method.invoke(entity);
                    return returnValue;
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    //Criar exception de negócio TipoChaveNaoEncontradaException
                    e.printStackTrace();
                    throw new TipoChaveNaoEncontradaException("Chave principal do objeto " + entity.getClass() + " não encontrada", e);
                }
            }
        }
        String msg = "Chave principal do objeto " + entity.getClass() + " não encontrada";
        System.out.println("**** ERRO ****" + msg);
        throw new TipoChaveNaoEncontradaException(msg);
    }

    @Override
    public boolean cadastrar(T objeto) throws TipoChaveNaoEncontradaException, DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(getQueryInsercao(), Statement.RETURN_GENERATED_KEYS);
            setParametrosQueryInsercao(statement, objeto);
            int linhasResultado = statement.executeUpdate();

            if (linhasResultado > 0) {
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        objeto.setId(resultSet.getLong(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            throw new DaoException("Erro cadastrando objeto", e);
        } finally {
            closeConnection(connection, statement, null);
        }
        return false;
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

    @Override
    public void excluir(E valor) throws DaoException, SQLException {
        Connection connection = getConnection();
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement(getQueryExclusao());
            setParametrosQueryExclusao(stm, valor);
            stm.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("ERRO EXCLUINDO OBJETO ", e);
        } finally {
            closeConnection(connection, stm, null);
        }
    }

    @Override
    public T buscar(E valor) throws MaisDeUmRegistroException, TableException, DaoException {
        try {
            validarMaisDeUmRegistro(valor);
            Connection connection = getConnection();
            PreparedStatement stm = connection.prepareStatement("select * from " + getTableName() + " where " + getNomeCampoChave(getTipoClasse()) + " = ?");
            setParametrosQuerySelect(stm, valor);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                T entity = getTipoClasse().getConstructor(null).newInstance(null);
                Field[] fields = entity.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(ColunaTabela.class)) {
                        ColunaTabela coluna = field.getAnnotation(ColunaTabela.class);
                        String dbName = coluna.dbName();
                        String javaSetName = coluna.setJavaName();
                        Class<?> classField = field.getType();
                        try {
                            Method method = entity.getClass().getMethod(javaSetName, classField);
                            setValueByType(entity, method, classField, rs, dbName);

                        } catch (NoSuchMethodException | TipoElementoNaoConhecidoException | IllegalAccessException | InvocationTargetException e) {
                            throw new DaoException("ERRO CONSULTANDO OBJETO ", e);
                        }
                    }
                }
                return entity;
            }

        } catch (SQLException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | TipoChaveNaoEncontradaException e) {
            throw new DaoException("ERRO CONSULTANDO OBJETO ", e);
        }
        return null;
    }

    @Override
    public void atualizar(T objeto) throws TipoChaveNaoEncontradaException, DaoException, SQLException {
        Connection connection = getConnection();
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement(getQueryAtualizacao());
            setParametrosQueryAtualizacao(stm, objeto);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("ERRO ALTERANDO OBJETO ", e);
        } finally {
            closeConnection(connection, stm, null);
        }
    }

    @Override
    public Collection<T> listaElementos() throws DaoException {
        List<T> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {

            connection = getConnection();
            stm = connection.prepareStatement("select * from " + getTableName());
            rs = stm.executeQuery();
            while (rs.next()) {
                T entity = getTipoClasse().getConstructor(null).newInstance(null);
                Field[] fields = entity.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(ColunaTabela.class)) {
                        ColunaTabela coluna = field.getAnnotation(ColunaTabela.class);
                        String dbName = coluna.dbName();
                        String javaSetName = coluna.setJavaName();
                        Class<?> classField = field.getType();
                        try {
                            Method method = entity.getClass().getMethod(javaSetName, classField);
                            setValueByType(entity, method, classField, rs, dbName);

                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException |
                                 TipoElementoNaoConhecidoException e) {
                            throw new DaoException("ERRO LISTANDO OBJETOS ", e);
                        }
                    }
                }
                list.add(entity);
            }

        } catch (SQLException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | TableException e) {
            throw new DaoException("ERRO LISTANDO OBJETOS ", e);
        } finally {
            closeConnection(connection, stm, rs);
        }
        return list;
    }

    private void validarMaisDeUmRegistro(E valor) throws MaisDeUmRegistroException, TableException, TipoChaveNaoEncontradaException, DaoException, SQLException {
        Connection connection = getConnection();
        PreparedStatement stm = null;
        ResultSet rs = null;
        Long count = null;
        try {
            stm = connection.prepareStatement("select count(*) from " + getTableName() + " where " + getNomeCampoChave(getTipoClasse()) + " = ?");
            setParametrosQuerySelect(stm, valor);
            rs = stm.executeQuery();
            if (rs.next()) {
                count = rs.getLong(1);
                if (count > 1) {
                    throw new MaisDeUmRegistroException("ENCONTRADO MAIS DE UM REGISTRO DE " + getTableName());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, stm, rs);
        }
    }

    private String getTableName() throws TableException {
        if (getTipoClasse().isAnnotationPresent(Tabela.class)) {
            Tabela table = getTipoClasse().getAnnotation(Tabela.class);
            return table.value();
        }else {
            throw new TableException("TABELA NO TIPO " + getTipoClasse().getName() + " NÃO FOI ENCONTRADA");
        }
    }

    private void setValueByType(T entity, Method method, Class<?> classField, ResultSet rs, String fieldName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException, TipoElementoNaoConhecidoException {

        if (classField.equals(Integer.class)) {
            Integer val = rs.getInt(fieldName);
            method.invoke(entity, val);
        } else if (classField.equals(Long.class)) {
            Long val = rs.getLong(fieldName);
            method.invoke(entity, val);
        } else if (classField.equals(Double.class)) {
            Double val =  rs.getDouble(fieldName);
            method.invoke(entity, val);
        } else if (classField.equals(Short.class)) {
            Short val =  rs.getShort(fieldName);
            method.invoke(entity, val);
        } else if (classField.equals(BigDecimal.class)) {
            BigDecimal val =  rs.getBigDecimal(fieldName);
            method.invoke(entity, val);
        } else if (classField.equals(String.class)) {
            String val =  rs.getString(fieldName);
            method.invoke(entity, val);
        } else {
            throw new TipoElementoNaoConhecidoException("TIPO DE CLASSE NÃO CONHECIDO: " + classField);
        }

    }

    public String getNomeCampoChave(Class classe) throws TipoChaveNaoEncontradaException {
        Field[] fields = classe.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(TipoChave.class)
                    && field.isAnnotationPresent(ColunaTabela.class)) {
                ColunaTabela coluna = field.getAnnotation(ColunaTabela.class);
                return coluna.dbName();
            }
        }
        return null;
    }
}
