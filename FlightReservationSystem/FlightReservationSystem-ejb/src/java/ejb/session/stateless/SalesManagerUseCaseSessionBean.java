/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightCabinClass;
import entity.FlightSchedule;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class SalesManagerUseCaseSessionBean implements SalesManagerUseCaseSessionBeanRemote, SalesManagerUseCaseSessionBeanLocal {

    @EJB
    private FlightCabinClassEntitySessionBeanLocal flightCabinClassEntitySessionBean;

    @EJB
    private FlightScheduleEntitySessionBeanLocal flightScheduleEntitySessionBean;

    @EJB
    private ScheduleManagerUseCaseSessionBeanLocal scheduleManagerUseCaseSessionBean;
    
   @Override
   public List<FlightCabinClass> viewSeatInventory(String flightNumber, Date uniqueDepartureDate) {
       // find the particular flight schedulde
       List<FlightCabinClass> fccList = flightCabinClassEntitySessionBean.findFccForParticularFS(flightNumber, uniqueDepartureDate);
       return fccList;
   }
   
   @Override
   public List<FlightSchedule> viewFlightSchedules(String flightNumber) {
       List<FlightSchedule> flightScheduleList =  flightScheduleEntitySessionBean.viewFlightSchedulesByFlightNumber(flightNumber);
       return flightScheduleList;
   }
}
