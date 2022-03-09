package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.ResourceBundle;

import static model.Inventory.AllParts;
import static model.Inventory.AllProducts;
import static model.Methods.*;
import static model.Methods.isInteger;

public class ProductAddScreen implements Initializable {

    public TableView<Part> PartsTable;
    public TableColumn<Object, Object> partID;
    public TableColumn<Object, Object> partName;
    public TableColumn<Object, Object> partPrice;
    public TableColumn<Object, Object> partInventory;
    public Label label1;
    public TextField productID;
    public TextField nameTxt;
    public TextField stockTxt;
    public TextField priceTxt;
    public TextField maxTxt;
    public TextField minTxt;
    public TextField partsFilterField;
    public TableView<Part> AssociatedPartsTable;
    public TableColumn<Object, Object> idColumn;
    public TableColumn<Object, Object> nameColumn;
    public TableColumn<Object, Object> stockColumn;
    public TableColumn<Object, Object> priceColumn;


    ObservableList<Part> tempList = FXCollections.observableArrayList();

    /**
     * Navigate to the Main screen
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
     * Move selected part from Parts list into AssociatedParts table.
     */
    public void movePart() {
        // Identify selected item
        Part selectedItem = PartsTable.getSelectionModel().getSelectedItem();
        //Ensure that a part has been selected.
        if (selectedItem == null) {
            Alerts(2);
            return;
        }
        movePart1(selectedItem);
    }

    public void movePart1(Part selectedItem ) {

        //Check for duplicate Items.
        if (tempList.contains(selectedItem)) {
            Alerts(8);
            return;
        }
         movePart2(selectedItem);
        }

    public void movePart2(Part selectedItem){
        //If the part move meets all requirements it is added to the associated list and table.
        tempList.add(selectedItem);
        PartsTable.getSelectionModel().clearSelection();
        AssociatedPartsTable.refresh();
    }

    /**
     *Delete part from Associated Parts list.
     */
    public void removePart() {
        //Identify selected part.
        Part selectedpart = AssociatedPartsTable.getSelectionModel().getSelectedItem();

        //ensure an item has been selected.
        if(selectedpart == null){
            Alerts(2);
            return;
        }
        // Remove part.
        tempList.remove(selectedpart);
        AssociatedPartsTable.refresh();
    }

    /**
     * Confirm the addition of a product to the Products list.\
     */
    public void saveProduct() throws IOException {
        // ensure that all text fields are filled in.
        if(nameTxt.getText().isEmpty() || priceTxt.getText().isEmpty() || stockTxt.getText().isEmpty() ||
        minTxt.getText().isEmpty() || maxTxt.getText().isEmpty() || tempList.isEmpty()){
            Alerts(7);
            return;
        }
        saveProduct2();
    }

    public void saveProduct2() throws IOException {
        //Check for numerical values.
        if (!isDouble(priceTxt.getText())){
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
        saveProduct3();
    }
    /**
     * FUTURE ENHANCEMENT.  Code could be written here to update the current inventory of parts when a part is used
     * for the creation of a product.
     */
    public void saveProduct3() throws IOException {
        //Generate a product ID.
        int maxVal = 0;
        for (Product allProduct : AllProducts) {
            if (maxVal < allProduct.getID()) {
                maxVal = allProduct.getID();
            }
        }
        int ID = maxVal + 1;

        //Get user inputs.
        String name = nameTxt.getText();
        double price = Double.parseDouble(priceTxt.getText());
        int stock = Integer.parseInt(stockTxt.getText());
        int max = Integer.parseInt(maxTxt.getText());
        int min = Integer.parseInt(minTxt.getText());
        //Convert price into a double with 2 decimal places.
        DecimalFormat df = new DecimalFormat("#.##");
        price = Double.parseDouble(df.format(price));



        //Check for price logic
        //first find the sum price of the associated parts.
        double sumOfAssociatedParts = 0;
        for (Part part : tempList) {
            sumOfAssociatedParts += part.getPrice();
        }
        // Check to ensure that the product price is higher than the sum price of its parts.
        if(price < sumOfAssociatedParts || price < .01){
            Alerts(9);
            return;
        }

        //Check the inventory logic.
        if(min < 0 || stock > max || stock < min){
            Alerts(4);
            return;
        }

        //Check to ensure that the product has at least one associated part.
        if (tempList.isEmpty()){
            Alerts(10);
            return;
        }

        //If product meets all requirements, create a new Product object.
        Product product = new Product(ID, name, price, stock, min, max);
        //add items to the associated parts list
        product.AssociatedParts.addAll(tempList);
        AllProducts.add(product);
        //Return to the main scene.
        backToMain();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Get Values for the Parts and AssociatedParts Tables.
        PartsTable.setItems(Inventory.getAllParts());
        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        AssociatedPartsTable.setItems(tempList);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));


        //Create a text based search field for the PartsTable.
        //Wrap the Parts Observable list in a Parts FilteredList initialized to display all data.
        FilteredList<Part> partsFilteredList = new FilteredList<>(AllParts, p -> true);


         //Create a listener to update whenever the partsFilterField text field changes.
         //Compare the filter text with partName and partID.
        partsFilterField.textProperty().addListener((observable, oldValue, newValue) -> partsFilteredList.setPredicate(part -> {
            // Display all objects if text field is empty.
            if (newValue == null || newValue.isEmpty()){
                return true;
            }
            // Compare partName with filter text.
            String lowerCaseFilter = newValue.toLowerCase();
            if (part.getName().toLowerCase().contains(lowerCaseFilter)){
                return true;
            }
            //Compare partID with filter text.
            else return String.valueOf(part.getId()).contains(lowerCaseFilter);
        }));

         //Wrap the Parts filtered list into a sorted list.
         //Then bind sorted list to the TableView
        SortedList<Part> partSortedList = new SortedList<>(partsFilteredList);
        partSortedList.comparatorProperty().bind(PartsTable.comparatorProperty());

        //Set the sorted and filtered objects into the TableView.
        PartsTable.setItems(partSortedList);


    }
        
}

