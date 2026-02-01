package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.IPricePlugin;
import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.model.Rental;

import javafx.scene.control.MenuItem;
import javafx.event.ActionEvent;
import javafx.scene.control.Tab;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import java.time.temporal.ChronoUnit;

public class MyPlugin implements IPlugin, IPricePlugin
{
    @Override
    public boolean init(ICore core) {
        System.out.println("Plugin de Cálculo (MyPlugin) inicializado!");
        return true;
    }

    @Override
    public double calculatePrice(Rental rental) {
        if (rental.getStartDate() == null || rental.getEndDate() == null) return 0.0;

        long days = ChronoUnit.DAYS.between(rental.getStartDate(), rental.getEndDate());

        if (days <= 0) days = 1;
        return days * 100.00;
    }

    @Override
    public String getPluginName() {
        return "Cálculo Padrão + UI";
    }
}