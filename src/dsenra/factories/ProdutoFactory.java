package dsenra.factories;

import dsenra.domain.Produto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProdutoFactory {
    public static Produto convert(ResultSet rs) throws SQLException {
        Produto prod = new Produto();
        prod.setId(rs.getLong("id_produto"));
        prod.setCodigo(rs.getLong("codigo"));
        prod.setNome(rs.getString("nome"));
        prod.setDescricao(rs.getString("descricao"));
        prod.setPreco(rs.getBigDecimal("preco"));
        return prod;
    }
}
