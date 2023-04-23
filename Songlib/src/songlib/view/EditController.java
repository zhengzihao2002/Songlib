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

public class EditController{
	Stage mainStage;
	int songIndex;
	String finalCompressedText;
	ArrayList <String> songs;
	
	@FXML TextField nameText;
	@FXML TextField artistText;
	@FXML TextField yearText;
	@FXML TextField albumText;
	@FXML Label warning;
	@FXML Label nameLabel;
	@FXML Label artistLabel;
	
	
	public void setMainStage(Stage stage,ArrayList <String> songs,int selectedSongIndex) {
		mainStage = stage;
		this.songs=songs;
		songIndex= selectedSongIndex;
		
		// Enter the fields on default, for the selected song
		String item = songs.get(selectedSongIndex);
		String songTitle = item.split("\\?")[0];
    	String songArtist = item.split("\\?")[1];
    	String songYear = item.split("\\?")[2];
    	String songAlbum = item.split("\\?")[3];
    	
    	nameText.setText(songTitle);
    	artistText.setText(songArtist);
    	yearText.setText(songYear);
    	albumText.setText(songAlbum);
	}
	
	@FXML
	public void submit(MouseEvent event) throws IOException {
		// Check Empty fields
		if(nameText.getText().isEmpty()||artistText.getText().isEmpty()) {
			warning.setText("Please enter the required fields");
			nameLabel.setTextFill(Color.RED);
			artistLabel.setTextFill(Color.RED);
		}else {
			// find duplicates (with other songs)
			for(int i=0;i<songs.size();i++) {
				if(i==songIndex){
					// if we reach the current song index ignore, or else duplicate will 100% show even if there is none
					continue;
				}
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
			if(yearText.getText().isEmpty()==false&&yearText.getText().matches("\\d{4}")==false) {
				Alert alertDialog = new Alert(AlertType.WARNING);
				alertDialog.initOwner(mainStage);
				alertDialog.setTitle("Information Warning");
				alertDialog.setHeaderText("Invalid Year");
				alertDialog.setContentText("Years must be a four digit integer");
				alertDialog.showAndWait();
				return;
			}
			
			// Song is allowed to add:
			//get user confirmation
			ButtonType confirm = new ButtonType("Confirm");
	        ButtonType cancel = new ButtonType("Cancel");
	        Alert dialog = new Alert(AlertType.WARNING,"You cannot undo this action",confirm,cancel);
	        dialog.initOwner(mainStage);
	        dialog.setHeaderText("Are you sure you want to edit \""+nameText.getText()+"\" ?");
	        dialog.setTitle("Confirm Edition");
	        Optional<ButtonType> result = dialog.showAndWait();
	        if (result.get() != confirm) {
				return;
	        }
	        
	        // compress text
			String compressedText = nameText.getText()+"?"+artistText.getText()+"?"+
			(yearText.getText().isEmpty()?"NA":yearText.getText()) + "?" +
			(albumText.getText().isEmpty()?"NA":albumText.getText());
					
			// write into file
			File file = new File("localData/songStorage.txt");
			FileWriter writer = new FileWriter(file);
			for(int i=0;i<songs.size();i++) {
				if(i==songIndex) {
					writer.write(compressedText + System.lineSeparator());
					continue;
				}
	            writer.write(songs.get(i) + System.lineSeparator());
			}
			writer.close();
			
			// set the compressed text to global
			finalCompressedText = compressedText;
			
	        
			// go back to main
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
		controller.setMainStage(mainStage,finalCompressedText);

		
		Scene scene = new Scene(root);
		mainStage.setScene(scene);
		mainStage.setResizable(false);
		mainStage.show();
	}


	
	
}