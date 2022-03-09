package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;

import static model.Inventory.AllParts;
import static model.Methods.*;


public class AddInHousePart {
    public TextField nameTxt;
    public TextField stockTxt;
    public TextField priceTxt;
    public TextField maxTxt;
    public TextField machineIDTxt;
    public TextField minTxt;
    public Label label1;
    public RadioButton inHouseButton;



    /**
     * navigates to AddPartOutsourced screen.
     */
    public void toOutsourced() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AddOutsourcedPart.fxml")));
        Stage stage = (Stage) (label1).getScene().getWindow();
        Scene scene = new Scene(root, 600, 550);
        stage.setTitle("Add Part - Outsourced");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Navigates to Main screen.
     */
    public void backToMain() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainScreen.fxml")));
        Stage stage = (Stage) (label1).getScene().getWindow();
        Scene scene = new Scene(root, 950, 450);
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Makes appropriate checks on data and adds a part to the parts list.
     */
    public void savePart() throws IOException {

       //Check for null values.
        if (nameTxt.getText().isEmpty() || priceTxt.getText().isEmpty() || stockTxt.getText().isEmpty() ||
                minTxt.getText().isEmpty() || maxTxt.getText().isEmpty() || machineIDTxt.getText().isEmpty()) {
            Alerts(7);
            return;
        }

        // Test numerical inputs for correct values.
        if (!isInteger(stockTxt.getText())){
            Alerts(5);
            return;
        }
        if (!isInteger(maxTxt.getText())){
            Alerts(5);
            return;
        }
        if (!isInteger(minTxt.getText())){
            Alerts(5);
            return;
        }
        if (!isInteger(machineIDTxt.getText())){
            Alerts(5);
            return;
        }
        if (!isDouble(priceTxt.getText())){
            Alerts(5);
            return;
        }

        //Generate a new unique ID.
        int maxVal = 0;
        for (model.Part allPart : AllParts) {
            if (maxVal < allPart.getId()) {
                maxVal = allPart.getId();
            }
        }
        int ID = (maxVal + 1);

        //Collect and parse user entered data.
        String name = nameTxt.getText();
        double price = Double.parseDouble(priceTxt.getText());
        int stock = Integer.parseInt(stockTxt.getText());
        int min = Integer.parseInt(minTxt.getText());
        int max = Integer.parseInt(maxTxt.getText());
        int machineID = Integer.parseInt(machineIDTxt.getText());
        //Convert price into a 2 decimal double.
        DecimalFormat df = new DecimalFormat("#.##");
        price = Double.parseDouble(df.format(price));

        //Checks machine ID for negative values.
        if(machineID < 1) {
            Alerts(1);
            return;
        }

        //Checks to ensure appropriate price value.
        if ( price < .01) {
            Alerts(3);
            return;
        }

        //Check the inventory logic.
        if (stock < min || stock > max || stock < 1 || min < 0) {
            Alerts(4);
            return;
        }

        //If all checks are successful add a new InHouse part.
        InHouse part = new InHouse(ID, name, price, stock, min, max, machineID);
        Inventory.addPart(part);
        backToMain();

    }
}



