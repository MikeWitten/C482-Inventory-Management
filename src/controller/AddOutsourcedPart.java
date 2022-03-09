package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Outsourced;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;

import static model.Inventory.AllParts;
import static model.Inventory.addPart;
import static model.Methods.*;

public class AddOutsourcedPart {

    public TextField nameTxt;
    public TextField stockTxt;
    public TextField priceTxt;
    public TextField maxTxt;
    public TextField companyNameTxt;
    public TextField minTxt;
    public Label label1;

    /**
     * Navigates to AddInHouse screen.
     */
    public void toInHouse() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AddInHousePart.fxml")));
        Stage stage = (Stage) (label1).getScene().getWindow();
        Scene scene = new Scene(root, 600, 550);
        stage.setTitle("Add Part - In House");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Navigates to Main screen.
     */
    public void backToMain() throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainScreen.fxml")));
        Stage stage = (Stage) (label1).getScene().getWindow();
        Scene scene = new Scene(root, 950, 450);
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Saves new part to Part list.
     */
    public void savePart() throws IOException {

        //Check for null values in the input fields.
        if(nameTxt.getText().isEmpty()|| priceTxt.getText().isEmpty() || stockTxt.getText().isEmpty() ||
                minTxt.getText().isEmpty() || maxTxt.getText().isEmpty() || companyNameTxt.getText().isEmpty()){
            Alerts(7);
            return;
        }

        //Test numeric inputs for correct values.
        if(!isDouble(priceTxt.getText())){
            Alerts(6);
            return;
        }
        if(!isInteger(stockTxt.getText())){
            Alerts(5);
            return;
        }
        if(!isInteger(maxTxt.getText())){
            Alerts(5);
            return;
        }
        if(!isInteger(minTxt.getText())){
            Alerts(5);
            return;
        }

        //Generate a new ID.
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
        String companyName = companyNameTxt.getText();
        //Convert price into a 2 decimal double.
        DecimalFormat df = new DecimalFormat("#.##");
        price = Double.parseDouble(df.format(price));

       //Ensure that price is a non-negative number.
        if (price < .01){
            Alerts(3);
            return;
        }

        //Check for faulty logic in the inventory inputs.
        if(stock < min || stock > max || stock < 1 || min < 0){
            Alerts(4);
            return;
        }

        //Create a new outsourced part to be added to the parts list then route the user back to the main scene.
        Outsourced part = new Outsourced(ID, name, price, stock, min, max, companyName);
        addPart(part);
        backToMain();
    }
}



