package br.edu.ifba.inf008.interfaces;

import java.util.List;
import br.edu.ifba.inf008.model.Customer;
import br.edu.ifba.inf008.model.Vehicle;
import br.edu.ifba.inf008.model.Rental;

public interface IDataProvider {

    void saveClient(Customer client);
    List<Customer> getAllClients();

    void saveVehicle(Vehicle vehicle);
    List<Vehicle> getAllVehicles();
    Vehicle findVehicleByPlate(String plate);

    void saveRental(Rental rental);
    List<Rental> getAllRentals();
}
