/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import util.enumerations.ScheduleType;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class ScheduleManagerUseCaseSessionBean implements ScheduleManagerUseCaseSessionBeanRemote, ScheduleManagerUseCaseSessionBeanLocal {

    @EJB
    private FlightSchedulePlanEntitySessionBeanLocal flightSchedulePlanEntitySessionBean;

    @EJB
    private FlightScheduleEntitySessionBeanLocal flightScheduleEntitySessionBean;

    @EJB
    private FlightEntitySessionBeanLocal flightEntitySessionBean;
    
    // plan for this 
    
    // we can tell if the size is more than 1 for departure date -> multiple
    // frequency = 7 -> recurrent week
    // frequency n days = not 7 
    // if endDate is not null also
    
    public abstract long createFlightSchedulePlan(ScheduleType type, String flightNumber, List<Date> departureDateList, Duration duration, Date endDate, int frequency);

    
}
