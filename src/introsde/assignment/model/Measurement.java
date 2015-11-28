package introsde.assignment.model;

import introsde.assignment.dao.LifeCoachDao;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.Date;
import java.util.List;

/**
 * Created by demiurgo on 11/9/15.
 */


@Entity
@NamedQuery(name = "MeasureType.distinctTypes", query = "SELECT DISTINCT m.measureType FROM Measurement m")
@Getter
@Setter
@ToString
@XmlRootElement(name="measure")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"mid", "dateRegistered", "measureType", "measureValue", "measureValueType"})
public class Measurement {

    @Id
    @GeneratedValue
    @TableGenerator(name = "sqlite_fmeasure")
    private long mid;


    @Temporal(TemporalType.TIMESTAMP) // defines the precision of the date attribute
    private Date dateRegistered;

    private String measureType;

    private String measureValue;

    private String measureValueType;


    @ManyToOne()
    @JoinColumn
    @XmlTransient
    private Person personId;


    public Measurement() {
        dateRegistered = new Date();
    }

    public Measurement(String measureType, String measureValue) {
        this.measureType = measureType;
        this.measureValue = measureValue;
        dateRegistered = new Date();
    }


    public static List<String> getTypes() {
        EntityManager em = LifeCoachDao.instance.createEntityManager();


        List<String> list = em.createNamedQuery("MeasureType.distinctTypes", String.class)
                .getResultList();
        LifeCoachDao.instance.closeConnections(em);
        return list;
    }


    public static List<Measurement> getHealthProfileOf(Person p) {
        List<Measurement> result;
        EntityManager em = LifeCoachDao.instance.createEntityManager();

        Query query = em.createQuery("select m from Measurement m WHERE m.personId = :id GROUP BY m.measureType ORDER BY m.dateRegistered").setParameter("id", p);

        result = query.getResultList();

        LifeCoachDao.instance.closeConnections(em);


        return result;
    }


    public static Measurement findMeasurement(long mid, String type) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();

        Measurement m = em.find(Measurement.class, mid);
        if (!m.getMeasureType().equals(type)) {
            m = null;
        }

        LifeCoachDao.instance.closeConnections(em);
        return m;
    }


    public static List<Measurement> getHistoryOf(Person p, String type) {
        List<Measurement> result;
        EntityManager em = LifeCoachDao.instance.createEntityManager();

        Query
                query = em.createQuery("select m from Measurement m WHERE m.personId = :id and m.measureType = :type ORDER BY m.dateRegistered")
                .setParameter("id", p).setParameter("type", type);


        result = query.getResultList();

        LifeCoachDao.instance.closeConnections(em);

        return result;
    }


}
