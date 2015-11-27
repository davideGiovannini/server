package introsde.assignment.model.presentation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by demiurgo on 11/17/15.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "people")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = "personList")
public class PersonList {

    @XmlElement(name = "person")
    List<PersonBean> personList;

}
