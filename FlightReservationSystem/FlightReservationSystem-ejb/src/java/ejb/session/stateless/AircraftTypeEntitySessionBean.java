/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftType;
import entity.CabinClass;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumerations.AircraftTypeName;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class AircraftTypeEntitySessionBean implements AircraftTypeEntitySessionBeanLocal {
    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    Map<String, Object> props = new HashMap<String, Object>();
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static Validator validator = validatorFactory.getValidator();
    

    public AircraftTypeEntitySessionBean() {
        props.put("javax.persistence.cache.retrieveMode", "USE");
    }
    
    @Override
    public long createNewAircraftType(AircraftTypeName aircraftTypeName, BigDecimal passengerSeatCapacity) {
        AircraftType aircraftType = new AircraftType(aircraftTypeName, passengerSeatCapacity);
        Set<ConstraintViolation<AircraftType>> constraints = validator.validate(aircraftType);
        if (constraints.size() == 0) {
            em.persist(aircraftType);
            em.flush();
            return aircraftType.getId();
        } else {
            return -1;
        }
               
    }
    
    @Override
    public AircraftType getAircraftTypeFromName(AircraftTypeName aircraftTypeName) {
        String query = "SELECT a FROM AircraftType a WHERE a.aircraftTypeName = :name";
         AircraftType aircraftType =(AircraftType) em.createQuery(query)
          .setParameter("name", aircraftTypeName)
          .getSingleResult();
        return aircraftType;
    }
    
    @Override
    public List<AircraftType> getAllAircraftTypes() {
        String query = "SELECT a FROM AircraftType a";
        List<AircraftType> results =  em.createQuery(query).setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.USE).getResultList();
        return results;
    }
    
}
