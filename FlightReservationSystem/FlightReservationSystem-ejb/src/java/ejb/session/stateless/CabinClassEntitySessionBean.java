/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.CabinClass;
import entity.Employee;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class CabinClassEntitySessionBean implements CabinClassEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static Validator validator = validatorFactory.getValidator();
    
    @Override
    public long createCabinClass(CabinClass cabinClass) {
        Set<ConstraintViolation<CabinClass>> constraints = validator.validate(cabinClass);
        if (constraints.size() == 0) {
            validator.validate(cabinClass);
            em.persist(cabinClass);
            em.flush();
            return cabinClass.getId();
        } else {
            return -1;
        }
    }
    
    @Override
    public List<CabinClass> recreateCabinClass(List<CabinClass> list, AircraftConfiguration aircraftConfiguration) {
        List<CabinClass> newList = new ArrayList<CabinClass>();
        for (CabinClass cabinClass : newList) {
            CabinClass newCabinClass = new CabinClass(cabinClass.getCabinClassName(), cabinClass.getNumAisles(), cabinClass.getNumRows(), cabinClass.getNumSeatsAbreast(), cabinClass.getSeatingConfiguration());
            newCabinClass.getAircraftConfigurationList().size();
            newCabinClass.getAircraftConfigurationList().add(aircraftConfiguration);
            em.persist(newCabinClass);
            em.flush();
            newList.add(newCabinClass);
        }
        return newList;
    }


}
