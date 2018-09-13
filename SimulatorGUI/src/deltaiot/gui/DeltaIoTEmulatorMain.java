package deltaiot.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.RQVAdaptation;
import simulator.Simulator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import activforms.engine.ActivFORMSEngine;
import activforms.plugin.ModelUpdationPlugin;
import deltaiot.activforms.ActivFORMSDeploy;
import deltaiot.services.QoS;
import javafx.scene.chart.LineChart;

public class DeltaIoTEmulatorMain extends Application {
	@FXML
	private Button runEmulator, btnSaveResults, btnActivFORMS, btnClearResults, btnDisplay, btnRQV;

	@FXML
	private LineChart<?, ?> chartEnergyConsumption, chartPacketLoss, chartLatency;

	@FXML
	private ProgressBar progressBar;

	@FXML
	private Label lblProgress;

	public static final Path filePath = Paths.get(System.getProperty("user.dir") + "/data/data.csv");
	List<String> data = new LinkedList<String>();
	Stage primaryStage;
	Simulator simul;
	XYChart.Series energyConsumptionSeries;
	 XYChart.Series packetLossSeries;
	 XYChart.Series latencySeries;
	Service serviceEmulation = new Service() {

		@Override
		protected void succeeded() {
			displayData("Without Adaptation", 0);
		}

		@Override
		protected Task createTask() {
			return new Task() {
				@Override
				protected Object call() throws Exception {
					btnDisplay.setDisable(true);
					simul = deltaiot.DeltaIoTSimulator.createSimulatorForDeltaIoT();
					energyConsumptionSeries = new XYChart.Series();
					 packetLossSeries = new XYChart.Series();
					 latencySeries = new XYChart.Series();
					energyConsumptionSeries.setName("Reference Approach");
					 packetLossSeries.setName("Reference Approach");
					 latencySeries.setName("Reference Approach");
					Platform.runLater(() -> {
						chartEnergyConsumption.getData().add(energyConsumptionSeries);
						chartEnergyConsumption.setAnimated(false);
						 chartPacketLoss.setAnimated(false);
						 chartPacketLoss.getData().add(packetLossSeries);
						 chartLatency.setAnimated(false);
						 chartLatency.getData().add(latencySeries);
					});
					for (int i = 0; i < 96; i++) {
						simul.doSingleRun();
						QoS qos = simul.getQosValues().get(simul.getQosValues().size() - 1);

						Platform.runLater(() -> {

							// chartEnergyConsumption.setAnimated(false);
							displayData("Without Adaptation", 0, qos);
						});
					}
					btnDisplay.setDisable(false);
					return null;
				}
			};
		}
	};

	@FXML
	void runEmulatorClicked(ActionEvent event) {
		if (!serviceEmulation.isRunning()) {
			serviceEmulation.restart();
			serviceProgress.restart();
		}
	}

