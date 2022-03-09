package controller;

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

import static model.Inventory.*;
import static model.Methods.*;
import static model.Methods.isInteger;

public class ProductModScreen implements Initializable {

    public TextField IDTxt;
    public TextField nameTxt;
    public TextField stockTxt;
    public TextField priceTxt;
    public TextField maxTxt;
    public TextField minTxt;

    public TableView<Part> AssociatedPartsTable;
    public TableColumn<Object, Object> a_partID;
    public TableColumn<Object, Object> a_partName;
    public TableColumn<Object, Object> a_partInventory;
    public TableColumn<Object, Object> a_partPrice;
    public TableView<Part> PartsTable;
    public TableColumn<Object, Object> partID;
    public TableColumn<Object, Object> partName;
    public TableColumn<Object, Object> partInventory;
    public TableColumn<Object, Object> partPrice;
    public Label label1;
    public TextField partsFilterField;
    public Button saveButton;



    /**
     * Moves part from Parts list to Associated Parts list.
     */
    public void movePart() {
        //Identify current product
        Product currentProduct = null;
        for (Product allProduct : AllProducts) {
            if (allProduct.getID() == Integer.parseInt(IDTxt.getText())) {
                currentProduct = allProduct;
            }
        }

        //Ensure that a part has been selected.
        if(PartsTable.getSelectionModel().getSelectedItem() == null){
            Alerts(2);
            return;
        }

        //Identify the selected part
        Part selectedPart = PartsTable.getSelectionModel().getSelectedItem();

        //Check to see if the selected part is identical to any part in the AssociatedParts list.
        assert currentProduct != null;
        if(currentProduct.AssociatedParts.contains(selectedPart)){
            Alerts(8);
            return;
        }
            //Update the AssociatedParts list and refresh the tableview.
            currentProduct.AssociatedParts.add(selectedPart);
            AssociatedPartsTable.refresh();
    }

    /**
     * Removes part from Associated Parts list.
     */
    public void removePart() {
        //Identify current product
        Product currentProduct = null;
        for (Product allProduct : AllProducts) {
            if (allProduct.getID() == Integer.parseInt(IDTxt.getText())) {
                currentProduct = allProduct;
            }
        }

        //Ensure that a part has been selected.
        if(AssociatedPartsTable.getSelectionModel().getSelectedItem() == null){
            Alerts(2);
            return;
        }
        //Identify the selected part
        Part selectedPart = AssociatedPartsTable.getSelectionModel().getSelectedItem();

        //confirm removal.
        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setContentText("Are you sure you want to delete " + selectedPart.getName() + " from the parts list.");
        PartsTable.getSelectionModel().clearSelection();
        Product finalCurrentProduct = currentProduct;
        alert1.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK){
                //Remove part from Associated list and update the table view.
                assert finalCurrentProduct != null;
                finalCurrentProduct.deleteAssociatedPart( selectedPart);
            }
        });
    }

    /**
     * Update existing product.
     */
    // Check to make sure at least 1 Associated part.
   public void updateProduct() throws IOException {
       //FIXME The rubric says that the product add screen should add one or more parts, but the product modify screen can have  zero, one or more parts.
        /*if (AssociatedPartsTable.getItems().isEmpty()) {
            Alerts(10);
            return;
        }*/

        //Identify current product.
        Product currentProduct = lookupProduct(Integer.parseInt(IDTxt.getText()));

        //Check for blank input fields.
        if(nameTxt.getText().isEmpty() || stockTxt.getText().isEmpty() || priceTxt.getText().isEmpty() ||
        maxTxt.getText().isEmpty() || minTxt.getText().isEmpty()){
            Alerts(7);
            return;
        }

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

        //Get User inputs
        int ID = Integer.parseInt(IDTxt.getText());
        String name = nameTxt.getText();
        double price = Double.parseDouble(priceTxt.getText());
        int stock = Integer.parseInt(stockTxt.getText());
        int min = Integer.parseInt(minTxt.getText());
        int max = Integer.parseInt(maxTxt.getText());
        //Convert price into a 2 decimal place double.
        DecimalFormat df = new DecimalFormat("#.##");
        price = Double.parseDouble(df.format(price));

        //Check for price inconsistencies.
        for (int i = 0; i < currentProduct.AssociatedParts.size(); i++) {
            double currentValue = 0.00;
            currentValue = currentValue + currentProduct.AssociatedParts.get(i).getPrice();
            if (currentValue > price || price < .01) {
                Alerts(9);
                return;
            }
        }

            //Check for logical rules regarding inventory.
            if (stock < min || stock > max || stock < 1 || min < 0) {
                Alerts(4);
                return;
            }

            //Add new product to the AllProducts list.
        Product product = new Product(ID, name, price, stock, min, max);
            product.AssociatedParts.addAll(currentProduct.AssociatedParts);
            //Find the index for the current product.
        int index = 0;
        for(int i = 0; i < AllProducts.size(); i++){
            if(AllProducts.get(i).getID() == currentProduct.getID()){
                index = i;
            }
        }
        //call the inventory method to update.
        Inventory.updateProduct(index, product);
        backToMain();
    }

    /**
     * Navigates to Main screen
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
     * Receive product from main screen.
     */
    public void sendProduct(Product product) {

        IDTxt.setText(String.valueOf(product.getID()));
        nameTxt.setText(product.getName());
        priceTxt.setText(String.valueOf(product.getPrice()));
        stockTxt.setText(String.valueOf(product.getStock()));
        maxTxt.setText(String.valueOf(product.getMax()));
        minTxt.setText(String.valueOf(product.getMin()));

        //Get values from the associatedParts list and add them to the table.
        AssociatedPartsTable.setItems(product.getAllAssociatedParts());
        a_partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        a_partInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        a_partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        a_partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Get values for Parts Table.
        PartsTable.setItems(Inventory.getAllParts());
        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

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

        //Wrap the Parts filtered list into a sorted list then bind sorted list to the TableView
        SortedList<Part> partSortedList = new SortedList<>(partsFilteredList);
        partSortedList.comparatorProperty().bind(PartsTable.comparatorProperty());

        //Set the sorted and filtered objects into the TableView.
        PartsTable.setItems(partSortedList);


    }
}
