package dsenra.factories;

import dsenra.domain.Cliente;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteFactory {
    public static Cliente convert(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getLong("id_cliente"));
        cliente.setNome(rs.getString(("nome")));
        cliente.setNome(rs.getString(("sobrenome")));
        cliente.setCpf(rs.getString(("cpf")));
        cliente.setTelefone(rs.getString(("telefone")));
        cliente.setEndereco(rs.getString(("endereco")));
        cliente.setNumeroEndereco(rs.getLong(("numero_endereco")));
        cliente.setCidade(rs.getString(("cidade")));
        cliente.setEstado(rs.getString(("estado")));
        return cliente;
    }
}
