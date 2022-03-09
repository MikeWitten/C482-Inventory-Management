package controller;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import java.util.Objects;
import java.util.ResourceBundle;
import static model.Inventory.AllParts;
import static model.Inventory.AllProducts;
import static model.Methods.Alerts;
import static model.Methods.ExitHere;

public class MainScreen implements Initializable {

    public Button partAdd;
    public Label label1;

    /**
     *Navigate to the AddPartScreen.
     */
    public void toPartAdd(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AddInHousePart.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 550);
        stage.setTitle("Add Part - In House");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Navigate to the ModifyPartScreen.
     */
    public void toPartMod() throws IOException {
        //Send alert to user if they have not selected an item.
        if (PartsTable.getSelectionModel().getSelectedItem() == null) {
            Alerts(2);
            return;
        }
        Part selectedPart = PartsTable.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ModPart.fxml"));
        loader.load();
        ModPart controller = loader.getController();
        controller.sendPart(selectedPart);
        Stage stage = (Stage) (label1).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setTitle("Modify Part");
        stage.setScene(new Scene(scene));
        stage.show();

        //Identify weather the selected item is an InHouse or Outsourced part and direct them to the correct screen.
/*        String rootMod = "/view/ModInHousePart.fxml";
        Part selectedPart = PartsTable.getSelectionModel().getSelectedItem();
        if (selectedPart.getClass().getSimpleName().equals("Outsourced")) {
            rootMod = "/view/ModOutsourcedPart.fxml";
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(rootMod));
        loader.load();
        //Identify the correct controller for the selected part.
        if (rootMod.equals("/view/ModInHousePart.fxml")) {
            ModInHousePart controller = loader.getController();
            controller.sendPart(selectedPart);
        } else {
            ModOutsourcedPart controller = loader.getController();
            controller.sendPart(selectedPart);
        }
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setTitle("Modify Part");
        stage.setScene(new Scene(scene));
        stage.show();*/
    }

    /**
     * Navigates to the AddProductScreen.
     */
    public void toProductAdd() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/ProductAddScreen.fxml")));
        Stage stage = (Stage) (label1).getScene().getWindow();
        Scene scene = new Scene(root, 1000, 550);
        stage.setTitle("Add Product Screen");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Navigate to the ModifyProductScreen.
     */
    public void toProductMod() throws IOException {
        if(ProductsTable.getSelectionModel().getSelectedItem() == null){
            Alerts(2); // No selected item.
            return;
        }
        //Pass the selected product to the modification screen.
        Product selectedProduct = (ProductsTable.getSelectionModel().getSelectedItem());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ProductModScreen.fxml"));
        loader.load();
        ProductModScreen controller = loader.getController();
        controller.sendProduct(selectedProduct);
        Stage stage = (Stage) (label1).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setTitle("Modify Product Screen");
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Navigates to the ExitConformationScreen.
     */
    public void toExit() {
    ExitHere();    //Confirmation alert to ask if the user wants to exit the program.
    }

    /**
     * Delete part from the Parts list.
     */
    public void deletePart(){
        Part selectedPart = PartsTable.getSelectionModel().getSelectedItem();
        if ( PartsTable.getSelectionModel().getSelectedItem() == null){
            Alerts(2);  //Alert if no item is selected.
            return;
        }

        //pop up confirmation of successful part deletion.
        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setContentText("Are you sure you want to delete " + selectedPart.getName() + " from the parts list.");
        PartsTable.getSelectionModel().clearSelection();
        alert1.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK){
                //Delete item from the products list and refresh the table.
                Inventory.deletePart(selectedPart);
                PartsTable.refresh();
            }
        });
    }

    /**
     * Delete product from the Product list.
     */
    public void deleteProduct() {
        Product selectedProduct = ProductsTable.getSelectionModel().getSelectedItem();
        if (ProductsTable.getSelectionModel().getSelectedItem() == null){
            Alerts(2);  //Alert if no item is selected.
            return;
        }
        if(!ProductsTable.getSelectionModel().getSelectedItem().AssociatedParts.isEmpty()){
            Alerts(12);
            return;
        }

        //Pop up to confirm the deletion of the product.
        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setContentText("Are you sure you want to delete " + selectedProduct.getName() + " from the products list.");
        ProductsTable.getSelectionModel().clearSelection();
        alert1.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK){
                //Delete item from the products list and refresh the table.
                Inventory.deleteProduct(selectedProduct);
                ProductsTable.refresh();
            }
        });
    }


    /**
     * Identify the PartsTable and ProductsTable search fields.
     */
    public TextField partsFilterField;
    public TextField productsFilterField;

    /**
     *Set up the Parts Table.
     */
    public TableView<Part> PartsTable;
    public TableColumn<Object, Object> partID;
    public TableColumn<Object, Object> partName;
    public TableColumn<Object, Object> partInventory;
    public TableColumn<Object, Object> partPrice;

    /**
     * Set up the Products Table.
     */
    public TableView<Product> ProductsTable;
    public TableColumn<Object, Object> productID;
    public TableColumn<Object, Object> productName;
    public TableColumn<Object, Object> productInventory;
    public TableColumn<Object, Object> productPrice;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Set values for both tables.
        PartsTable.setItems(Inventory.getAllParts());
        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        ProductsTable.setItems(Inventory.getAllProducts());
        productID.setCellValueFactory(new PropertyValueFactory<>("iD"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("price"));


        //Creating an easy to use searchbar.
        //Wrap the Parts Observable list in a Parts FilteredList initialized to display all data.
        FilteredList<Part> partsFilteredList = new FilteredList<>(AllParts, p -> true);

        //Create a listener.
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

        //Wrap the filtered list in a sorted list, then bind the tableview.
        SortedList<Part> partSortedList = new SortedList<>(partsFilteredList);
        partSortedList.comparatorProperty().bind(PartsTable.comparatorProperty());

        //Set the sorted and filtered objects into the TableView.
        PartsTable.setItems(partSortedList);


        //Create a text based search field for the ProductsTable.
        //Wrap the Products Observable list in a Products FilteredList initialized to display all data.
        FilteredList<Product> productsFilteredList = new FilteredList<>(AllProducts, b -> true);

        // Create a listener to update whenever the productsFilterField text field changes.
        // Compare the filter text with productName and productID.
        productsFilterField.textProperty().addListener((observable, oldValue, newValue) -> productsFilteredList.setPredicate(product -> {
            // Display all objects if text field is empty.
            if (newValue == null || newValue.isEmpty()){
                return true;
            }
            // Compare productName with filter text.
            String lowerCaseFilter = newValue.toLowerCase();
            if (product.getName().toLowerCase().contains(lowerCaseFilter)){
                return true;
            }
            //Compare productID with filter text.
            else return String.valueOf(product.getID()).contains(lowerCaseFilter);
        }));
         //Wrap the Products filtered list into a sorted list.
         //Then bind sorted list to the TableView
        SortedList<Product> productSortedList = new SortedList<>(productsFilteredList);
        productSortedList.comparatorProperty().bind(ProductsTable.comparatorProperty());

        //Set the sorted and filtered objects into the TableView.
        ProductsTable.setItems(productSortedList);

    }

}
