/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.AircraftType;
import entity.CabinClass;
import entity.Seat;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import util.enumerations.AircraftTypeName;
import util.enumerations.CabinClassType;
import util.enumerations.JobTitle;
import util.enumerations.SeatStatus;
import util.exception.InvalidStringLengthException;
import util.exception.SeatLimitExceedException;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class FleetManagerUseCaseSessionBean implements FleetManagerUseCaseSessionBeanRemote, FleetManagerUseCaseSessionBeanLocal {

    @EJB
    private SeatEntitySessionBeanLocal seatEntitySessionBean;

    @EJB
    private CabinClassEntitySessionBeanLocal cabinClassEntitySessionBean;
    @EJB
    private AircraftConfigurationEntitySessionBeanLocal aircraftConfigurationEntitySessionBean;
    
    @EJB
    private AircraftTypeEntitySessionBeanLocal aircraftTypeEntitySessionBean;
    
    
    
    @Resource
    private EJBContext ejbContext;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public long createAircraftConfigurationForFleetManager(JobTitle jobTitle, AircraftTypeName aircraftTypeName, String configurationName, List<CabinClassType> cabinClassNameList, List<Integer> numAislesList, List<Integer> numRowsList, List<Integer> numSeatsAbreastList, List<String> seatingConfigurationList) 
            throws InvalidStringLengthException, SeatLimitExceedException {
        
            if (jobTitle == JobTitle.FLEET_MANAGER) {
                AircraftType aircraftType = aircraftTypeEntitySessionBean.getAircraftTypeFromName(aircraftTypeName);

                // Calculate the total number of seats
                int totalSeats = 0;
                List<CabinClass> cabinClassList = new ArrayList<CabinClass>();
                for (int i = 0; i < cabinClassNameList.size(); i++) {
                    CabinClassType cabinClassName = cabinClassNameList.get(i);
                    BigDecimal numAisles = new BigDecimal(numAislesList.get(i));
                    BigDecimal numRows = new BigDecimal(numRowsList.get(i));
                    BigDecimal numSeatsAbreast = new BigDecimal(numSeatsAbreastList.get(i));
                    String seatingConfiguration = seatingConfigurationList.get(i);
                    BigDecimal numAvailableSeats = new BigDecimal(numRows.intValue() * numSeatsAbreast.intValue());
                    CabinClass cabinClass = new CabinClass(cabinClassName, numAisles, numRows, numSeatsAbreast, seatingConfiguration);
                    cabinClassEntitySessionBean.createCabinClass(cabinClass);
                    cabinClassList.add(cabinClass);
                    totalSeats += (numRows.intValue() * numSeatsAbreast.intValue());
                }
                
                System.out.println(aircraftType.getPassengerSeatCapacity().intValue());
                System.out.println(totalSeats);

                // Verify if totalSeats exceeds the aircraft limit
                if (totalSeats > aircraftType.getPassengerSeatCapacity().intValue()) {
                    // Throw a custom exceptionF
                    throw new SeatLimitExceedException("Total seats exceed the aircraft limit.");
//                    ejbContext.setRollbackOnly();
                    
                } else {
                    // Create and persist the aircraft configuration
                    long configurationId = aircraftConfigurationEntitySessionBean.createNewAircraftConfiguration(aircraftType, configurationName, cabinClassList);
                    // If everything is successful, return the configurationId
                    return configurationId;
                }
            } 
            return 0L;
        
    }
    
    
    @Override
    public long createAircraftConfigurationForFleetManagerDatabaseInit(JobTitle jobTitle, AircraftTypeName aircraftTypeName, String configurationName, List<CabinClassType> cabinClassNameList, List<Integer> numAislesList, List<Integer> numRowsList, List<Integer> numSeatsAbreastList, List<String> seatingConfigurationList) {
        
        if (jobTitle == JobTitle.FLEET_MANAGER) {
            AircraftType aircraftType = aircraftTypeEntitySessionBean.getAircraftTypeFromName(aircraftTypeName);

            // Calculate the total number of seats
            int totalSeats = 0;
            List<CabinClass> cabinClassList = new ArrayList<CabinClass>();
            for (int i = 0; i < cabinClassNameList.size(); i++) {
                CabinClassType cabinClassName = cabinClassNameList.get(i);
                BigDecimal numAisles = new BigDecimal(numAislesList.get(i));
                BigDecimal numRows = new BigDecimal(numRowsList.get(i));
                BigDecimal numSeatsAbreast = new BigDecimal(numSeatsAbreastList.get(i));
                String seatingConfiguration = seatingConfigurationList.get(i);
                BigDecimal numAvailableSeats = new BigDecimal(numRows.intValue() * numSeatsAbreast.intValue());
                CabinClass cabinClass = new CabinClass(cabinClassName, numAisles, numRows, numSeatsAbreast, seatingConfiguration);
                cabinClassEntitySessionBean.createCabinClass(cabinClass);
                cabinClassList.add(cabinClass);
                totalSeats += (numRows.intValue() * numSeatsAbreast.intValue());
            }


            // Create and persist the aircraft configuration
        try {
            long configurationId = aircraftConfigurationEntitySessionBean.createNewAircraftConfiguration(aircraftType, configurationName, cabinClassList);
            // If everything is successful, return the configurationId
            return configurationId;
            } catch (InvalidStringLengthException e) {
                System.out.println(e.getMessage());
            }
            
        } 

        return 0L;
    }
    
    
    @Override
    public List<AircraftType> getAllAircraftTypes() {
        System.out.println(" size is " + aircraftTypeEntitySessionBean.getAllAircraftTypes());
        return aircraftTypeEntitySessionBean.getAllAircraftTypes();
    }
    
    @Override
    public List<AircraftConfiguration> viewAllAircraftConfiguration() {
        return aircraftConfigurationEntitySessionBean.getAllAircraftConfigurations();
    }
    
    @Override
    public AircraftConfiguration viewAircraftConfigurationDetails(String configurationName) {
        return aircraftConfigurationEntitySessionBean.getAircraftConfigurationPerConfigurationName(configurationName);
    }
    
    
}
