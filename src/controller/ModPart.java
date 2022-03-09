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
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;

import static model.Inventory.AllParts;
import static model.Methods.*;
import static model.Methods.isInteger;

public class ModPart {
    public Label label1;
    public RadioButton inHouseButton;
    public RadioButton outsourcedButton;
    public TextField IDTxt;
    public TextField nameTxt;
    public TextField stockTxt;
    public TextField priceTxt;
    public TextField maxTxt;
    public TextField flexTxt;
    public TextField minTxt;
    public Label flexLabel;


    Part selectedPart = null;

    /**
     * Receive part from main screen.
     */
    public void sendPart(Part selectedItem){
        //Identify the subclass of the selected part.
        if(selectedItem instanceof Outsourced){
            flexLabel.setText("Company name");
            outsourcedButton.setSelected(true);
            flexTxt.setText(((Outsourced) selectedItem).getCompanyName());
        }
        else {
            InHouse temp = (InHouse) selectedItem;
            flexLabel.setText("Machine ID");
            inHouseButton.setSelected(true);
            flexTxt.setText((String.valueOf(temp.getMachineID())));
        }

        //Populate text fields with appropriate data.
        nameTxt.setText(selectedItem.getName());
        IDTxt.setText(String.valueOf(selectedItem.getId()));
        stockTxt.setText(String.valueOf(selectedItem.getStock()));
        priceTxt.setText(String.valueOf(selectedItem.getPrice()));
        maxTxt.setText(String.valueOf(selectedItem.getMax()));
        minTxt.setText(String.valueOf(selectedItem.getMin()));

        selectedPart = selectedItem;

        if(inHouseButton.isSelected()){
            inHouseButton.setDisable(true);
        }
        if(outsourcedButton.isSelected()){
            outsourcedButton.setDisable(true);
        }
    }


    /**
     * Navigates back to the Main screen.
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
     * Allow users to change the part subclass to InHouse type.
     */
    public void toInHouse() {
        outsourcedButton.setDisable(false);
        outsourcedButton.setSelected(false);
        inHouseButton.setDisable(true);
        flexLabel.setText("Machine ID");
        selectedPart = new InHouse(1, "null", 1, 1, 1, 1, 1);

    }

    /**
     * Allow users to change the part subclass to Outsourced type.
     */
    public void toOutsourced() {
        inHouseButton.setSelected(false);
        inHouseButton.setDisable(false);
        outsourcedButton.setDisable(true);
        flexLabel.setText("Company name");
        selectedPart = new Outsourced(0,"null", 0 , 0 , 0, 0, "null");

    }


      //Saves the new values of the selected part.
    /**
     *LOGICAL ERROR. Originally my code tried to replace the old part with an index that matched "selectedPart.getID()".
     * This was incredibly frustrating because I was replacing the first item in the list each time I updated a part, regardless
     * of what the part ID happened to be.  I realized that the problem was that my object "selectedPart" was initialized with an ID value
     * of 0.  I changed the code to replace the old part with an index that matched "ID" instead.
     */
    public void updatePart() throws IOException {
        //Checks for null values in user input fields.
        if (nameTxt.getText().isEmpty() || priceTxt.getText().isEmpty() || stockTxt.getText().isEmpty() ||
                minTxt.getText().isEmpty() || maxTxt.getText().isEmpty() || flexTxt.getText().isEmpty()) {
            Alerts(7);
            return;
        }

        //Checks for appropriate input into numerical fields.
        if (!isDouble(priceTxt.getText())) {
            Alerts(6);
            return;
        }
        if (!isInteger(stockTxt.getText())) {
            Alerts(5);
            return;
        }
        if (!isInteger(maxTxt.getText())) {
            Alerts(5);
            return;
        }
        if (!isInteger(minTxt.getText())) {
            Alerts(5);
            return;
        }
        if (selectedPart instanceof InHouse && !isInteger(flexTxt.getText())) {
            Alerts(5);
            return;
        }

        //Get user inputs from the text fields.
        int ID = Integer.parseInt(IDTxt.getText());
        String name = nameTxt.getText();
        double price = Double.parseDouble(priceTxt.getText());
        int stock = Integer.parseInt(stockTxt.getText());
        int min = Integer.parseInt(minTxt.getText());
        int max = Integer.parseInt(maxTxt.getText());
        int machineID = 0;
        String companyName = null;
        if (selectedPart instanceof InHouse) {
            machineID = Integer.parseInt(flexTxt.getText());
        } else if (selectedPart instanceof Outsourced) {
            companyName = flexTxt.getText();
        }
        //Format price to hold 2 decimal places.
        DecimalFormat df = new DecimalFormat("#.##");
        price = Double.parseDouble(df.format(price));


        //Ensure machineID is a positive number.
        if (selectedPart instanceof InHouse && machineID < 1) {
            Alerts(5);
            return;
        }

        //Ensure price is a positive value.
        if (price < .01) {
            Alerts(3);
            return;
        }

        //Check for logical errors in inventory.
        if (stock < min || stock > max || stock < 1 || min < 0) {
            Alerts(4);
            return;
        }

        //Get index for the part to be replaced.
        int index=0;
        for (int i = 0; i < AllParts.size(); i++) {
            if (AllParts.get(i).getId() == ID) {
                index = i;
            }
            }

        //Create new part, replace old part, and return to the main screen.
        if (selectedPart instanceof InHouse){
            InHouse part = new InHouse(ID, name, price, stock, min, max, machineID);
            Inventory.updatePart( index, part);
            backToMain();
        }
        else if (selectedPart instanceof Outsourced){
            Outsourced part = new Outsourced(ID, name, price, stock, min, max, companyName);
            Inventory.updatePart( index, part);
            backToMain();
        }

    }
}
