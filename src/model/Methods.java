package model;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Methods {

    //Alerts that are reused throughout the program.
    public static void Alerts(int alertType){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("You did it Wrong!");
        alert.setHeight(500);
        switch (alertType){
            case 1:
                alert.setContentText("""
                The following fields may only contain positive numbers:\s
                Inventory, Price, Minimum, Maximum\s
                and (if applicable) Machine ID.""");
                break;
            case 2:
                alert.setContentText("You didn't select anything.");
                break;
            case 3:
                alert.setContentText("Price must be a positive value.");
                break;
            case 4:
                alert.setContentText("""
                        Maximum inventory must be larger than minimum inventory.
                        Inventory must be between min and max.
                        Minimum inventory cannot be a negative number.""");
                break;
            case 5:
                alert.setContentText("Inventory, maximum, minimum, and machineID must be positive whole numbers.");
                break;
            case 6:
                alert.setContentText("Price must be convertible into a number with two decimal places: \n" +
                        "I.E.  29.99    or    .01    or    10    or    109.0923893");
                break;
            case 7:
                alert.setContentText("All fields are required.");
                break;
            case 8:
                alert.setContentText("That item has already been added.");
                break;
            case 9:
                alert.setContentText("Price cannot be negative. \n" +
                        "Product price cannot be less than the combined price of its associated parts.");
                break;
            case 10:
                alert.setContentText("Products must contain at least one associated part.");
                break;
            case 11:
                alert.setContentText("You do not have a part to modify.  I will route you back to the main screen.");
                break;
            case 12:
                alert.setContentText("Product cannot be deleted until all associated parts are removed.\n" +
                        "You can remove associated parts by selecting a product and clicking  'Modify'");
                break;
        }
        alert.show();
    }

    //Exits the program.
    public static void ExitHere(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation, You are exiting the program.");
        alert.setContentText("Press OK to exit.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK){
                System.exit(0);
            }
        });
    }

    //Checks if the input of a text field can be converted into an integer.
    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    //Checks if the input of a text field can be converted into a double.
    public static boolean isDouble(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }



}
