/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftType;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jeromegoh
 */
@Local
public interface AircraftTypeEntitySessionBeanLocal {
    public long createNewAircraftType(String aircraftTypeName, String manufacturer, BigDecimal passengerSeatCapacity);
    public AircraftType getAircraftTypeFromName(String name);
    public List<AircraftType> getAllAircraftTypes();
}