	Service serviceProgress = new Service() {
		@Override
		protected void succeeded() {

		}

		@Override
		protected Task createTask() {
			return new Task() {
				@Override
				protected Object call() throws Exception {
					int run = 0;
					do {
						if (simul != null) {
							run = simul.getRunInfo().getRunNumber();

							updateProgress(run, 96);
							updateMessage("(" + run + "/96" + ")");
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// e.printStackTrace();
						}

					} while (run < 96);

					return null;
				}
			};
		}
	};
	RQVAdaptation client;
	Service serviceAdaptation = new Service() {

		@Override
		protected void succeeded() {
			displayData("RQV", 1);
		}

		@Override
		protected Task createTask() {
			return new Task() {
				@Override
				protected Object call() throws Exception {
					if (client == null)
						client = new RQVAdaptation();
					// if (modelUpdationPlugin != null)
					// modelUpdationPlugin
					// .updateModel(System.getProperty("user.dir") +
					// "/models/SimpleAdaptationMAPE.xml");
					btnDisplay.setDisable(true);
					client.initializeSimulator();
					simul = client.getSimulator();
					btnDisplay.setDisable(false);
					client.start();

					energyConsumptionSeries = new XYChart.Series();
					packetLossSeries = new XYChart.Series();
					latencySeries = new XYChart.Series();
					energyConsumptionSeries.setName("RQV");
					packetLossSeries.setName("RQV");
					latencySeries.setName("RQV");
					Platform.runLater(() -> {
						serviceProgress.restart();
						chartEnergyConsumption.getData().add(energyConsumptionSeries);
						chartEnergyConsumption.setAnimated(false);
					
						chartPacketLoss.getData().add(packetLossSeries);
						chartPacketLoss.setAnimated(false);
						
						chartLatency.getData().add(latencySeries);
						chartLatency.setAnimated(false);
						
					});
					int i = 0;
					while (i < 96) {
						Thread.sleep(100);

						if (i < simul.getRunInfo().getRunNumber()) {
							i = simul.getRunInfo().getRunNumber();
							QoS qos = simul.getQosValues().get(simul.getQosValues().size() - 1);

							Platform.runLater(() -> {

								// chartEnergyConsumption.setAnimated(false);
								displayData("RQV", 0, qos);
							});
						}
					}

					return null;
				}
			};
		}
	};
	ActivFORMSDeploy activFORMSDeploy;
	Service serviceActivFORMS = new Service() {

		@Override
		protected void succeeded() {
			displayData("ActivFORMS", 2);
		}

		@Override
		protected Task createTask() {
			return new Task() {
				@Override
				protected Object call() throws Exception {
					if (activFORMSDeploy == null)
						activFORMSDeploy = new ActivFORMSDeploy();
					// if (modelUpdationPlugin != null)
					// modelUpdationPlugin
					// .updateModel(System.getProperty("user.dir") +
					// "/models/SimpleAdaptationMAPE.xml");
					btnDisplay.setDisable(true);
					activFORMSDeploy.initializeSimulator();
					simul = activFORMSDeploy.getSimulator();
					btnDisplay.setDisable(false);
					activFORMSDeploy.start();

					energyConsumptionSeries = new XYChart.Series();
					packetLossSeries = new XYChart.Series();
					latencySeries = new XYChart.Series();
					energyConsumptionSeries.setName("ActivFORMS");
					packetLossSeries.setName("ActivFORMS");
					latencySeries.setName("ActivFORMS");
					Platform.runLater(() -> {
						serviceProgress.restart();
						chartEnergyConsumption.getData().add(energyConsumptionSeries);
						chartEnergyConsumption.setAnimated(false);
					
						chartPacketLoss.getData().add(packetLossSeries);
						chartPacketLoss.setAnimated(false);
						
						chartLatency.getData().add(latencySeries);
						chartLatency.setAnimated(false);
						
					});
					int i = 0;
					while (i < 96) {
						Thread.sleep(100);

						if (i < simul.getRunInfo().getRunNumber()) {
							i = simul.getRunInfo().getRunNumber();
							QoS qos = simul.getQosValues().get(simul.getQosValues().size() - 1);

							Platform.runLater(() -> {

								// chartEnergyConsumption.setAnimated(false);
								displayData("ActivFORMS", 0, qos);
							});
						}
					}

					return null;
				}
			};
		}
	};

//	ModelUpdationPlugin modelUpdationPlugin;
//	Service serviceUpdateModel = new Service() {
//
//		@Override
//		protected void succeeded() {
//			displayData("With Updated Model", 1);
//		}
//
//		@Override
//		protected Task createTask() {
//			return new Task() {
//				@Override
//				protected Object call() throws Exception {
//					if (client == null) {
//						client = new lnu.activforms.example.SimpleAdaptation();
//					}
//					btnDisplay.setDisable(true);
//					client.initializeSimulator();
//					simul = client.getSimulator();
//					btnDisplay.setDisable(false);
//
//					if (modelUpdationPlugin == null) {
//						ActivFORMSEngine engine = client.getActivFORMSEngine();
//						modelUpdationPlugin = new ModelUpdationPlugin(engine);
//					}
//					modelUpdationPlugin
//							.updateModel(System.getProperty("user.dir") + "/models/SimpleAdaptationMAPE-Updated.xml");
//					//
//					client.start();
//					// do{
//					// try {
//					// Thread.sleep(1000);
//					// } catch (InterruptedException e) {
//					// e.printStackTrace();
//					// }
//					// }while()
//					return null;
//				}
//			};
//		}
//	};

	// ActivFORMSDeploy client;

	@FXML
	void btnActivFORMS(ActionEvent event) {
		if (!serviceActivFORMS.isRunning()) {
			serviceActivFORMS.restart();
			//serviceProgress.restart();
		}
	}
	
