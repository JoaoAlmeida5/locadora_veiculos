package br.edu.ifba.inf008.model;

public class Rental {

    private Customer client;
    private Vehicle vehicle;
    private int days;
    private double totalPrice;

    public Rental(Customer client, Vehicle vehicle, int days, double totalPrice) {
        this.client = client;
        this.vehicle = vehicle;
        this.days = days;
        this.totalPrice = totalPrice;
    }

    // construtor usado pelo banco
    public Rental(String cpf, String plate, int days, double totalPrice) {
        this.client = new Customer("", cpf);
        this.vehicle = new Vehicle(plate, "", 0, "");
        this.days = days;
        this.totalPrice = totalPrice;
    }

    public Customer getClient() {
        return client;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public int getDays() {
        return days;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
