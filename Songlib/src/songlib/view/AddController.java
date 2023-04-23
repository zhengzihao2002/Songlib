package songlib.view;

// Song class
import songlib.app.Song;

// Array List operations
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

// JavaFX stuff
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

// File writing
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AddController{
	Stage mainStage;
	
	@FXML TextField nameText;
	@FXML TextField artistText;
	@FXML TextField yearText;
	@FXML TextField albumText;
	@FXML Label warning;
	@FXML Label nameLabel;
	@FXML Label artistLabel;
	
	int newIndex;
	ArrayList<String> songs;
	
	public void setMainStage(Stage stage, ArrayList<String> songs) {
		mainStage = stage;
		this.songs=songs;
	}
	
	@FXML
	public void submit(MouseEvent event) throws IOException {
		
		
		// Empty fields
		if(nameText.getText().isEmpty()||artistText.getText().isEmpty()) {
			warning.setText("Please enter the required fields");
			nameLabel.setTextFill(Color.RED);
			artistLabel.setTextFill(Color.RED);
			
		}else {
			// find duplicates
			for(int i=0;i<songs.size();i++) {
				String item = songs.get(i);
				String songTitle = item.split("\\?")[0];
	        	String songArtist = item.split("\\?")[1];
				//If the name and artist are the same as an existing song, the add should not be allowed
				if(nameText.getText().equals(songTitle) && artistText.getText().equals(songArtist)) {
					Alert alertDialog = new Alert(AlertType.WARNING);
					alertDialog.initOwner(mainStage);
					alertDialog.setTitle("Duplicate Warning");
					alertDialog.setHeaderText("Song and Artist already exists");
					alertDialog.setContentText("Duplicates not allowed");
					alertDialog.showAndWait();
					return;
				}
			}
			// Test whether the year is a four digit number
			if(yearText.getText().isEmpty()==false && yearText.getText().matches("\\d{4}")==false) {
				Alert alertDialog = new Alert(AlertType.WARNING);
				alertDialog.initOwner(mainStage);
				alertDialog.setTitle("Information Warning");
				alertDialog.setHeaderText("Invalid Year");
				alertDialog.setContentText("Years must be a four digit integer");
				alertDialog.showAndWait();
				return;
			}
			
			// Song is allowed: 
			// get user confirmation
			ButtonType confirm = new ButtonType("Confirm");
	        ButtonType cancel = new ButtonType("Cancel");
	        Alert dialog = new Alert(AlertType.WARNING,"You cannot undo this action",confirm,cancel);
	        dialog.initOwner(mainStage);
	        dialog.setHeaderText("Are you sure you want to add \""+nameText.getText()+"\" ?");
	        dialog.setTitle("Confirm Addition");
	        Optional<ButtonType> result = dialog.showAndWait();
	        if (result.get() != confirm) {
				return;
	        }
			//compress the text
			String compressedText = nameText.getText()+"?"+artistText.getText()+"?"+
			(yearText.getText().isEmpty()?"NA":yearText.getText()) + "?" +
			(albumText.getText().isEmpty()?"NA":albumText.getText())
			;
			//write into the file
            File file = new File("localData/songStorage.txt");
            FileWriter writer = new FileWriter(file,true);
            writer.write(compressedText + System.lineSeparator());
            writer.close();
            //get index of the new song of the sorted array
            songs.add(compressedText);
    		Collections.sort(songs,(a, b) -> a.compareToIgnoreCase(b));
            for(int i=0;i<songs.size();i++) {
            	if(songs.get(i).equals(compressedText)) {
            		newIndex = i;
            		break;
            	}
            }
            // return back to main scene
			backToMainScene(event);
		}
				

	}
	@FXML public void back(MouseEvent event) throws IOException{
		ButtonType confirmButton = new ButtonType("Confirm");
        ButtonType cancelButton = new ButtonType("Cancel");
		Alert alert = new Alert(AlertType.WARNING,"Nothing will be saved",confirmButton,cancelButton);
		alert.initOwner(mainStage);
		alert.setTitle("Back To Main Menu");
		alert.setHeaderText("Warning!");
		
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == confirmButton) {
            // Move on to the next stage
        	backToMainScene(event);
        } else {
            // Stay in the current stage
        }
		
		
	}
	public void backToMainScene(MouseEvent event)throws IOException{
		//create FXML Loader
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/songlib/view/SonglibDisplay.fxml"));
		
		BorderPane root = (BorderPane)loader.load();
		
		SonglibController controller = loader.getController();
		controller.setMainStage(mainStage,newIndex);

		
		Scene scene = new Scene(root);
		mainStage.setScene(scene);
		mainStage.setResizable(false);
		mainStage.show();
	}


	
	
}