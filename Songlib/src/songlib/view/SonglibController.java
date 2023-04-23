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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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

public class SonglibController{
	Stage mainStage;
	Scene scenee;
	Parent roott;
	
	@FXML Button addButton;
	@FXML Button deleteButton;
	@FXML Button editButon;
	@FXML ListView<String> songList;
	@FXML Label title;
	@FXML Label year;
	@FXML Label artist;
	@FXML Label album;
	
	ArrayList<String> songs = new ArrayList<String>(); 
	
	public void setMainStage(Stage stage) throws IOException {
		mainStage = stage;
		
		
		// Read From songStorage.txt file which is the local storage for all the songs
		try {
            File file = new File("localData/songStorage.txt");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                // notice that line read is compressed-text
                songs.add(line);
            }

            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("No Matching File");
        }

		
		
		// Sort the Array alphabatically
		// Collections.sort(songs); // A != a
		Collections.sort(songs,(a, b) -> a.compareToIgnoreCase(b)); // A == a
		//write the sorted back to file
		writeToFile();
		
		
		//pre-select
		if(songs.size()>0) {
			songList.getSelectionModel().select(0);
			selectSongManual(0);
		}
		
		
		// Render the data into the ListView Table
		render();
		
	}
	public void setMainStage(Stage stage, int preselect_index) throws IOException {
		mainStage = stage;
		// Read From songStorage.txt file which is the local storage for all the songs
		try {
            File file = new File("localData/songStorage.txt");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                // notice that line read is compressed-text
                songs.add(line);
            }

            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("No Matching File");
        }

		// Sort the Array alphabatically
		Collections.sort(songs,(a, b) -> a.compareToIgnoreCase(b));
		//write the sorted back to file
		writeToFile();
		
		//pre-select
		if(songs.size()>0) {
			songList.getSelectionModel().select(preselect_index);
			selectSongManual(preselect_index);
		}
		// Render the data into the ListView Table
		render();
		
	}
	public void setMainStage(Stage stage, String preselect_string) throws IOException {
		mainStage = stage;
		// Read From songStorage.txt file which is the local storage for all the songs
		try {
            File file = new File("localData/songStorage.txt");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                // notice that line read is compressed-text
                songs.add(line);
            }

            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("No Matching File");
        }

		// Sort the Array alphabatically
		Collections.sort(songs,(a, b) -> a.compareToIgnoreCase(b));
		//write the sorted back to file
		writeToFile();
		
		//pre-select
		if(songs.size()>0) {
			int preselect_index=0;
			for(int i=0;i<songs.size();i++) {
				if(songs.get(i).equals(preselect_string)) {
					preselect_index=i;
					break;
				}
			}
			songList.getSelectionModel().select(preselect_index);
			selectSongManual(preselect_index);
		}
		// Render the data into the ListView Table
		render();
		
	}
	
	
	@FXML
	public void addHandler(MouseEvent event) throws IOException{
		// ONLINE way
//		try {
//            // Load the FXML file for the add scene
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("add.fxml"));
//            Parent addScene = loader.load();
//
//            // Get the stage and set the new scene
//            Stage stage = (Stage) addButton.getScene().getWindow();
//            stage.setScene(new Scene(addScene));
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
		
		
		
		// SESH Way
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/songlib/view/add.fxml"));
		
		AnchorPane root = (AnchorPane)loader.load();
		
		AddController controller = loader.getController();
		controller.setMainStage(mainStage,songs);

		
		Scene scene = new Scene(root);
		mainStage.setScene(scene);
		mainStage.setResizable(false);
		mainStage.show();
		
	
	}
	@FXML public void deleteHandler(MouseEvent event)throws IOException{
		// Check if a song is selected
		int selectedSongIndex = songList.getSelectionModel().getSelectedIndex();
		if(selectedSongIndex==-1){
			
			Alert alertDialog = new Alert(AlertType.WARNING);
			alertDialog.initOwner(mainStage);
			alertDialog.setTitle("Delete Warning");
			alertDialog.setHeaderText("Nothing is selected");
			alertDialog.setContentText("Please select a song before continuing to delete");
			alertDialog.showAndWait();
			return;
			
		}
		
        
        
        // Use the index to get the song, and delete it from local file
        String songName = songs.get(selectedSongIndex).split("\\?")[0];
        ButtonType delete = new ButtonType("Delete");
        ButtonType cancel = new ButtonType("Cancel");
        Alert dialog = new Alert(AlertType.WARNING,"You cannot undo this action",delete,cancel);
        dialog.initOwner(mainStage);
        dialog.setHeaderText("Are you sure you want to delete \""+songName+"\" ?");
        dialog.setTitle("Confirm Deletion");
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.get() == delete) {
        	//remove song
        	songs.remove(selectedSongIndex);
        	//update file
        	writeToFile();
        	//update list
        	render();
        	//update labels
        	if(selectedSongIndex<songs.size()){
        		//select next song
        		selectSongManual(selectedSongIndex);
        	}else if(selectedSongIndex>=songs.size()) {
        		//select previous song
        		selectSongManual(selectedSongIndex-1);
        	}else {
        		// Nothing in list
        		clearLabel();
        	}
        }
		
	}
	public void editHandler(MouseEvent event)throws IOException{
		int selectedSongIndex = songList.getSelectionModel().getSelectedIndex();
		if(selectedSongIndex==-1){
			
			Alert alertDialog = new Alert(AlertType.WARNING);
			alertDialog.initOwner(mainStage);
			alertDialog.setTitle("Edit Warning");
			alertDialog.setHeaderText("Nothing is selected");
			alertDialog.setContentText("Please select a song before continuing to edit");
			alertDialog.showAndWait();
			return;
			
		}
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/songlib/view/edit.fxml"));
		
		AnchorPane root = (AnchorPane)loader.load();
		
		EditController controller = loader.getController();
		controller.setMainStage(mainStage,songs,selectedSongIndex);

		
		Scene scene = new Scene(root);
		mainStage.setScene(scene);
		mainStage.setResizable(false);
		mainStage.show();
	}
	
	
	
	@FXML
	public void selectSong() {
		int index = songList.getSelectionModel().getSelectedIndex();
		if(index == -1) {
			return;
		}
		String item = songs.get(index);
        if (item != null) {
        	String songTitle = item.split("\\?")[0];
        	String songArtist = item.split("\\?")[1];
        	String songYear = item.split("\\?")[2];
        	String songAlbum = item.split("\\?")[3];
        	
        	
            title.setText(songTitle);
            artist.setText(songArtist);
            year.setText(songYear);
            album.setText(songAlbum);
            
        }  
	}
	public void selectSongManual(int index) {
		if(songs.size()==0 || index<0) {
			// If we are selecting a song that does that exists:
			// to activate this, we have to delete the last song.
			title.setText("");
            artist.setText("");
            year.setText("");
            album.setText("");
            return;
		}
		String item = songs.get(index);
		
        if (item != null) {
        	String songTitle = item.split("\\?")[0];
        	String songArtist = item.split("\\?")[1];
        	String songYear = item.split("\\?")[2];
        	String songAlbum = item.split("\\?")[3];
        	
        	
            title.setText(songTitle);
            artist.setText(songArtist);
            year.setText(songYear);
            album.setText(songAlbum);
            
        }  
	}
	
	public void writeToFile() throws IOException{
		File file = new File("localData/songStorage.txt");
		FileWriter writer = new FileWriter(file);
		for(int i=0;i<songs.size();i++) {
			String compressedText = songs.get(i);
            writer.write(compressedText + System.lineSeparator());
		}
		writer.close();
	}
	public void render() {
		//clean the list
		songList.getItems().clear();
		// Render the arraylist items into ListView
		for(int i=0;i<songs.size();i++) {
			String compressedText = songs.get(i);
			String songName = compressedText.split("\\?")[0];
			songList.getItems().add(songName);
		}
	}
	public void clearLabel() {
		title.setText("");
		artist.setText("");
		year.setText("");
		album.setText("");
	}
	
}