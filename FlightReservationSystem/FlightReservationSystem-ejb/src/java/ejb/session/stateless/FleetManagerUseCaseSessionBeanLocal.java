/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.AircraftType;
import java.util.List;
import javax.ejb.Local;
import util.enumerations.CabinClassType;
import util.enumerations.JobTitle;

/**
 *
 * @author jeromegoh
 */
@Local
public interface FleetManagerUseCaseSessionBeanLocal {
    public List<AircraftType> getAllAircraftTypes();
    public long createAircraftConfigurationForFleetManager(JobTitle jobTitle, String aircraftTypeString, String configurationName, List<CabinClassType> cabinClassNameList, List<Integer> numAislesList, List<Integer> numRowsList, List<Integer> numSeatsAbreastList, List<String> seatingConfigurationList);
    public List<AircraftConfiguration> viewAllAircraftConfiguration();
    public AircraftConfiguration viewAircraftConfigurationDetails(String configurationName);
}
