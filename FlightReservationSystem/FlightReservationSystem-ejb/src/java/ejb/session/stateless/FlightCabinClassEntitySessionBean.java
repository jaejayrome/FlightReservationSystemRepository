/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftType;
import entity.FlightCabinClass;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumerations.CabinClassType;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class FlightCabinClassEntitySessionBean implements FlightCabinClassEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static Validator validator = validatorFactory.getValidator();
    
    
    @Override
    public FlightCabinClass createFlightCabinClass(FlightCabinClass fcc) {
        Set<ConstraintViolation<FlightCabinClass>> errors = validator.validate(fcc);
        if (errors.size() == 0) {
        em.persist(fcc);
        em.flush();
        return fcc;
        } else return null;
    }
    
    @Override
    public List<FlightCabinClass> findFccForParticularFS(String flightNumber, Date uniqueDate) {
        return em.createQuery("SELECT fcc FROM FlightCabinClass fcc WHERE fcc.flightSchedule.departureTime = :date AND fcc.flightSchedule.flightSchedulePlan.flight.flightNumber = :flightNumber")
                .setParameter("date", uniqueDate)
                .setParameter("flightNumber", flightNumber)
                .getResultList();
    }
    
    @Override
    public FlightCabinClass getFCCByID(long id) {
        return em.find(FlightCabinClass.class, id);
    }
    
    @Override
    public List<FlightCabinClass> findFccWithFSAndFCCType(long fsID, String cabinClassType) {
        CabinClassType realType = null;
        switch (cabinClassType) {
                case "F": 
                    realType = CabinClassType.F;
                    break;
                case "J":
                    realType = CabinClassType.J;
                    break;
                case "w":
                    realType = CabinClassType.W;
                    break;
                case "Y": 
                    realType = CabinClassType.W;
                    break;
        }
        return em.createQuery("SELECT fcc FROM FlightCabinClass fcc WHERE fcc.flightSchedule.id = :date AND fcc.cabinClass.cabinClassName = :cabinClassType")
                .setParameter("date", fsID)
                .setParameter("cabinClassType", realType)
                .getResultList();
    }

 
}
