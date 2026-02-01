package br.edu.ifba.inf008.interfaces;

import java.util.List;
import br.edu.ifba.inf008.model.Rental;

public interface IReportPlugin {
    String getName();
    String generate(List<Rental> rentals);
}
