/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftType;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;
import util.enumerations.AircraftTypeName;

/**
 *
 * @author jeromegoh
 */
@Local
public interface AircraftTypeEntitySessionBeanLocal {
    public long createNewAircraftType(AircraftTypeName aircraftTypeName, BigDecimal passengerSeatCapacity);
    public AircraftType getAircraftTypeFromName(AircraftTypeName aircraftTypeName);
    public List<AircraftType> getAllAircraftTypes();
}
