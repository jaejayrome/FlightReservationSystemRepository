/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightSchedulePlan;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jeromegoh
 */
@Local
public interface FlightSchedulePlanEntitySessionBeanLocal {
    public FlightSchedulePlan createFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan);
    public List<FlightSchedulePlan> viewAllFlightSchedulePlan();
    public FlightSchedulePlan getFSPfromID(long id);
    public void disableFlightSchedulePlan(FlightSchedulePlan fsp);
    public void deleteFlightSchedulePlan(FlightSchedulePlan fsp);
}
