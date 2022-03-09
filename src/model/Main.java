package model;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

import static model.Inventory.loadDefaultSet;
/**
 * JavaDocs located in JavaDocs folder.
 * 
 *LOGICAL ERROR.
 * Originally my code tried to replace the old part with an index that matched "selectedPart.getID()".
 * This was incredibly frustrating because I was replacing the first item in the list each time I updated a part, regardless
 * of what the part ID happened to be.  I realized that the problem was that my object "selectedPart" was initialized with an ID value
 * of 0.  I changed the code to replace the old part with an index that matched "ID" instead.
 *
 * FUTURE ENHANCEMENT.
 *Code could be written here to update the current inventory of parts when a part is used
 * for the creation of a product.
 */
public class Main extends Application {

     /**
     *Start Main Screen.
     */
    @Override
    public void start (Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainScreen.fxml")));
        primaryStage.setTitle("Main Screen");
        primaryStage.setScene(new Scene(root,1000,450));
        primaryStage.show();
    }

    public static void main(String[] args){

        /**
         * Calls method to add default data to tables
         */
        loadDefaultSet();

        /**
         * Launch application.
         */
        launch(args);
    }

}
