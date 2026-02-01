package br.edu.ifba.inf008.shell;

import br.edu.ifba.inf008.model.*;
import br.edu.ifba.inf008.interfaces.*;
import br.edu.ifba.inf008.interfaces.IDataProvider;

import br.edu.ifba.inf008.MariaDBDataProvider;

import java.util.*;

public class Core {

    private static Core instance;

    private IDataProvider dataProvider;

    private Map<String, IVehiclePlugin> vehiclePlugins;
    private List<IReportPlugin> reportPlugins;

    private Core() {
        dataProvider = new MariaDBDataProvider();
        vehiclePlugins = new HashMap<>();
        reportPlugins = new ArrayList<>();
    }

    public static Core getInstance() {
        if (instance == null) {
            instance = new Core();
        }
        return instance;
    }

    // ---------------- CLIENT ----------------
    public void registerClient(String name, String cpf) {
        Customer client = new Customer(name, cpf);
        dataProvider.saveClient(client);
    }

    public List<Customer> listClients() {
        return dataProvider.getAllClients();
    }

    public void registerVehicle(String plate, String model, double dailyPrice, String type) {
        Vehicle vehicle = new Vehicle(plate, model, dailyPrice, type);
        dataProvider.saveVehicle(vehicle);
    }

    public List<Vehicle> listVehicles() {
        return dataProvider.getAllVehicles();
    }

    public void registerVehiclePlugin(IVehiclePlugin plugin) {
        vehiclePlugins.put(plugin.getType(), plugin);
    }

    public void registerReportPlugin(IReportPlugin plugin) {
        reportPlugins.add(plugin);
    }

    public Collection<IVehiclePlugin> getVehiclePlugins() {
        return vehiclePlugins.values();
    }

    public List<IReportPlugin> getReportPlugins() {
        return reportPlugins;
    }

    public void rentVehicle(String cpf, String plate, int days) {

        Customer client = dataProvider.getAllClients()
                .stream()
                .filter(c -> c.getCpf().equals(cpf))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Vehicle vehicle = dataProvider.findVehicleByPlate(plate);
        if (vehicle == null) {
            throw new RuntimeException("Veículo não encontrado");
        }

        IVehiclePlugin plugin = vehiclePlugins.get(vehicle.getType());
        if (plugin == null) {
            throw new RuntimeException("Plugin do veículo não registrado");
        }

        double total = plugin.calculatePrice(vehicle.getDailyPrice(), days);

        Rental rental = new Rental(client, vehicle, days, total);
        dataProvider.saveRental(rental);
    }

    public List<Rental> listRentals() {
        return dataProvider.getAllRentals();
    }
}
