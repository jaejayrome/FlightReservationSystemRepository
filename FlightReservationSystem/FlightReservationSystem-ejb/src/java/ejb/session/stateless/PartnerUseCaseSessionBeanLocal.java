/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.CabinClass;
import entity.Fare;
import entity.Flight;
import entity.FlightBooking;
import entity.FlightReservation;
import entity.FlightSchedule;
import entity.Passenger;
import entity.Seat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Local;
import util.enumerations.CabinClassType;
import util.exception.NoFlightFoundException;
import util.util.Pair;

/**
 *
 * @author jeromegoh
 */
@Local
public interface PartnerUseCaseSessionBeanLocal {
    public long partnerLogin(String username, String password);
    public void partnerLogout(long id);
    public List<FlightSchedule> partnerSearchForFlightRoutes(
        String departureAirport, String departureDateS, String destinationAirport);
   public List<FlightSchedule> partnerSearchForFlightRoutesConnecting(String departureAirport, String departureDateS, String destinationAirport, boolean isFirst);
    public List<Fare> retreiveFares(long id, String cabinClass);
    public Flight retrieveFlight(long fspID);
    public List<String> retrieveFlightRoute(long flightID);
    public CabinClass retrieveCabinClass(long fspID, CabinClassType name);
    public List<Seat> retrieveSeats(long fspID, CabinClassType name);
    public FlightBooking makeFlightBooking(long flightSchdeduleId, String flightCabinClassName, List<String> seatNumber, FlightReservation flightReservation, double ticketPricesForEachFlightSchedule, List<Passenger> passengerList);
    
            
}
