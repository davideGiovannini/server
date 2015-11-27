package introsde.assignment.model.presentation;

import introsde.assignment.model.Measurement;
import introsde.assignment.model.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by demiurgo on 11/17/15.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "measureType")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"measure", "value"})
public class CurrentMeasureBean {

    private String measure;
    private float value;


    //Creates a CurrentMeasureBean from a Measurement
    public static CurrentMeasureBean from(Measurement measurement){
        CurrentMeasureBean measure = new CurrentMeasureBean();
        measure.setMeasure(measurement.getMeasure());
        measure.setValue(measurement.getValue());
        return measure;
    }


    //Convert from CurrentMeasureBean to a Measurement with an optional Person parameter
    public Measurement toDb(){
        Measurement measurement = new Measurement(measure, value);
        return measurement;
    }

    public Measurement toDb(Person person){
        Measurement measurement = toDb();
        measurement.setPersonId(person);
        return measurement;
    }

}
