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
        if (core == null) return false;

        System.out.println("Inicializando MyPlugin...");

        Tab myTab = new Tab("Aba Azul (MyPlugin)");
        myTab.setContent(new Rectangle(200, 200, Color.LIGHTSTEELBLUE));
        myTab.setClosable(false);

        core.getUIController().addTab(myTab);

        IUIController uiController = core.getUIController();

        if (uiController != null) {
            MenuItem menuItem = new MenuItem("My Menu Item");
            boolean added = uiController.addMenuItem("Menu 1", menuItem);

            if (added) {
                menuItem.setOnAction(e ->
                        System.out.println("Cliquei no menu do MyPlugin!")
                );
            }
        }

        return true;
    }

    // O método onLoad() foi removido pois sua lógica foi para o init()

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