package dev.erikmota.desafiounika.dao;

import com.mysql.cj.util.StringUtils;
import dev.erikmota.desafiounika.models.Endereco;
import dev.erikmota.desafiounika.service.exceptions.DAOException;
import dev.erikmota.desafiounika.service.exceptions.ValidacaoException;
import jakarta.validation.ValidationException;
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

    public void save(Endereco e, Long monitoradorId) {
        String sql = "INSERT INTO endereco(cep, endereco, numero, bairro, cidade, estado, telefone, principal, monitorador_id) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection connection = dataSource.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            setStatements(e, stmt);
            stmt.setLong(9, monitoradorId);
            stmt.execute();
        } catch (SQLException ex) {
            throw new DAOException(ex.toString());
        }
    }

    public void edit(Endereco e) {
        String sql = "UPDATE endereco SET cep=?, endereco=?, numero=?, bairro=?, cidade=?, estado=?, telefone=?, principal=?, monitorador_id=? WHERE id=?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            setStatements(e, stmt);
            stmt.setLong(9, e.getMonitorador().getId());
            stmt.setLong(10, e.getId());
            stmt.execute();
        } catch (SQLException ex) {
            throw new DAOException(ex.toString());
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM endereco WHERE id=?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.execute();
        } catch (SQLException ex) {
            throw new DAOException(ex.toString());
        }
    }

    public List<Endereco> findAll() {
        String sql = "SELECT * FROM endereco";
        try (Connection connection = dataSource.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            return mountObject(stmt);
        } catch (SQLException ex) {
            throw new DAOException(ex.toString());
        }
    }

    public List<Endereco> filter(String text, String estado, String cidade, Long monitorador_id) {
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
            return mountObject(stmt);
        } catch (SQLException ex) {
            throw new DAOException(ex.toString());
        }
    }

    private List<Endereco> mountObject(PreparedStatement stmt) throws SQLException {
        List<Endereco> list = new ArrayList<>();
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
                    resultSet.getBoolean("principal"));
            list.add(e);
        }
        return list;
    }
    private void setStatements(Endereco e, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, e.getCep());
        stmt.setString(2, e.getEndereco());
        stmt.setString(3, e.getNumero());
        stmt.setString(4, e.getBairro());
        stmt.setString(5, e.getCidade());
        stmt.setString(6, e.getEstado());
        stmt.setString(7, e.getTelefone());
        stmt.setBoolean(8, e.getPrincipal());
    }

    public boolean existsById(Long id){
        String sql = "SELECT COUNT(id) FROM endereco e WHERE e.id=?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next())
                return resultSet.getLong(1) != 0;
            else
                throw new ValidacaoException("Esse endereço não foi encontrado!");
        } catch (SQLException ex) {
            throw new DAOException(ex.toString());
        }
    }
    public boolean existsByEnderecoAndNumero(String endereco, String numero){
        String sql = "SELECT COUNT(id) FROM endereco e WHERE e.endereco=? AND e.numero=?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, endereco);
            stmt.setString(2, numero);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next())
                return resultSet.getLong(1) != 0;
        } catch (SQLException ex) {
            throw new ValidationException(ex);
        }
        return false;
    }
    public Endereco findById(Long id) {
        String sql = "SELECT * FROM endereco WHERE id=?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return mountObject(stmt).get(0);
        } catch (SQLException ex) {
            throw new DAOException(ex.toString());
        }
    }
}
