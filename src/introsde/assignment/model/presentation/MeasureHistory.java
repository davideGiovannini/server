package introsde.assignment.model.presentation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by demiurgo on 11/16/15.
 */

@XmlRootElement(name = "measureHistory")
@XmlAccessorType(XmlAccessType.FIELD)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MeasureHistory {

    @XmlElement(name = "measure")
    private List<MeasureBean> measurements;

}
