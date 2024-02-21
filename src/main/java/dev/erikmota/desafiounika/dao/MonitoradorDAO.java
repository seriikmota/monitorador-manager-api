package dev.erikmota.desafiounika.dao;

import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.models.TipoPessoa;
import dev.erikmota.desafiounika.service.exceptions.DAOException;
import dev.erikmota.desafiounika.service.exceptions.ValidacaoException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
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

    public void save(Monitorador m) {
        String sql = "INSERT INTO monitorador(tipo, cpf, nome, rg, cnpj, razao, inscricao, email, data, ativo) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection connection = dataSource.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setStatements(m, stmt);
            stmt.executeUpdate();

            if (m.getEnderecos() != null && !m.getEnderecos().isEmpty()){
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next())
                    enderecoDAO.save(m.getEnderecos().get(0), generatedKeys.getLong(1));
            }
        } catch (SQLException ex) {
            throw new DAOException(ex.toString());
        }
    }

    public void edit(Monitorador m) {
        String sql = "UPDATE monitorador SET tipo=?, cpf=?, nome=?, rg=?, cnpj=?, razao=?, inscricao=?, email=?, data=?, ativo=? WHERE id=?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            setStatements(m, stmt);
            stmt.setLong(11, m.getId());
            stmt.execute();
        } catch (SQLException ex) {
            throw new DAOException(ex.toString());
        }
    }

    public void delete(Monitorador m) {
        String sql = "DELETE FROM monitorador WHERE id=?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (m.getEnderecos() != null && !m.getEnderecos().isEmpty()){
                m.getEnderecos().forEach(e -> enderecoDAO.delete(e.getId()));
            }
            stmt.setLong(1, m.getId());
            stmt.execute();
        } catch (SQLException ex) {
            throw new DAOException(ex.toString());
        }
    }

    public List<Monitorador> findAll() {
        String sql = "SELECT * FROM monitorador";
        try (Connection connection = dataSource.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            return mountObject(stmt);
        } catch (SQLException ex) {
            throw new DAOException(ex.toString());
        }
    }

    public List<Monitorador> filter(String text, Boolean ativo, TipoPessoa tipo) {
        String sql = "SELECT * FROM Monitorador WHERE TRUE";
        List<Object> params = new ArrayList<>();

        if (!StringUtils.isBlank(text)) {
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
            return mountObject(stmt);
        } catch (SQLException ex) {
            throw new DAOException(ex.toString());
        }
    }

    private List<Monitorador> mountObject(PreparedStatement stmt) throws SQLException {
        List<Monitorador> list = new ArrayList<>();
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
            list.add(m);
        }
        return list;
    }

    private void setStatements(Monitorador m, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, m.getTipo().toString());
        stmt.setString(2, m.getCpf());
        stmt.setString(3, m.getNome());
        stmt.setString(4, m.getRg());
        stmt.setString(5, m.getCnpj());
        stmt.setString(6, m.getRazao());
        stmt.setString(7, m.getInscricao());
        stmt.setString(8, m.getEmail());
        stmt.setDate(9, Date.valueOf(m.getData()));
        stmt.setBoolean(10, m.getAtivo());
    }

    public boolean existsById(Long id){
        String sql = "SELECT COUNT(id) FROM monitorador m WHERE m.id=?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next())
                return resultSet.getLong(1) != 0;
            else
                throw new ValidacaoException("Esse monitorador não foi encontrado!");
        } catch (SQLException ex) {
            throw new DAOException(ex.toString());
        }
    }

    public boolean existsByCpf(String cpf){
        String sql = "SELECT COUNT(id) FROM monitorador m WHERE m.cpf=?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next())
                return resultSet.getLong(1) != 0;
        } catch (SQLException ex) {
            throw new DAOException(ex.toString());
        }
        return false;
    }
    public boolean existsByCnpj(String cnpj){
        String sql = "SELECT COUNT(id) FROM monitorador m WHERE m.cnpj=?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cnpj);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next())
                return resultSet.getLong(1) != 0;
        } catch (SQLException ex) {
            throw new DAOException(ex.toString());
        }
        return false;
    }
    public Monitorador findByCpf(String cpf){
        String sql = "SELECT * FROM monitorador WHERE cpf=?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            return mountObject(stmt).get(0);
        } catch (SQLException ex) {
            throw new DAOException(ex.toString());
        }
    }
    public Monitorador findByCnpj(String cnpj){
        String sql = "SELECT * FROM monitorador WHERE cnpj=?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cnpj);
            return mountObject(stmt).get(0);
        } catch (SQLException ex) {
            throw new DAOException(ex.toString());
        }
    }

    public Monitorador findById(Long id) {
        String sql = "SELECT * FROM monitorador WHERE id=?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return mountObject(stmt).get(0);
        } catch (SQLException ex) {
            throw new DAOException(ex.toString());
        }
    }
}
