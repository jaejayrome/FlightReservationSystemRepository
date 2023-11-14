/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/EjbWebServiceNoOp.java to edit this template
 */
package ejb.session.ws;

import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import ejb.session.stateless.PartnerUseCaseSessionBeanLocal;
import entity.Fare;
import entity.Flight;
import entity.FlightBooking;
import entity.FlightCabinClass;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import util.exception.InvalidLoginCredentialsException;
import util.exception.NoFlightFoundException;
import util.util.Pair;

/**
 *
 * @author jeromegoh
 */
@WebService(serviceName = "FlightReservationSystemWebService")
@Stateless
public class FlightReservationSystemWebService {

    @EJB
    private PartnerUseCaseSessionBeanLocal partnerUseCaseSessionBeanLocal;
 
    @WebMethod(operationName = "partnerLogin")
    public long partnerLogin(@WebParam(name = "username") String username, @WebParam(name = "password") String password) {
        return partnerUseCaseSessionBeanLocal.partnerLogin(username, password);
    }
    
    @WebMethod(operationName = "partnerLogout") 
    public void partnerLogout(@WebParam(name = "partnerId") long id) {
        partnerUseCaseSessionBeanLocal.partnerLogout(id);
    }
    @WebMethod(operationName = "partnerSearchFlight")
    public List<FlightSchedule> partnerSearchFlight(
            @WebParam(name = "departureAirport") String departureAirport, 
            @WebParam(name = "departureDate") String depatureDate, 
            @WebParam(name = "destinationAirport") String destinationAirport, 
            @WebParam(name = "returnDate") String returnDate, 
            @WebParam(name = "directFlight") int directFlight) {

        try {
            List<FlightSchedule> flightSchedules = partnerUseCaseSessionBeanLocal.partnerSearchForFlightRoutes(departureAirport, depatureDate, destinationAirport, returnDate, directFlight);

            flightSchedules.forEach(fs -> {
                if (!fs.getFccList().isEmpty()) {
                    fs.getFccList().forEach(fcc -> {
                        if (fcc != null) {
                            fcc.setFlightSchedule(null);
                            fcc.getSeatList().size();
                            fcc.getSeatList().forEach(y -> y.setFlightCabinClass(null));
                            fcc.setCabinClass(null);
                        }
                    });
                }
                
                fs.setFlightBookingList(Collections.emptyList());

                if (!fs.getFlightBookingList().isEmpty()) {
                    fs.getFlightBookingList().forEach(fb -> {
                        if (fb != null) {
                            fb.setFlightSchedule(null);
                        }
                    });
                }
                
                fs.setFccList(Collections.emptyList());

                FlightSchedulePlan fsp = fs.getFlightSchedulePlan();
                if (fsp != null) {
                    Flight flight = fsp.getFlight();

                    if (flight != null) {
                        // Nullify fields to break circular references
                        if (flight.getAircraftConfiguration() != null) {
                            // Break the cycle by setting AircraftType to null
                            if (flight.getAircraftConfiguration().getAircraftType() != null) {
                                flight.getAircraftConfiguration().getAircraftType().setAircraftConfigurations(Collections.emptyList());
                            }
                            flight.getAircraftConfiguration().setAircraftType(null);
                        }
                        flight.setAircraftConfiguration(null);
                    }
                    fsp.setFlightScheduleList(Collections.emptyList());
                }
                
                fs.setFlightSchedulePlan(null);
            });

            return flightSchedules;
        } catch (Exception ex) {
            // Handle exceptions or log them as needed
            ex.printStackTrace(); 
            return Collections.emptyList(); 
        }
    }
    
    
    @WebMethod(operationName = "retrieveFaresForFlightSchedule")
    public List<Fare>  retrieveFaresForFlightSchedule(@WebParam(name = "flightScheduleId") long id, @WebParam(name = "cabinClassName") String cabinClass) {
        List<Fare> fares = partnerUseCaseSessionBeanLocal.retreiveFares(id, cabinClass);
        fares.stream().forEach(x -> x.setCabinClass(null));
        fares.stream().forEach(x -> x.setFlightSchedulePlan(null));
        return fares;
    } 
    
//    @WebMethod(operationName = "partnerSearchFlight")
//    public List<Pair<FlightSchedule>> partnerSearchConnectingFlight(
//            @WebParam(name = "departureAirport") String departureAirport, 
//            @WebParam(name = "departureDate") String depatureDate, 
//            @WebParam(name = "destinationAirport") String destinationAirport, 
//            @WebParam(name = "returnDate") String returnDate, 
//            @WebParam(name = "directFlight") int directFlight) {
//        
//        // do manual nullifcation
////        try {
//        List<Pair<FlightSchedule>> flightSchedules = new ArrayList<Pair<FlightSchedule>>();
////            // detaching of entity
////            List<Pair<FlightSchedule, FlightSchedule>> flightSchedules = partnerUseCaseSessionBeanLocal.partnerSearchForFlightRoutesConnecting(departureAirport, depatureDate, destinationAirport, returnDate);
////            List<FlightCabinClass> fccList = new ArrayList<FlightCabinClass>();
////            List<FlightBooking> flightBookingList = new ArrayList<FlightBooking>();
////            List<FlightSchedule> flightScheduleList = new ArrayList<FlightSchedule>();
////            
////            // manual nullifcaiton
////           
////            flightSchedules.stream().forEach(y -> {
////                y.getKey().getFccList().size();
////                y.getKey().getFccList().stream().forEach(z -> z.setFlightSchedule(null));
////                y.getKey().getFlightBookingList().size();
////                y.getKey().getFlightBookingList().stream().forEach(z -> z.setFlightSchedule(null));
////                y.getKey().getFlightSchedulePlan().setFlightScheduleList(flightScheduleList);
////            });
//            
//        return flightSchedules;
//    }
}

    