	@FXML
	void btnRQV(ActionEvent event) {
		if (!serviceAdaptation.isRunning()) {
			serviceAdaptation.restart();
			//serviceProgress.restart();
		}
	}

//	@FXML
//	void btnUpdateModel(ActionEvent event) {
//		if (!serviceUpdateModel.isRunning()) {
//			serviceUpdateModel.restart();
//			serviceProgress.restart();
//		}
//	}

	@FXML
	void btnDisplay(ActionEvent event) {
		try {
			URL url = new URL("file://" + System.getProperty("user.dir") + "/resources/DeltaIoTModel.fxml");
			FXMLLoader fxmlLoader = new FXMLLoader(url);
			Parent root1 = (Parent) fxmlLoader.load();
			DeltaIoTClientMain main = fxmlLoader.getController();
			main.setSimulationClient(simul);
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.setResizable(false);
			stage.setTitle("DeltaIoT Topology");
			stage.setAlwaysOnTop(true);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void initialize() {
		assert progressBar != null : "fx:id=\"progressBar\" was not injected: check your FXML file 'Progress.fxml'.";
		lblProgress.textProperty().bind(serviceProgress.messageProperty());
		progressBar.progressProperty().bind(serviceProgress.progressProperty());
	}

	@FXML
	void btnSaveResults(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showSaveDialog(primaryStage);

		if (file != null) {
			saveFile(file);
		}
	}

	private void saveFile(File file) {
		try {
			FileWriter fileWriter = null;

			fileWriter = new FileWriter(file, true);

			for (String line : data)
				fileWriter.write(line + "\n");
			fileWriter.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	@FXML
	void btnClearResults(ActionEvent event) {
		chartEnergyConsumption.getData().clear();
		chartPacketLoss.getData().clear();
		chartLatency.getData().clear();
		data.clear();
	}

	void displayData(String setName, int index) {
		//XYChart.Series energyConsumptionSeries = new XYChart.Series();
		//XYChart.Series packetLossSeries = new XYChart.Series();
		//energyConsumptionSeries.setName(setName);
		//packetLossSeries.setName(setName);
		energyConsumptionSeries.getData().clear();
		packetLossSeries.getData().clear();
		latencySeries.getData().clear();
		List<QoS> qosList = simul.getQosValues();

		for (QoS qos : qosList) {
			data.add(qos + ", " + setName);
			energyConsumptionSeries.getData()
					.add(new XYChart.Data(Integer.parseInt(qos.getPeriod()), qos.getEnergyConsumption()));
			packetLossSeries.getData().add(new XYChart.Data(Integer.parseInt(qos.getPeriod()), qos.getPacketLoss()*100));
			latencySeries.getData().add(new XYChart.Data(Integer.parseInt(qos.getPeriod()), qos.getLatency()*100));
		}
		//chartEnergyConsumption.getData().add(energyConsumptionSeries);
	    //chartPacketLoss.getData().add(packetLossSeries);
	}

	void displayData(String setName, int index, QoS qos) {
		// List<QoS> qosList = simul.getQosValues();
		// energyConsumptionSeries.getData().clear();
		// packetLossSeries.getData().clear();
		// for (QoS qos : qosList) {
		data.add(qos + ", " + setName);
		energyConsumptionSeries.getData().add(new XYChart.Data(Integer.parseInt(qos.getPeriod()), qos.getEnergyConsumption()));
		 packetLossSeries.getData().add(new XYChart.Data(Integer.parseInt(qos.getPeriod()),qos.getPacketLoss()*100));
		 latencySeries.getData().add(new XYChart.Data(Integer.parseInt(qos.getPeriod()),qos.getLatency()*100));
		// }

	}

	@Override
	public void start(Stage stage) throws Exception {
		URL url = new URL("file://" + System.getProperty("user.dir") + "/resources/EmulatorGUI.fxml");
		Parent root = FXMLLoader.load(url);// getClass().getResource(System.getProperty("user.dir")
											// +
											// "/resources/EmulatorGUI.fxml"));
		primaryStage = stage;
		Scene scene = new Scene(root, 900, 600);
		scene.getStylesheets().add("file://" + System.getProperty("user.dir") + "/resources/style.css");
		stage.setTitle("DeltaIoT");
		stage.setOnCloseRequest(new EventHandler() {

			@Override
			public void handle(Event arg0) {
				Platform.exit();
			}

		});
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
