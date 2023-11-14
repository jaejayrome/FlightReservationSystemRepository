/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Fare;
import entity.FlightSchedule;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Local;
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
        String departureAirport, String departureDateS, String destinationAirport, String returnDateS, int directFlight);
    public List<javafx.util.Pair<FlightSchedule, FlightSchedule>> partnerSearchForFlightRoutesConnecting(String departureAirport, String departureDateS, String destinationAirport, String returnDateS);
    public List<Fare> retreiveFares(long id, String cabinClass);
            
}
