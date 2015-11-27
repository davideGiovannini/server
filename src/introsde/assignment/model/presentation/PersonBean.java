package introsde.assignment.model.presentation;

import introsde.assignment.model.Measurement;
import introsde.assignment.model.Person;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.*;
import java.util.Date;
import java.util.List;

/**
 * Created by demiurgo on 11/17/15.
 */


@XmlRootElement(name = "person")
@XmlType(propOrder = {"firstname", "lastname", "birthdate", "hProfile"})
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PersonBean {
    @XmlAttribute(name = "id")
    private int personId;

    private String firstname;
    private String lastname;

    @Temporal(TemporalType.DATE) // defines the precision of the date attribute
    private Date birthdate;


    @XmlElementWrapper(name = "healthProfile")
    @XmlElement(name = "measureType")
    private List<CurrentMeasureBean> hProfile;


    public PersonBean(String firstname, String lastname, Date birthdate){
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
    }


    //Create a PersonBean from a Person
    public static PersonBean from(Person dbPerson ){
        PersonBean person =  new PersonBean();
        person.setPersonId(dbPerson.getPersonId());
        person.setFirstname(dbPerson.getFirstname());
        person.setLastname(dbPerson.getLastname());

        person.setBirthdate(dbPerson.getBirthdate());
        person.setHProfile(Measurement.getHealthProfileOf(dbPerson));

        return person;
    }

    //Convert a PersonBean to a Person
    public Person toDb(){
        Person person = new Person();
        person.setFirstname(firstname);
        person.setLastname(lastname);
        person.setBirthdate(birthdate);
        return person;
    }
}
