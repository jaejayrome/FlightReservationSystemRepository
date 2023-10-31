/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.AircraftType;
import entity.CabinClass;
import java.util.List;
import javax.ejb.Local;
import util.enumerations.AircraftTypeName;

/**
 *
 * @author jeromegoh
 */
@Local
public interface AircraftConfigurationEntitySessionBeanLocal {
    public long createNewAircraftConfiguration(AircraftType aircraftType, String configurationName, List<CabinClass> cabinClassList);
    public AircraftConfiguration getAircraftConfigurationById(long id);
    public List<AircraftConfiguration> getAllAircraftConfigurationPerAircraftType(AircraftTypeName aircraftTypeName);
    public List<AircraftConfiguration> getAllAircraftConfigurations();
    public AircraftConfiguration getAircraftConfigurationPerConfigurationName(String configurationName);
}
