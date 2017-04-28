package uk.ac.le.school.meal;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by User on 3/24/2017.
 */
public class School {
    private final SimpleStringProperty monthProperty;
    private final SimpleStringProperty nameProperty;
    private final SimpleIntegerProperty nStudentOnRollProperty;
    private final SimpleIntegerProperty nStudentEligibleProperty;
   // private double percentageOfStudentEligible;

    public School(){
        this("","",0,0);
    }

    public School(String month, String name, int numberOfStudentOnRoll, int numberOfStudentEligible){
        this.monthProperty = new SimpleStringProperty(month);
        this.nameProperty = new SimpleStringProperty(name);
        this.nStudentOnRollProperty = new SimpleIntegerProperty(numberOfStudentOnRoll);
        this.nStudentEligibleProperty = new SimpleIntegerProperty(numberOfStudentEligible);
    }

    public String getMonthProperty() {
        return monthProperty.get();
    }

    public void setMonthProperty(String month) {
        this.monthProperty.set(month);
    }

    public String getNameProperty() {
        return nameProperty.get();
    }

    public void setNameProperty(String name) {
        this.nameProperty.set(name);
    }

    public int getNStudentOnRollProperty() {
        return nStudentOnRollProperty.get();
    }

    public void setNStudentOnRollProperty(int nStudentOnRollProperty) {
        this.nStudentOnRollProperty.set(nStudentOnRollProperty);
    }

    public int getNStudentEligibleProperty() {
        return nStudentEligibleProperty.get();
    }

    public void setNStudentEligibleProperty(int nStudentEligibleProperty) {
        this.nStudentEligibleProperty.set(nStudentEligibleProperty);
    }

    /*
    @Override
    public String toString() {
        return String.format("%s %s %d %d %s", new Object[]{getMonthProperty(), getNameProperty(), getnStudentOnRollProperty(), getnStudentEligibleProperty()});
    }*/
}
