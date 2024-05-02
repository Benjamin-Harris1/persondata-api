package edu.persondata.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Person {
    private String firstName;
    private String middleName;
    private String lastName;

    public Person(String firstName, String middleName, String lastName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public String getFullName(){
        return getFirstName() + " " + (getMiddleName() != null ? getMiddleName() + " " : "") + getLastName();
    }
    public void setFullName(String fullName){
        if (fullName == null || fullName.isEmpty()){
            return;
        } else {
            int firstSpace = fullName.indexOf(' ');
            int lastSpace = fullName.lastIndexOf(' ');

            setFirstName(fullName.substring(0, firstSpace));
            if(firstSpace != lastSpace){
                setMiddleName(fullName.substring(firstSpace + 1, lastSpace));
                setLastName(fullName.substring(lastSpace + 1));
            } else {
                setLastName(fullName.substring(firstSpace + 1));
            }
        }
    }

}
