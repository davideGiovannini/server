package introsde.assignment.model;

import introsde.assignment.dao.LifeCoachDao;
import introsde.assignment.model.presentation.CurrentMeasureBean;
import introsde.assignment.model.presentation.MeasureBean;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by demiurgo on 11/9/15.
 */


@Entity

@NamedQuery(name = "MeasureType.distinctTypes", query = "SELECT DISTINCT m.measure FROM Measurement m")
@Getter
@Setter
@ToString
public class Measurement {

    private String measure;

    private float value;

    @Id
    @GeneratedValue
    @TableGenerator(name = "sqlite_fmeasure")
    private long mid;

    @Temporal(TemporalType.TIMESTAMP) // defines the precision of the date attribute
    private Date created;

    @ManyToOne()
    @JoinColumn
    private Person personId;


    public Measurement() {
        created = new Date();
    }

    public Measurement(String measure, float value) {
        this.measure = measure;
        this.value = value;
        created = new Date();
    }


    public static List<String> getTypes() {
        EntityManager em = LifeCoachDao.instance.createEntityManager();


        List<String> list = em.createNamedQuery("MeasureType.distinctTypes", String.class)
                .getResultList();
        LifeCoachDao.instance.closeConnections(em);
        return list;
    }


    public static List<CurrentMeasureBean> getHealthProfileOf(Person p) {
        List<Measurement> result;
        EntityManager em = LifeCoachDao.instance.createEntityManager();

        Query query = em.createQuery("select m from Measurement m WHERE m.personId = :id GROUP BY m.measure ORDER BY m.created").setParameter("id", p);

        result = query.getResultList();

        LifeCoachDao.instance.closeConnections(em);


        List<CurrentMeasureBean> convertedResult = new ArrayList<>(result.size());

        for (Measurement m : result) {
            convertedResult.add(CurrentMeasureBean.from(m));
        }
        return convertedResult;
    }


    public static Measurement findMeasurement(long mid, String type) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();

        Measurement m = em.find(Measurement.class, mid);
        if (!m.getMeasure().equals(type)) {
            m = null;
        }

        LifeCoachDao.instance.closeConnections(em);
        return m;
    }


    public static List<MeasureBean> getHistoryOf(Person p, String type) {
        List<Measurement> result;
        EntityManager em = LifeCoachDao.instance.createEntityManager();

        Query
                query = em.createQuery("select m from Measurement m WHERE m.personId = :id and m.measure = :type ORDER BY m.created")
                .setParameter("id", p).setParameter("type", type);


        result = query.getResultList();

        LifeCoachDao.instance.closeConnections(em);

        List<MeasureBean> convertedResult = new ArrayList<>(result.size());

        for (Measurement m : result) {
            convertedResult.add(MeasureBean.from(m));
        }
        return convertedResult;
    }


}
