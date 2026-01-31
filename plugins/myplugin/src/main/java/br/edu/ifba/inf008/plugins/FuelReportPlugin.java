package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IDataProvider;
import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.model.Vehicle;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tab;
import java.util.List;

public class FuelReportPlugin implements IPlugin {

    @Override
    public boolean init(ICore core) { // <--- O Core chega por aqui
        // -----------------------------------------------------------
        // ERRO ESTAVA AQUI: Não declare "ICore core = ..." novamente.
        // Use a variável do parâmetro diretamente.
        // -----------------------------------------------------------

        if (core == null) return false;

        PieChart pieChart = new PieChart();
        pieChart.setTitle("Veículos por Tipo (Estoque)");

        // Usa o 'core' que veio do parâmetro
        IDataProvider provider = core.getDataProvider();

        try {
            for (Vehicle.VehicleType tipo : Vehicle.VehicleType.values()) {
                List<Vehicle> lista = provider.getVehiclesByType(tipo);
                if (lista != null && !lista.isEmpty()) {
                    pieChart.getData().add(new PieChart.Data(tipo.name(), lista.size()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Tab tab = new Tab("Relatório (Gráfico)", pieChart);
        // Usa o 'core' que veio do parâmetro
        core.getUIController().addTab(tab);

        return true;
    }
}