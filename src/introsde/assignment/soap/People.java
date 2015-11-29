package introsde.assignment.soap;

import introsde.assignment.model.Measurement;
import introsde.assignment.model.Person;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.List;

/**
 * Created by demiurgo on 11/27/15.
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL) //optional
public interface People {


    @WebMethod()
    @WebResult()
    List<Person> readPersonList(); // List | should list all the people in the database (see below Person model to know what data to return here) in your database

    @WebMethod()
    @WebResult()
    Person readPerson(Long id); // Person | should give all the Personal information plus current measures of one Person identified by {id} (e.g., current measures means current healthProfile)

    @WebMethod()
    @WebResult()
    void updatePerson(Person p); // Person | should update the Personal information of the Person identified by {id} (e.g., only the Person's information, not the measures of the health profile)

    @WebMethod()
    @WebResult()
    Person createPerson(Person p); // Person | should create a new Person and return the newly dateRegistered Person with its assigned id (if a health profile is included, create also those measurements for the new Person).

    @WebMethod()
    @WebResult()
    void deletePerson(Long id); //should delete the Person identified by

    @WebMethod()
    @WebResult()
    List<Measurement> readPersonHistory(Long id, String measureType); // List should return the list of values (the history) of {measureType} (e.g. weight) for Person identified by {id}

    @WebMethod()
    @WebResult()
    List<String> readMeasureTypes(); // List should return the list of measures

    @WebMethod()
    @WebResult()
    Measurement readPersonMeasure(Long id, String measureType, Long mid); // Measure should return the value of {measureType} (e.g. weight) identified by {mid} for Person identified by {id}

    @WebMethod()
    @WebResult()
    Measurement savePersonMeasure(Long id, Measurement m); //should save a new measure object {m} (e.g. weight) of Person identified by {id} and archive the old value in the history

    @WebMethod()
    @WebResult()
    Measurement updatePersonMeasure(Long id, Measurement m); // Measure | should update the measure identified with {m.mid}, related to the Person identified by {id}
}
