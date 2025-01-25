package dao;

import model.User;
import db.DatabaseConnection;

import java.sql.*;

public class UserDAOImplements implements UserDAO {

    @Override
    public int create(User user) {
        String sql = "INSERT INTO user (cpf, name, email, phone, birth_date, account_type, password) VALUES (?,?,?,?,?,?,?)";
        int generatedId = -1;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { // Adicione RETURN_GENERATED_KEYS

            stmt.setString(1, user.getCpf());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setDate(5, java.sql.Date.valueOf(user.getBirthDate()));
            stmt.setString(6, user.getAccountType());
            stmt.setString(7, user.getPassword());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while creating user: " + e.getMessage());
        }
        return generatedId;
    }

    @Override
    public boolean isCpfRegistered(String cpf) {
        String sql = "SELECT COUNT(*) FROM user WHERE cpf = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println("Failed to check CPF: " + e.getMessage());
        }
        return false;
    }
}