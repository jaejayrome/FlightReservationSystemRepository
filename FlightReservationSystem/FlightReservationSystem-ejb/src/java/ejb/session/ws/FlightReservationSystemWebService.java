/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/EjbWebServiceNoOp.java to edit this template
 */
package ejb.session.ws;

import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import ejb.session.stateless.PartnerUseCaseSessionBeanLocal;
import entity.CabinClass;
import entity.Fare;
import entity.Flight;
import entity.FlightBooking;
import entity.FlightCabinClass;
import entity.FlightReservation;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.Passenger;
import entity.Seat;
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
import util.enumerations.CabinClassType;

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
    
    @WebMethod(operationName = "retrieveCabinClass")
    public CabinClass retrieveCabinClass(
        @WebParam(name = "fspID") long fspID,
        @WebParam(name = "cabinClassName") String name 
    ) {
        CabinClassType chosenOne = null;
         switch (name) {
            case "F": 
                chosenOne = CabinClassType.F;
                break;
            case "Y": 
                chosenOne = CabinClassType.Y;
                break;
            case "W": 
                chosenOne = CabinClassType.W;
                break;   
             case "J": 
                chosenOne = CabinClassType.J;
                break;
        }
        CabinClass cc = partnerUseCaseSessionBeanLocal.retrieveCabinClass(fspID, chosenOne);
        cc.setAircraftConfigurationList(Collections.EMPTY_LIST);
        return cc;
    }
    
    @WebMethod(operationName = "retrieveSeats")
    public List<Seat> retrieveSeats(
        @WebParam(name = "fspID") long fspID,
        @WebParam(name = "cabinClassName") String name 
    ) {
        CabinClassType chosenOne = null;
         switch (name) {
            case "F": 
                chosenOne = CabinClassType.F;
                break;
            case "Y": 
                chosenOne = CabinClassType.Y;
                break;
            case "W": 
                chosenOne = CabinClassType.W;
                break;   
             case "J": 
                chosenOne = CabinClassType.J;
                break;
        }
        List<Seat> seatList = partnerUseCaseSessionBeanLocal.retrieveSeats(fspID, chosenOne);
        seatList.stream().forEach(x -> {
            x.setFlightCabinClass(null);
        });
        return seatList;
    }
    // passengers, flight bookings, flight reservation
    @WebMethod(operationName = "makeFlightBooking")
    public FlightBooking makeFlightBooking(
        @WebParam(name = "flightScheduleId") long flightScheduleId,
        @WebParam(name = "cabinClassName") String cabinClassName,
        @WebParam(name = "seatNumber") List<String> seatNumber,
        @WebParam(name = "ticketPricesForEachFlightSchedule") double ticketPricesForEachFlightSchedule,
        @WebParam(name = "passengerList") List<Passenger> passengerList) {
        FlightBooking fb = partnerUseCaseSessionBeanLocal.makeFlightBooking(flightScheduleId, cabinClassName, seatNumber, ticketPricesForEachFlightSchedule, passengerList);
        fb.setFlightSchedule(null);
        fb.setReservedSeats(Collections.EMPTY_LIST);
        return fb;
    }
    
    @WebMethod(operationName = "persistPassengers")
    public Passenger persistPassengers(
       @WebParam(name = "passengerDetails") List<String> passenger) {
       return partnerUseCaseSessionBeanLocal.persistPassengers(passenger);
    }
            
    @WebMethod(operationName = "makeFlightReservation")
    public void makeFlightReservation(
        @WebParam(name = "partnerId") long partnerId,
        @WebParam(name = "flightScheduleId") Long flightScheduleId,
        @WebParam(name = "passengerDetails") List<Passenger> passengerDetails,
        @WebParam(name = "creditCardNumber") String creditCardNumber,
        @WebParam(name = "bookings") List<FlightBooking> bookings) {

        partnerUseCaseSessionBeanLocal.makeFlightReservation(partnerId, flightScheduleId, passengerDetails, creditCardNumber, bookings);
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
    
    @WebMethod(operationName = "viewFlightResevations")
    public List<FlightReservation> getFlightReservations(
            @WebParam(name = "partnerID") long partnerID) {
        List<FlightReservation> list = partnerUseCaseSessionBeanLocal.getFlightReservations(partnerID);
        list.stream().forEach(x -> x.setFlightBookingList(Collections.EMPTY_LIST));
        list.stream().forEach(x -> x.setCustomer(null));
        list.stream().forEach(x -> x.setPassengerList(Collections.EMPTY_LIST));
        list.stream().forEach(x -> x.setPartner(null));
        return list;
    }
    
    @WebMethod(operationName = "getFlightBookingsForReservation")
    public List<FlightBooking> getFlightBookingsForReservation(
            @WebParam(name = "flightReservationId") long flightReservationId) {
            List<FlightBooking> list = partnerUseCaseSessionBeanLocal.getFlightBookingsForReservation(flightReservationId);
            list.stream().forEach(x -> x.setFlightReservation(null));
            list.stream().forEach(x -> x.setFlightSchedule(null));
            list.stream().forEach(x -> x.setReservedSeats(Collections.EMPTY_LIST));
            return list;
    }
    
    
    @WebMethod(operationName = "retrieveFaresForFlightSchedule")
    public Fare retrieveFaresForFlightSchedule(@WebParam(name = "flightSchedulIed") long id, @WebParam(name = "cabinClassName") String cabinClass) {
        List<Fare> fares = partnerUseCaseSessionBeanLocal.retreiveFares(id, cabinClass);
        if (!fares.isEmpty()) {
            fares.stream().forEach(x -> x.setCabinClass(null));
        fares.stream().forEach(x -> x.setFlightSchedulePlan(null));
        return fares.get(0);
        } else {
            return null;
        }
        
    } 
    
        
}