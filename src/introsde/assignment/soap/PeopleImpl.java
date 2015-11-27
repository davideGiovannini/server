package introsde.assignment.soap;

import introsde.assignment.dao.LifeCoachDao;
import introsde.assignment.model.Measurement;
import introsde.assignment.model.Person;
import introsde.assignment.model.presentation.*;

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
    public PersonList readPersonList() {
        System.out.println("Getting list of people...");
        return new PersonList(Person.getAll());
    }

    @Override
    public PersonBean readPerson(Long id) {
        return PersonBean.from(Person.getPersonById(id));
    }

    @Override
    public void updatePerson(PersonBean person) {
        //TODO where to get ID ?
        long id = 0;
        System.out.println("--> Updating Person... " + id);
        System.out.println("--> " + person.toString());

        Response res;
        Person existing = Person.getPersonById(id);

        if (existing != null) {
            existing.mergeWith(person);

            LifeCoachDao.updateEntity(existing);
            //TODO return something in failure cases ?
        }
    }

    @Override
    public PersonBean createPerson(PersonBean person) {
        System.out.println("Creating new person...123");
        Person personDb = person.toDb();
        List<Measurement> history = new ArrayList<>(person.getHProfile().size());

        for (CurrentMeasureBean m : person.getHProfile()) {
            history.add(m.toDb(personDb));
        }
        personDb.setHistory(history);

        return PersonBean.from(LifeCoachDao.saveEntity(personDb));

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
    public MeasureHistory readPersonHistory(Long id, String measureType) {

        Person person = Person.getPersonById(id);
        if (person != null) {

            return new MeasureHistory(Measurement.getHistoryOf(person, measureType));
        }
        return null;
    }

    @Override
    public TypesList readMeasureTypes() {
        List<String> measures = Measurement.getTypes();
        return new TypesList(measures);
    }

    @Override
    public MeasureBean readPersonMeasure(Long id, String measureType, Long mid) {
        // TODO check that mid belongs to id ?
        return MeasureBean.from(Measurement.findMeasurement(mid, measureType));
    }

    @Override
    public MeasureBean savePersonMeasure(Long id, MeasureBean measure) {
        System.out.println("Posting new measure");
        Person person = Person.getPersonById(id);
        if (person != null) {
            // TODO fix this line => new model that measure knows about its type
            Measurement ret = LifeCoachDao.saveEntity(measure.toDb("todo", person));
            return MeasureBean.from(ret);
        }
        return null;
    }

    @Override
    public void updatePersonMeasure(Long id, MeasureBean measure) {
        System.out.println("Updating new measure");
        Person person = Person.getPersonById(id);
        if (person != null) {

            //TODO type is inside measurebean now also mid apparently
            Measurement existing = Measurement.findMeasurement(0, "TODO");

            if (existing != null) {
                existing.setValue(measure.getValue());// TODO merge function

                LifeCoachDao.updateEntity(existing);
            }
        }
    }
}