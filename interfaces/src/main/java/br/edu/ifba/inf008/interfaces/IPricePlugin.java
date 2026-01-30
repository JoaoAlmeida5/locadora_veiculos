package br.edu.ifba.inf008.interfaces;

import br.edu.ifba.inf008.model.Rental;

public interface IPricePlugin {
    double calculatePrice(Rental rental);
    String getPluginName();
}
