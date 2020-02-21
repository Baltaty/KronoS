
package com.kronos.global.util;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author TeamKronos
 * @version 1.0
 * Masks contain regex used to control the validity of new data input.
 */
public class Mask {
    static String regExsimpleString = "[a-zA-Z_0-9]*";
    static String regExcomplexString = "([a-zA-Z_0-9]* |[\\s]*){0,4}";
    static String regxEpressiondate1 = "^(3[01]|[12][0-9]|0[1-9])-(1[0-2]|0[1-9])-[0-9]{4}$";
    static String regExNumeric = "[0-9]*";
    static String regexDouble = "^[0-9]{1,16}(\\.[0-9]{1,2})?$";

    /**
     * Suppresses spaces of the value contained in the text field.
     * @param field text field
     */
    public static void noSpaces(TextField field) {
        field.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (field.getText() != null) {
                    if (field.getText().length() > 0) {
                        String value = field.getText();
                        value = value.replaceFirst("[ ]", "");
                        field.setText(value);
                    }
                }
            }
        });
    }

    /**
     * Replaces the new value of the text field by the old value if the new value is longer than a defined length.
     * @param field text field
     * @param length max length
     */
    public static void maxField(TextField field, int length) {
        field.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue == null || newValue.length() > length) {
                field.setText(oldValue);
            }
        });
    }

    /**
     * Suppresses spaces at the beginning of the value contained in the text field.
     * @param field text field
     */
    public static void noInitSpace(TextField field) {
        field.lengthProperty().addListener((observable, oldValue, newValue) -> {

            if (field.getText() != null) {
                if (field.getText().length() > 0) {
                    String value = field.getText();
                    value = value.replaceFirst("[^a-zA-Z ][~´]", "");
                    value = value.replaceAll("^ ", "");
                    field.setText(value);

                }
            }
        });
    }

    /**
     * Replaces symbols (characters which are not letters or numbers) contained in the text field by an empty value.
     * @param field text field
     */
    public static void noSymbols(final TextField field) {

        ChangeListener listener = (ChangeListener<Number>) (observable, oldValue, newValue) -> {
            if (field.getText() != null) {
                if (field.getText().length() > 0) {
                    String value = field.getText();
                    value = value.replaceAll("[^a-zA-Z0-9 ]", "");
                    field.setText(value);
                }
            }
        };
        field.lengthProperty().addListener(listener);
    }

    /**
     * Replaces symbols (characters which are not letters or numbers) contained in the text field by an empty value.
     * Gives the possibility to add exceptions to this rule.
     * @param field text field
     * @param exceptions eventual exceptions
     */
    public static void noSymbols(final TextField field, String exceptions) {

        ChangeListener listener = (ChangeListener<Number>) (observable, oldValue, newValue) -> {
            if (field.getText() != null) {
                if (field.getText().length() > 0) {
                    String value = field.getText();
                    value = value.replaceAll("[^a-zA-Z0-9 " + exceptions + "]", "");
                    field.setText(value);
                }
            }
        };
        field.lengthProperty().addListener(listener);
    }


    /**
     * Adds a capital letter to the beginning of the value contained in the text field.
     * @param field text field
     */
    public static void nameField(final TextField field) {
        field.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                String text = field.getText();
                String[] parts = text.split(" ");
                StringBuilder sb = new StringBuilder();
                for (String word : parts) {
                    if (word.length() > 2) {
                        word = word.substring(0, 1).toUpperCase() + word.substring(1);
                    }
                    sb.append(" ").append(word);
                }

                field.setText(sb.toString().replaceFirst(" ", ""));
                field.positionCaret(field.getText().length());
            }
        });
    }


    /**
     * Checks if the text field value has the email format (xxxxx.xxxxx@xxxxx.xxxxx or xxxxx@xxxxx.xxxxx).
     * @param field text field
     * @return true if the format is respected, false otherwise
     */
    public static boolean isEmail(TextField field) { // KeyPressed
        boolean is = false;
        if (!field.getText().isEmpty()) {
            if (field.getText().contains("@") && field.getText().contains(".") && !field.getText().contains(" ")) {

                String user = field.getText().substring(0, field.getText().lastIndexOf('@'));
                String domain = field.getText().substring(field.getText().lastIndexOf('@') + 1, field.getText().length());
                String subdomain = field.getText().substring(field.getText().indexOf(".") + 1, field.getText().length());

                if ((user.length() >= 1) && (!user.contains("@")) &&
                        (domain.contains(".")) && (!domain.contains("@"))
                        && (domain.indexOf(".") >= 1)
                        && (domain.lastIndexOf(".") < domain.length() - 1)
                        && subdomain.length() >= 2) {
                    is = true;
                }
            }
        }
        return is;
    }

    /**
     * Deletes all characters in the text field which are not valid for an email address.
     * @param field text field
     */
    public static void emailField(TextField field) {
        field.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (field.getText() != null) {
                if (field.getText().length() > 0) {
                    String value = field.getText();
                    value = value.replaceAll("[^a-zA-Z0-9@.]", "");
                    field.setText(value);

                }
            }
        });
    }

    /**
     * Deletes all letters for the value contained in the text field.
     * @param textField text field
     */
    public static void noLetters(final TextField textField) {

        textField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (textField.getText() != null) {
                if (textField.getText().length() > 0) {
                    String value = textField.getText();
                    value = value.replaceAll("[a-zA-Zç]", "");
                    textField.setText(value);
                }
            }
        });
    }

    /**
     * Reformats the monetary fields.
     * @param field text field
     * @param locale locale time zone
     */
    public static void monetaryField(final TextField field, Locale locale) {
        AtomicInteger position = new AtomicInteger(1);
        if (locale.toString().equals("pt_BR")) {
            field.lengthProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(() -> {
                String value = field.getText();

                value = value.replaceAll("[^0-9]", "");
                value = value.replaceAll("([0-9])([0-9]{14})$", "$1.$2");
                value = value.replaceAll("([0-9])([0-9]{11})$", "$1.$2");
                value = value.replaceAll("([0-9])([0-9]{8})$", "$1.$2");
                value = value.replaceAll("([0-9])([0-9]{5})$", "$1.$2");
                value = value.replaceAll("([0-9])([0-9]{2})$", "$1,$2");

                field.setText(value);
                field.positionCaret(position.getAndIncrement());
            }));
        } else {
            field.lengthProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(() -> {
                String value = field.getText();

                value = value.replaceAll("[^0-9]", "");
                value = value.replaceAll("([0-9])([0-9]{14})$", "$1,$2");
                value = value.replaceAll("([0-9])([0-9]{11})$", "$1,$2");
                value = value.replaceAll("([0-9])([0-9]{8})$", "$1,$2");
                value = value.replaceAll("([0-9])([0-9]{5})$", "$1,$2");
                value = value.replaceAll("([0-9])([0-9]{2})$", "$1.$2");

                field.setText(value);
                field.positionCaret(position.getAndIncrement());
            }));
        }
        field.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case BACK_SPACE:
                    field.positionCaret(position.decrementAndGet());
                    break;
            }
        });
    }

    /**
     * Checks if the string is a simple string (string without spaces).
     * @param myString string to test
     * @return true if the format is respected, false otherwise
     */
    public static Boolean isSimpleString(String myString) {

        return  myString.matches(regExcomplexString);
    }

    /**
     * Checks if the string respects the format JJ-MM-YYYY.
     * @param myDate string to test
     * @return true if the format is respected, false otherwise
     */
    public static Boolean isDate(String myDate) {

        return myDate.matches(regxEpressiondate1);
    }

    /**
     * Checks if the string is numeric.
     * @param myNumeric string to test
     * @return true if the format is respected, false otherwise
     */
    public static Boolean isNumeric(String myNumeric) {

        return myNumeric.matches(regExNumeric);
    }

    /**
     * Checks the string is a complex string (string which can contain space).
     * @param myComplexString string to check
     * @return true if the format is respected, false otherwise
     */
    public static Boolean isComplexString(String myComplexString) {

        return myComplexString.matches(regExcomplexString);
    }

    /**
     * Checks the string is a double.
     * @param myDouble string to check
     * @return true if the format is respected, false otherwise
     */
    public static Boolean isDouble(String myDouble) {

        return myDouble.matches(regexDouble);
    }

    /**
     * Checks the format of the date and returns one with the correct format.
     * @param myDate date
     * @return a date with the correct format
     * @throws ParseException exception in case the date has a wrong format
     */
    public static int validateDate(Date myDate) throws ParseException {

        Date currentDate = new Date();
        SimpleDateFormat DateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date currentdatecompare = DateFormat.parse(new SimpleDateFormat("dd-MM-yyyy").format(currentDate));
        Date myDateFormat = DateFormat.parse(new SimpleDateFormat("dd-MM-yyyy").format(myDate));


        return currentdatecompare.compareTo(myDateFormat);
    }

}
