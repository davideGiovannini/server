package introsde.assignment.model;

import introsde.assignment.dao.LifeCoachDao;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p")
@Getter
@Setter
@ToString
@XmlRootElement(name = "person")
@XmlType(propOrder = {"personId","firstname", "lastname", "birthdate", "healthProfile"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Person {
    private String firstname;
    private String lastname;

    @Temporal(TemporalType.DATE) // defines the precision of the date attribute
    private Date birthdate;


    @Id
    @GeneratedValue(generator = "sqlite_person")
    @TableGenerator(name = "sqlite_person")
    private int personId;


    @XmlTransient
    @OneToMany(mappedBy = "personId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Measurement> history;


    @Transient
    @XmlTransient
    private List<Measurement> healthProfile;

    @XmlElementWrapper(name = "healthProfile")
    @XmlElement(name = "measureType")
    public List<Measurement> getHealthProfile(){
        healthProfile = Measurement.getHealthProfileOf(this);
        return  healthProfile;
    }

    public void setHealthStatus(List<Measurement> hprofile){
        healthProfile = hprofile;
    }

    public List<Measurement> getHealthProfileTransient(){
        return healthProfile;
    }


    public Person(String firstname, String lastname, Date birthdate) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
    }

    public Person() {
        this.firstname = "Pinco";
        this.lastname = "Pallino";
        this.birthdate = new Date();
        this.history = new ArrayList<>(5);
    }

    public void mergeWith(Person personBean){
        if (personBean.getFirstname() != null) {
            firstname = personBean.getFirstname();
        }
        if (personBean.getLastname() != null) {
            lastname = personBean.getLastname();
        }
        if (personBean.getBirthdate() != null) {
            birthdate = personBean.getBirthdate();
        }
    }


    // Queries

    public static Person getPersonById(Long personId) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        Person p = em.find(Person.class, personId);
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }

    public static List<Person> getAll() {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        List<Person> list = em.createNamedQuery("Person.findAll", Person.class)
                .getResultList();
        LifeCoachDao.instance.closeConnections(em);

        return list;
    }
}
