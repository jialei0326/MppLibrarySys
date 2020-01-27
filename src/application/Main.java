package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/view/login/MainMenu.fxml"));
			//Application.setUserAgentStylesheet(getClass().getResource("/application/css/style.css").toExternalForm());
			Scene scene = new Scene(root,830,400);
			scene.getStylesheets().add(getClass().getResource("/application/css/style.css").toExternalForm());
			primaryStage.setResizable(false);//window size immutable
			primaryStage.setTitle("Main Menu");
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

