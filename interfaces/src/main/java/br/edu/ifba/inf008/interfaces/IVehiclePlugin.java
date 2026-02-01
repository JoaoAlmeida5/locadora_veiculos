package br.edu.ifba.inf008.interfaces;

public interface IVehiclePlugin {
    String getType();
    double calculatePrice(double basePrice, int days);
}
