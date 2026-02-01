package br.edu.ifba.inf008; // Ajuste o pacote se necessário

import br.edu.ifba.inf008.interfaces.IPricePlugin;
import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.MariaDBProvider;
import br.edu.ifba.inf008.shell.Core;
import br.edu.ifba.inf008.shell.PluginController;
import br.edu.ifba.inf008.shell.UIController;
import br.edu.ifba.inf008.interfaces.IDataProvider;
import br.edu.ifba.inf008.model.Customer;
import br.edu.ifba.inf008.model.Rental;
import br.edu.ifba.inf008.model.Vehicle;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        MenuBar menuBar = new MenuBar();
        TabPane rootTabPane = new TabPane();
        VBox mainLayout = new VBox(menuBar, rootTabPane);

        Core core = new Core(rootTabPane, menuBar);

        System.out.println(" >>> CARREGANDO PLUGINS... ");

        core.getPluginController().init();

        core.getPluginController().startPlugins();

        createRentalTab(rootTabPane);

        Scene scene = new Scene(mainLayout, 1024, 768);
        primaryStage.setTitle("Locadora de Veículos - INF008");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createRentalTab(TabPane tabPane) {
        IDataProvider dataProvider = Core.getInstance().getDataProvider();

        Label lblCliente = new Label("Selecione o Cliente:");
        ComboBox<Customer> cmbCustomers = new ComboBox<>();

        try {
            cmbCustomers.getItems().addAll(dataProvider.getAllCustomers());
        } catch (Exception ex) {
            System.err.println("Erro ao buscar clientes: " + ex.getMessage());
        }

        Label lblTipo = new Label("Tipo de Veículo:");
        ComboBox<Vehicle.VehicleType> cmbType = new ComboBox<>();
        cmbType.getItems().addAll(Vehicle.VehicleType.values());

        Button btnBuscar = new Button("Buscar Veículos Disponíveis");
        ListView<Vehicle> listVehicles = new ListView<>();
        Label lblResultado = new Label();

        btnBuscar.setOnAction(e -> {
            Vehicle.VehicleType tipo = cmbType.getValue();
            if (tipo != null) {
                try {
                    List<Vehicle> veiculos = dataProvider.getVehiclesByType(tipo);
                    listVehicles.getItems().setAll(veiculos);
                    if(veiculos.isEmpty()) lblResultado.setText("Nenhum veículo disponível.");
                    else lblResultado.setText(veiculos.size() + " veículos encontrados.");
                } catch (Exception ex) {
                    lblResultado.setText("Erro no banco.");
                    ex.printStackTrace();
                }
            }
        });

        Button btnAlugar = new Button("ALUGAR VEÍCULO");

        btnAlugar.setOnAction(e -> {
            Customer cliente = cmbCustomers.getValue();
            Vehicle veiculo = listVehicles.getSelectionModel().getSelectedItem();

            if (cliente != null && veiculo != null) {
                // 1. Montamos o objeto Rental (ainda sem preço)
                Rental aluguel = new Rental();
                aluguel.setCustomer(cliente);
                aluguel.setVehicle(veiculo);
                aluguel.setStartDate(LocalDateTime.now());
                aluguel.setEndDate(LocalDateTime.now().plusDays(3)); // Exemplo: 3 dias de aluguel

                // 2. Buscamos o Plugin de Preço no Core
                // Note que usamos Core.getInstance() para pegar o controller
                var pluginController = Core.getInstance().getPluginController();
                IPricePlugin pricePlugin = pluginController.getPricePlugin();

                BigDecimal valorFinal;

                if (pricePlugin != null) {
                    // SE achou o plugin, usa a lógica dele
                    System.out.println("Calculando preço usando: " + pricePlugin.getPluginName());
                    double precoCalculado = pricePlugin.calculatePrice(aluguel);
                    valorFinal = BigDecimal.valueOf(precoCalculado);
                } else {
                    // SE NÃO achou, usa um fallback (plano B)
                    System.out.println("Nenhum plugin de preço encontrado. Usando fixo.");
                    valorFinal = new BigDecimal("100.00");
                }

                aluguel.setTotalValue(valorFinal);

                // 3. Salva no Banco
                if (dataProvider.saveRental(aluguel)) {
                    String msg = String.format("Aluguel salvo! Valor Total: R$ %.2f", valorFinal);
                    new Alert(Alert.AlertType.INFORMATION, msg).show();
                    btnBuscar.fire();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Erro ao salvar aluguel.").show();
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Selecione Cliente e Veículo.").show();
            }
        });

        VBox rentalLayout = new VBox(10);
        rentalLayout.setPadding(new Insets(15));
        rentalLayout.getChildren().addAll(
                lblCliente, cmbCustomers,
                lblTipo, cmbType,
                btnBuscar,
                new Label("Veículos Disponíveis:"), listVehicles,
                btnAlugar, lblResultado
        );

        Tab rentalTab = new Tab("Nova Locação");
        rentalTab.setContent(rentalLayout);
        rentalTab.setClosable(false); // Aba fixa
        tabPane.getTabs().add(rentalTab);
    }

    public static void main(String[] args) {
        launch(args);
    }
}