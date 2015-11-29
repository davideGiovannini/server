package introsde.assignment.soap;

import introsde.assignment.dao.LifeCoachDao;
import introsde.assignment.model.Measurement;
import introsde.assignment.model.Person;

import javax.jws.WebService;
import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by demiurgo on 11/27/15.
 */
@WebService(endpointInterface = "introsde.assignment.soap.People",
        serviceName = "PeopleService")
public class PeopleImpl implements People {
    @Override
    public List<Person> readPersonList() {
        System.out.println("Getting list of people...");
        return Person.getAll();
    }

    @Override
    public Person readPerson(Long id) {
        return Person.getPersonById(id);
    }

    @Override
    public void updatePerson(Person person) {
        //TODO check it is working
        long id = person.getPersonId();
        System.out.println("--> Updating Person... " + id);


        Response res;
        Person existing = Person.getPersonById(id);

        if (existing != null) {
            existing.mergeWith(person);

            LifeCoachDao.updateEntity(existing);
            //TODO return something in failure cases ?
        }
    }

    @Override
    public Person createPerson(Person person) {
        System.out.println("Creating new person...123");

        List<Measurement> history = new ArrayList<>(person.getHealthProfileTransient().size());

        for (Measurement m : person.getHealthProfileTransient()) {
            m.setPersonId(person);
            history.add(m);
        }
        person.setHistory(history);

        return LifeCoachDao.saveEntity(person);

    }

    @Override
    public void deletePerson(Long id) {
        Person c = Person.getPersonById(id);
        if (c != null) {

            LifeCoachDao.removeEntity(c);
            System.out.println("Removing person");
        }
        //TODO return something in failure cases ?
    }

    @Override
    public List<Measurement> readPersonHistory(Long id, String measureType) {

        Person person = Person.getPersonById(id);
        if (person != null) {
            return Measurement.getHistoryOf(person, measureType);
        }
        return null;
    }

    @Override
    public List<String> readMeasureTypes() {
        List<String> measures = Measurement.getTypes();
        return measures;
    }

    @Override
    public Measurement readPersonMeasure(Long id, String measureType, Long mid) {
        // TODO check that mid belongs to id ?
        return Measurement.findMeasurement(mid, measureType);
    }

    @Override
    public Measurement savePersonMeasure(Long id, Measurement measure) {
        System.out.println("Posting new measure");
        Person person = Person.getPersonById(id);
        if (person != null) {
            measure.setPersonId(person);
            Measurement ret = LifeCoachDao.saveEntity(measure);
            return ret;
        }
        return null;
    }

    @Override
    public Measurement updatePersonMeasure(Long id, Measurement measure) {
        System.out.println("Updating new measure");
        Person person = Person.getPersonById(id);
        if (person != null) {

            Measurement existing = Measurement.findMeasurement(measure);

            if (existing != null) {
                existing.setMeasureValue(measure.getMeasureValue());// TODO merge function

                return LifeCoachDao.updateEntity(existing);
            }
        }
        return  null;
    }
}