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
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    

    @Override
    public AircraftConfiguration getAircraftConfigurationById(long id) {
        AircraftConfiguration aircraftConfiguration = em.find(AircraftConfiguration.class, id);
        return aircraftConfiguration;
    }
    
    // use case for this should be as such
    // chooes the desired aircraft type to make a configuration with
    // enter the configuration name
    @Override
    public long createNewAircraftConfiguration(AircraftType aircraftType, String configurationName, List<CabinClass> cabinClassList) {
        BigDecimal numCabinClass = new BigDecimal(cabinClassList.size());
        AircraftConfiguration aircraftConfiguration = new AircraftConfiguration(configurationName, numCabinClass);
        // mandatory relationships
         // association: aircraftConfiguration -> aircraftType 
         
        aircraftConfiguration.setAircraftType(aircraftType);
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
