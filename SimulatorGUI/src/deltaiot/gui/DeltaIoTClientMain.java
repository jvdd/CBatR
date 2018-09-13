package deltaiot.gui;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import deltaiot.client.SimulationClient;
import deltaiot.services.QoS;
import domain.Link;
import domain.Node;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import simulator.Simulator;

public class DeltaIoTClientMain extends Application {

	@FXML
	Pane model;

	@FXML
	VBox window;

	@FXML
	private Label lbl10_6, lbl11_8, lbl12_7, lbl3_1, lbl12_3, lbl10_5, lbl4_1, lbl2_4, lbl7_2, lbl7_3, lbl9_1, lbl6_4,
			lbl11_17, lbl9_5, lbl13_11, lbl14_12, lbl15_12;

	@FXML
	TextArea log;

	Simulator simulator;

	Service startClient = new Service() {

		@Override
		protected void succeeded() {

		}

		@Override
		protected Task createTask() {
			return new Task() {
				@Override
				protected Object call() throws Exception {
					System.out.println("Starting Client");
					return null;
				}
			};
		}
	};

	@Override
	public void start(Stage stage) throws Exception {
		URL url = new URL("file://" + System.getProperty("user.dir") + "/resources/DeltaIoTModel.fxml");
		Parent root = FXMLLoader.load(url);

		Scene scene = new Scene(root, 800, 800);
		stage.setTitle("DeltaIoT Current Settings");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setOnCloseRequest(new EventHandler() {

			@Override
			public void handle(Event arg0) {
				Platform.exit();
			}

		});
		startClient.start();
		stage.show();
	}

	final String format = "(%d, %d)";

	String getText(int moteId, int linkId) {
		// Probe.deltaIoTMode;
		if (simulator != null) {
			Node node = simulator.getNodeWithId(linkId);
			Link link = simulator.getMoteWithId(moteId).getLinkTo(node);
			if (link == null) {
				assert link != null : "Link should not be null!";
			}
			return String.format(format, link.getPowerNumber(), link.getDistribution());
		}
		return "";
	}

	public void resetColors() {
		lbl10_6.setTextFill(Color.BLACK);
		lbl11_8.setTextFill(Color.BLACK);
		lbl12_7.setTextFill(Color.BLACK);
		lbl3_1.setTextFill(Color.BLACK);
		lbl12_3.setTextFill(Color.BLACK);
		lbl10_5.setTextFill(Color.BLACK);
		lbl4_1.setTextFill(Color.BLACK);
		lbl2_4.setTextFill(Color.BLACK);
		lbl7_2.setTextFill(Color.BLACK);
		lbl7_3.setTextFill(Color.BLACK);
		lbl9_1.setTextFill(Color.BLACK);
		lbl6_4.setTextFill(Color.BLACK);
		lbl11_17.setTextFill(Color.BLACK);
		lbl9_5.setTextFill(Color.BLACK);
		lbl13_11.setTextFill(Color.BLACK);
		lbl14_12.setTextFill(Color.BLACK);
		lbl15_12.setTextFill(Color.BLACK);
	}

	// boolean first = true;
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	public void changeText(Label label, String text, int source, int destination) {
		if (!label.getText().equals(text)) {
			// if (first) {
			// resetColors();
			// first = false;
			// }
			// if (!label.getText().equals("(15,100)")) {
			label.setTextFill(Color.GREEN);
			StringBuilder builder = new StringBuilder();
			if (!log.getText().isEmpty()) {
				builder.append("\n");
			}

			builder.append(dateFormat.format(new Date()));
			builder.append(": Link (");
			builder.append(source);
			builder.append("-->");
			builder.append(destination);
			builder.append(") settings are changed from ");
			builder.append(label.getText());
			builder.append(" to ");
			builder.append(text);

			log.appendText(builder.toString());

			// }
			label.setText(text);
		}
	}

	Service deltaIoTDisplay = new Service() {

		int run = 0;

		@Override
		protected Task createTask() {

			return new Task() {
				@Override
				protected Object call() throws Exception {
					while (true) {
						if (simulator != null && simulator.getRunInfo().getRunNumber() > run) {
							run = simulator.getRunInfo().getRunNumber();
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									// first = true;
									changeText(lbl10_6, getText(10, 6), 10, 6);
									changeText(lbl11_8, getText(11, 7), 11, 7);
									changeText(lbl12_7, getText(12, 7), 12, 7);
									changeText(lbl3_1, getText(3, 1), 3, 1);
									changeText(lbl12_3, getText(12, 3), 12, 3);
									changeText(lbl10_5, getText(10, 5), 10, 5);
									changeText(lbl4_1, getText(4, 1), 4, 1);
									changeText(lbl2_4, getText(2, 4), 2, 4);
									changeText(lbl7_2, getText(7, 2), 7, 2);
									changeText(lbl7_3, getText(7, 3), 7, 3);
									changeText(lbl9_1, getText(9, 1), 9, 1);
									changeText(lbl6_4, getText(6, 4), 6, 4);
									changeText(lbl11_17, getText(11, 7), 11, 7);
									changeText(lbl9_5, getText(5, 9), 5, 9);
									changeText(lbl13_11, getText(13, 11), 13, 11);
									changeText(lbl14_12, getText(14, 12), 14, 12);
									changeText(lbl15_12, getText(15, 12), 15, 12);
								}
							});
						}
						Thread.sleep(1000);
					}
					// return null;
				}
			};
		}
	};

	@FXML
	private void initialize() {
		deltaIoTDisplay.start();
		log.textProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				log.setScrollTop(Double.MIN_VALUE);

			}
		});
	}

	@Override
	public void stop() {
		deltaIoTDisplay.cancel();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void setSimulationClient(Simulator simulator) {
		this.simulator = simulator;
	}

}
