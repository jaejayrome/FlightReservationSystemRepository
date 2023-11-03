/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightCabinClass;
import entity.FlightSchedule;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author jeromegoh
 */
@Remote
public interface SalesManagerUseCaseSessionBeanRemote {
    public List<FlightSchedule> viewFlightSchedules(String flightNumber);
    public List<FlightCabinClass> viewSeatInventory(String flightNumber, Date uniqueDepartureDate);
}
