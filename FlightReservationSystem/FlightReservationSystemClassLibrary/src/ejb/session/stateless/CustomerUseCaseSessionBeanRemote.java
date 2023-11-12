/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightReservation;
import entity.FlightSchedule;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Remote;
import util.exception.NoFlightFoundException;

/**
 *
 * @author geraldtan
 */
@Remote
public interface CustomerUseCaseSessionBeanRemote {
    public int customerLogin(String email, String password);   
    public List<List<FlightSchedule>> searchForFlightRoutes(
    String departureAirport, Date departureDate, String destinationAirport, Date returnDate, int directFlight) throws NoFlightFoundException;
    public FlightReservation makeFlightReservation(long customerId, List<Long> flightScheduleIdList, List<Long> flightCabinClassList, List<List<String>> seatNumberList, List<HashMap<Integer, String>> passengerDetails, String creditCardNumber);

}
