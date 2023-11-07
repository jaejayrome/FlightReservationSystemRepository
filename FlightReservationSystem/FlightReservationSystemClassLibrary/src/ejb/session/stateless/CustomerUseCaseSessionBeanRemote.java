/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightRoute;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author geraldtan
 */
@Remote
public interface CustomerUseCaseSessionBeanRemote {

    int customerLogin(String email, String password);   
    
    
    //need to add return Flights
    List<FlightRoute> searchForFlightRoutes(
        String departureAirport, 
        Date departureDate, 
        String arrivalAirport,
        Date returnDate, 
        boolean dfbl
    );
    
    
    //no need add return flights
    List<FlightRoute> searchForFlightRoutes(
        String departureAirport, 
        Date departureDate, 
        String arrivalAirport,
        boolean dfbl
    );
    
    
}
