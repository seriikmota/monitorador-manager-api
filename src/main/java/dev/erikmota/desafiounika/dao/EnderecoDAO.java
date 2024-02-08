package dev.erikmota.desafiounika.dao;

import com.mysql.cj.util.StringUtils;
import dev.erikmota.desafiounika.models.Endereco;
import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.models.TipoPessoa;
import dev.erikmota.desafiounika.service.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EnderecoDAO {
    private final DataSource dataSource;

    public EnderecoDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Endereco> filter(String text, String estado, String cidade, Long monitorador_id) {
        List<Endereco> lista = new ArrayList<>();

        String sql = "SELECT * FROM endereco WHERE TRUE";
        List<Object> params = new ArrayList<>();

        if (!StringUtils.isEmptyOrWhitespaceOnly(text)){
            sql += " AND (cep LIKE ? OR endereco LIKE ? OR bairro LIKE ? OR cidade LIKE ? OR estado LIKE ? OR telefone LIKE ?)";
            String likeText = "%" + text + "%";
            params.add(likeText);
            params.add(likeText);
            params.add(likeText);
            params.add(likeText);
            params.add(likeText);
            params.add(likeText);
        }
        if (!StringUtils.isEmptyOrWhitespaceOnly(cidade)){
            sql += " AND cidade = ?";
            params.add(cidade);
        }
        if (!StringUtils.isEmptyOrWhitespaceOnly(estado)){
            sql += " AND estado = ?";
            params.add(estado);
        }
        if (monitorador_id != null){
            sql += " AND monitorador_id = ?";
            params.add(monitorador_id);
        }
        try (Connection connection = dataSource.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)){
            int parameterIndex = 1;
            for (Object param: params){
                stmt.setObject(parameterIndex++, param);
            }
            ResultSet resultSet = stmt.executeQuery();
            while(resultSet.next()){
                Endereco e = new Endereco(
                        resultSet.getLong("id"),
                        resultSet.getString("cep"),
                        resultSet.getString("endereco"),
                        resultSet.getString("numero"),
                        resultSet.getString("bairro"),
                        resultSet.getString("cidade"),
                        resultSet.getString("estado"),
                        resultSet.getString("telefone"),
                        resultSet.getBoolean("principal"),
                        null);
                lista.add(e);
            }
            return lista;
        } catch (SQLException e) {
            throw new ValidacaoException("Erro ao realizar a filtragem de endereços!");
        }
    }
}
