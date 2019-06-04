package application;
	
import java.io.File;

import controller.ScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
			FXMLLoader loader= new FXMLLoader();
			loader.setLocation(this.getClass().getResource("screen.fxml"));
			ScreenController sc = new ScreenController();
			loader.setController(sc);
			AnchorPane anchorPane = loader.load();
			Scene scene = new Scene(anchorPane);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
