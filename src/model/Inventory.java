package model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {

    /**
     * Create an observable array list for each of the Parts and Products objects.
     */
    public static final ObservableList<Part> AllParts = FXCollections.observableArrayList();

    public static final ObservableList<Product> AllProducts = FXCollections.observableArrayList();

    /**
     * Method to add a Part to the PartList.
     */
    public static void addPart (Part part){
        AllParts.add(part);
    }

    /**
     * Method to add a Product to the ProductList
     */
    public static void addProduct(Product product){
        AllProducts.add(product);
    }

    /**
     * Method to locate a part in the PartList.
     */
    public static Part lookupPart(int partID){
        Part part = null;
        for (Part allPart : AllParts) {
            if (partID == allPart.getId()) {
                part = allPart;
            }
        }
        return part;
    }           //this function is integrated into the update part function and the search field function and is redundant here.

    /**
     * Method to locate a part in the PartList.
     */
    public static ObservableList<Part> lookupPart(String partName) {
        Part part = null;
        for (Part allPart : AllParts) {
            if (partName.equals(allPart.getName())) {
                part = allPart;
            }
        }
        return (ObservableList<Part>) part;
    }       //This function is not necessary, as I have instead used a listener search field to make searches on all tables more responsive.

    /**
     * Method to locate a Product in the ProductList.
     */
    public static Product lookupProduct(int productID){
        Product product = null;
        for (Product allProduct : AllProducts) {
            if (productID == allProduct.getID()) {
                product = allProduct;
            }
        }
        return product;
    }

    /**
     * Method to locate a Product in the ProductList.
     */
    public static ObservableList<Product> lookupProduct(String productName){
        for (Product allProduct : AllProducts) {
            Product product = null;
            if (productName.equals(allProduct.getName())) {
                product = allProduct;
            }
            return (ObservableList<Product>) product;
        }
        return null;
    }    //This function is not necessary, as I have instead used a listener search field to make searches on all tables more responsive.


    /**
     * Method to change one or more values in a Part object.
     */
    public static void updatePart(int index, Part selectedPart){
        AllParts.set(index, selectedPart);
            }

    /**
     * Method to change one or more values in a Product object.
     */
    public static void updateProduct(int index, Product selectedProduct){
        AllProducts.set(index, selectedProduct);
    }

    /**
     * Method to delete a part from the PartList.
     */
    public static boolean deletePart(Part selectedPart){
        int i;
        for (i = 0; i < AllParts.size(); i ++ ) {
            if (AllParts.get(i).getId() == selectedPart.getId()){
                System.out.println("You have deleted "+ AllParts.get(i).getName() + " from the Parts list.");
                AllParts.remove(i);
                return true;
            }
        }
       return false;
    }

    /**
     * Method to delete product from the ProductList.
     */
    public static boolean deleteProduct(Product selectedProduct){
        int i;
        for (i = 0; i < AllProducts.size(); i++){
            if (AllProducts.get(i).getID() == selectedProduct.getID()){
                AllProducts.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Method to display the PartList in the TableView.
     */
    public static ObservableList<Part> getAllParts(){
        return AllParts;
    }

    /**
     * Method to display the ProductList in the TableView.
     */
    public static ObservableList<Product> getAllProducts(){
        return AllProducts;
    }

    /**
     * Populate tables with default data when the program starts.
     */
    public static void loadDefaultSet() {
        InHouse brakes = new InHouse(1, "Brakes", 12.99, 10, 2, 20, 101);
        Inventory.addPart(brakes);
        Outsourced wheels = new Outsourced(2, "Wheel", 11.00, 16, 2, 20, "Alamo Wheels");
        Inventory.addPart(wheels);
        InHouse seat = new InHouse(3, "Seat", 10.00, 10, 1, 10, 102);
        Inventory.addPart(seat);
        Product giantBike = new Product(1000, "Giant Bike", 299.99, 5, 1, 5);
        giantBike.addAssociatedPart(brakes);
        Inventory.addProduct(giantBike);
        Product tricycle = new Product(1001, "Tricycle", 99.99, 3, 1, 5);
        tricycle.addAssociatedPart(brakes);
        tricycle.addAssociatedPart(wheels);
        tricycle.addAssociatedPart(seat);
        Inventory.addProduct(tricycle);
    }
}
