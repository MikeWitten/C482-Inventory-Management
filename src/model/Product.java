package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {
    int ID;
    String name;
    double price;
    int stock;
    int min;
    int max;

    /**
     * create a constructor.
     */
    public Product(int ID, String name, double price, int stock, int min, int max) {
        this.ID = ID;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @return the iD.
     */
    public int getID() {
        return ID;
    }

    /**
     * @param ID the iD to set.
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the stock.
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return get the min.
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the min to set.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the max.
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the max to set.
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Create an ObservableList for AssociatedParts.
     */
    public final ObservableList<Part> AssociatedParts = FXCollections.observableArrayList();

    /**
     * Method to Add an associatedPart to the Product.
     */
    void addAssociatedPart(Part part){
        AssociatedParts.add(part);
    }

    /**
     * Method to delete an associatedPart from the Product.
     */
    public boolean deleteAssociatedPart( Part part) {
        AssociatedParts.remove(part);
        return true;
    }

    /**
     * Return the observable list for associatedParts.
     */
    public ObservableList<Part> getAllAssociatedParts(){
        return AssociatedParts;
    }
}
