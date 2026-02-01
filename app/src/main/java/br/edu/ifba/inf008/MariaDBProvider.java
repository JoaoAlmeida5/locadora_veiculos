package br.edu.ifba.inf008;

import br.edu.ifba.inf008.model.Customer;
import br.edu.ifba.inf008.interfaces.IDataProvider;
import br.edu.ifba.inf008.model.*;

import br.edu.ifba.inf008.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MariaDBDataProvider implements IDataProvider {

    @Override
    public void saveClient(Customer client) {
        String sql = "INSERT INTO clients (name, cpf) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getName());
            stmt.setString(2, client.getCpf());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Customer> getAllClients() {
        List<Customer> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                clients.add(new Customer(
                        rs.getString("name"),
                        rs.getString("cpf")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clients;
    }

    @Override
    public void saveVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles (plate, model, daily_price, type) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vehicle.getPlate());
            stmt.setString(2, vehicle.getModel());
            stmt.setDouble(3, vehicle.getDailyPrice());
            stmt.setString(4, vehicle.getType());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                vehicles.add(new Vehicle(
                        rs.getString("plate"),
                        rs.getString("model"),
                        rs.getDouble("daily_price"),
                        rs.getString("type")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicles;
    }

    @Override
    public Vehicle findVehicleByPlate(String plate) {
        String sql = "SELECT * FROM vehicles WHERE plate = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, plate);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Vehicle(
                        rs.getString("plate"),
                        rs.getString("model"),
                        rs.getDouble("daily_price"),
                        rs.getString("type")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveRental(Rental rental) {
        String sql = "INSERT INTO rentals (client_cpf, vehicle_plate, days, total_price) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, rental.getClient().getCpf());
            stmt.setString(2, rental.getVehicle().getPlate());
            stmt.setInt(3, rental.getDays());
            stmt.setDouble(4, rental.getTotalPrice());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Rental> getAllRentals() {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT * FROM rentals";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                rentals.add(new Rental(
                        rs.getString("client_cpf"),
                        rs.getString("vehicle_plate"),
                        rs.getInt("days"),
                        rs.getDouble("total_price")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rentals;
    }
}
