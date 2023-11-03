/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightCabinClass;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jeromegoh
 */
@Local
public interface FlightCabinClassEntitySessionBeanLocal {
    public FlightCabinClass createFlightCabinClass(FlightCabinClass fcc);
    public List<FlightCabinClass> findFccForParticularFS(String flightNumber, Date uniqueDate);
}
