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
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;

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
    
    @WebMethod(operationName = "retrieveFlights")
    public Flight retrieveFlightDetails(
        @WebParam(name = "fspID") long fspID) {
       
        Flight f = partnerUseCaseSessionBeanLocal.retrieveFlight(fspID);
        
        // manual nullification
        f.setFlightSchedulePlanList(Collections.EMPTY_LIST);
        f.setAircraftConfiguration(null);
        f.setFlightRoute(null);
        return f;
    }
    
    @WebMethod(operationName = "retrieveFlightRoute")
    public List<String> retrieveFlightRouteDetails(
        @WebParam(name = "fspID") long fspId) {
        System.out.println(fspId);
        return partnerUseCaseSessionBeanLocal.retrieveFlightRoute(fspId);
    }
    
    @WebMethod(operationName = "partnerSearchConnectingFlightLeg1") 
    public List<FlightSchedule> partnerSearchConnectingFlight(
        @WebParam(name = "departureAirport") String departureAirport, 
        @WebParam(name = "departureDate") String depatureDate, 
        @WebParam(name = "destinationAirport") String destinationAirport,
        @WebParam(name = "isFirst") boolean isFirst) {
        List<FlightSchedule> flightScheduleList = partnerUseCaseSessionBeanLocal.partnerSearchForFlightRoutesConnecting(departureAirport, depatureDate, destinationAirport, isFirst);
        try {
                flightScheduleList.forEach(fs -> {
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

                return flightScheduleList;
            } catch (Exception ex) {
                // Handle exceptions or log them as needed
                ex.printStackTrace(); 
                return Collections.emptyList(); 
            }
    }
    
    @WebMethod(operationName = "partnerSearchFlight")
    public List<FlightSchedule> partnerSearchFlight(
            @WebParam(name = "departureAirport") String departureAirport, 
            @WebParam(name = "departureDate") String depatureDate, 
            @WebParam(name = "destinationAirport") String destinationAirport) {

            List<FlightSchedule> flightSchedules = partnerUseCaseSessionBeanLocal.partnerSearchForFlightRoutes(departureAirport, depatureDate, destinationAirport);
            // do the manual nullification here
            try {
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
    public Fare retrieveFaresForFlightSchedule(@WebParam(name = "flightSchedulIed") long id, @WebParam(name = "cabinClassName") String cabinClass) {
        List<Fare> fares = partnerUseCaseSessionBeanLocal.retreiveFares(id, cabinClass);
        if (!fares.isEmpty()) {
            fares.stream().forEach(x -> x.setCabinClass(null));
        fares.stream().forEach(x -> x.setFlightSchedulePlan(null));
        
        // get the highest fare
//        Comparator<Fare> highestFare = Comparator.comparingDouble(x -> x.getFareAmount().doubleValue());
//        return fares.stream().max(highestFare).get();
        return fares.get(0);
        } else {
            return null;
        }
        
    } 
    
        
}