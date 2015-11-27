package introsde.assignment.model;

import introsde.assignment.dao.LifeCoachDao;
import introsde.assignment.model.presentation.PersonBean;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p")
@Getter
@Setter
@ToString
public class Person {
    private String firstname;
    private String lastname;

    @Temporal(TemporalType.DATE) // defines the precision of the date attribute
    private Date birthdate;


    @Id
    @GeneratedValue(generator = "sqlite_person")
    @TableGenerator(name = "sqlite_person")
    private int personId;


    @OneToMany(mappedBy = "personId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Measurement> history;


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

    public void mergeWith(PersonBean personBean){
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

    public static List<PersonBean> getAll() {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        List<Person> list = em.createNamedQuery("Person.findAll", Person.class)
                .getResultList();
        LifeCoachDao.instance.closeConnections(em);


        List<PersonBean> result = new ArrayList<>(list.size());
        for(Person person: list){
            result.add(PersonBean.from(person));
        }
        return result;
    }


    public static List<PersonBean> getAllThatMatch(@NotNull String type, Integer min, Integer max) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        Query query;
        if(min != null && max != null) {
            query = em.createQuery("select m.personId from Measurement m WHERE m.value > :min and m.value < :max and m.measure = :type GROUP BY m.personId ORDER BY m.created")
            .setParameter("type", type).setParameter("min", min).setParameter("max", max);
        }else if(min != null){
            query = em.createQuery("select m.personId from Measurement m WHERE m.value > :min and m.measure = :type GROUP BY m.personId ORDER BY m.created")
                    .setParameter("type", type).setParameter("min", min);
        }else if (max != null){
            query = em.createQuery("select m.personId from Measurement m WHERE m.value < :max and m.measure = :type GROUP BY m.personId ORDER BY m.created")
                    .setParameter("type", type).setParameter("max", max);
        }else{
            //Should never reach this statement
            System.err.println("Person.getAllThatMatch: Both min and max were null!");
            return null;
        }


        List<Person> list = query.getResultList();
        LifeCoachDao.instance.closeConnections(em);

        List<PersonBean> result = new ArrayList<>(list.size());
        for(Person person: list){
            result.add(PersonBean.from(person));
        }
        return result;
    }
}
