/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.AircraftType;
import entity.CabinClass;
import java.math.BigDecimal;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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
public class AircraftConfigurationEntitySessionBean implements AircraftConfigurationEntitySessionBeanLocal {

    @EJB
    private AircraftTypeEntitySessionBeanLocal aircraftTypeEntitySessionBean;

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static Validator validator = validatorFactory.getValidator();
    
    @Resource
    private EJBContext ejbContext;
    
    @Override
    public AircraftConfiguration getAircraftConfigurationById(long id) {
        AircraftConfiguration aircraftConfiguration = em.find(AircraftConfiguration.class, id);
        return aircraftConfiguration;
    }
    
    // use case for this should be as such
    // chooes the desired aircraft type to make a configuration with
    // enter the configuration name
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public long createNewAircraftConfiguration(AircraftType aircraftType, String configurationName, List<CabinClass> cabinClassList) {
        BigDecimal numCabinClass = new BigDecimal(cabinClassList.size());
        AircraftConfiguration aircraftConfiguration = new AircraftConfiguration(configurationName, numCabinClass);
        // mandatory relationships
         // association: aircraftConfiguration -> aircraftType 
         
        aircraftConfiguration.setAircraftType(aircraftType);
        Set<ConstraintViolation<AircraftConfiguration>> errors = validator.validate(aircraftConfiguration);
        if (errors.size() > 0) {
            for (ConstraintViolation<AircraftConfiguration> error : errors) {
                System.out.println(error.getPropertyPath() + " " + error.getMessage());
            }
            ejbContext.setRollbackOnly();
        }
        em.persist(aircraftConfiguration);
        em.flush();
        
        // associating aircraftType with aircraftConfiguration
        int init = aircraftType.getAircraftConfigurations().size();
        aircraftType.getAircraftConfigurations().add(aircraftConfiguration);
        
        // associating aircraftConfiguraiton with cabin class
        int initialise = aircraftConfiguration.getCabinClassList().size();
        List<CabinClass> existingList = aircraftConfiguration.getCabinClassList();
        existingList.addAll(cabinClassList);
        
        // associate cabin class to aircraft configuration
        cabinClassList.stream().forEach(cabinClass -> {
            int initAgain = cabinClass.getAircraftConfigurationList().size();
            cabinClass.getAircraftConfigurationList().add(aircraftConfiguration);
        });

        return aircraftConfiguration.getId();
    }
      
    @Override
    public List<AircraftConfiguration> getAllAircraftConfigurationPerAircraftType(AircraftTypeName aircraftTypeName) {
        AircraftType aircraftType = aircraftTypeEntitySessionBean.getAircraftTypeFromName(aircraftTypeName);
        if (aircraftType != null) {
            return aircraftType.getAircraftConfigurations();
        }
        return new ArrayList<AircraftConfiguration>();
    }
    
    @Override
    public List<AircraftConfiguration> getAllAircraftConfigurations() {
        return em.createQuery("SELECT a FROM AircraftConfiguration a").getResultList();
    }
    
    @Override
    public AircraftConfiguration getAircraftConfigurationPerConfigurationName(String configurationName) {
        return (AircraftConfiguration) em.createQuery("SELECT a FROM AircraftConfiguration a WHERE a.configurationName = :name").setParameter("name", configurationName).getSingleResult();
    }
    
    
    
}
