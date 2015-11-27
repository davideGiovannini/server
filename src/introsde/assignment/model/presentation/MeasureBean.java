package introsde.assignment.model.presentation;

import introsde.assignment.model.Measurement;
import introsde.assignment.model.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
import java.util.Date;

/**
 * Created by demiurgo on 11/17/15.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "measure")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"value", "created"})
public class MeasureBean {

    private float value;

    @XmlAttribute
    private long mid;

    private Date created;

    //Creates a CurrentMeasureBean from a Measurement
    public static MeasureBean from(Measurement measurement) {
        MeasureBean measure = new MeasureBean();
        measure.setMid(measurement.getMid());
        measure.setCreated(measurement.getCreated());
        measure.setValue(measurement.getValue());
        return measure;
    }


    //Convert from CurrentMeasureBean to a Measurement with an optional Person parameter
    public Measurement toDb(String type) {
        Measurement measurement = new Measurement(type, value);
        return measurement;
    }

    public Measurement toDb(String type, Person person) {
        Measurement measurement = toDb(type);
        measurement.setPersonId(person);
        return measurement;
    }

}