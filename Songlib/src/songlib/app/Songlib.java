package songlib.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import songlib.view.SonglibController;


public class Songlib extends Application{
	
	Stage mainStage;

	@Override
	public void start(Stage stage) throws Exception {
		mainStage = stage;
		
		//create FXML Loader
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/songlib/view/SonglibDisplay.fxml"));
		
		BorderPane root = (BorderPane)loader.load();
		
		SonglibController controller = loader.getController();
		controller.setMainStage(mainStage);

		
		Scene scene = new Scene(root);
		mainStage.setScene(scene);
		mainStage.setResizable(false);
		mainStage.show();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	
}