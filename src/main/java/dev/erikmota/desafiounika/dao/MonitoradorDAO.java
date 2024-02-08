package dev.erikmota.desafiounika.dao;

import com.mysql.cj.util.StringUtils;
import dev.erikmota.desafiounika.models.Endereco;
import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.models.TipoPessoa;
import dev.erikmota.desafiounika.service.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MonitoradorDAO {
    private final DataSource dataSource;
    private final EnderecoDAO enderecoDAO;

    public MonitoradorDAO(DataSource dataSource, EnderecoDAO enderecoDAO) {
        this.dataSource = dataSource;
        this.enderecoDAO = enderecoDAO;
    }

    public List<Monitorador> filter(String text, Boolean ativo, TipoPessoa tipo) {
        List<Monitorador> lista = new ArrayList<>();

        String sql = "SELECT * FROM Monitorador WHERE TRUE";
        List<Object> params = new ArrayList<>();

        if (!StringUtils.isEmptyOrWhitespaceOnly(text)) {
            sql += " AND (nome LIKE ? OR razao LIKE ? OR cpf LIKE ? OR cnpj LIKE ?)";
            String likeText = "%" + text + "%";
            params.add(likeText);
            params.add(likeText);
            params.add(likeText);
            params.add(likeText);
        }
        if (ativo != null) {
            sql += " AND ativo = ?";
            params.add(ativo);
        }
        if (tipo != null) {
            sql += " AND tipo = ?";
            params.add(tipo.toString());
        }
        try (Connection connection = dataSource.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            int parameterIndex = 1;
            for (Object param : params) {
                stmt.setObject(parameterIndex++, param);
            }
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Monitorador m = new Monitorador(
                        resultSet.getLong("id"),
                        TipoPessoa.valueOf(resultSet.getString("tipo")),
                        resultSet.getString("cpf"),
                        resultSet.getString("nome"),
                        resultSet.getString("rg"),
                        resultSet.getString("cnpj"),
                        resultSet.getString("razao"),
                        resultSet.getString("inscricao"),
                        resultSet.getString("email"),
                        resultSet.getDate("data").toLocalDate(),
                        resultSet.getBoolean("ativo"),
                        enderecoDAO.filter(null, null, null, resultSet.getLong("id")));
                lista.add(m);
            }
            return lista;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new ValidacaoException("Erro ao filtrar monitorador!");
        }
    }
}
