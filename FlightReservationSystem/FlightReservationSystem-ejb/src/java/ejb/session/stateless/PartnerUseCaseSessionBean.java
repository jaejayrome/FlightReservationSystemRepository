/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.CabinClass;
import entity.Customer;
import entity.Fare;
import entity.Flight;
import entity.FlightBooking;
import entity.FlightCabinClass;
import entity.FlightReservation;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.Partner;
import entity.Passenger;
import entity.Seat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.InvalidLoginCredentialsException;
import util.exception.NoFlightFoundException;
import javafx.util.Pair;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import util.enumerations.CabinClassType;
import util.enumerations.FlightSchedulePlanStatus;
import util.enumerations.SeatStatus;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class PartnerUseCaseSessionBean implements PartnerUseCaseSessionBeanLocal {

    @EJB
    private SeatEntitySessionBeanLocal seatEntitySessionBean;

    @EJB
    private FlightEntitySessionBeanLocal flightEntitySessionBean;
    
    
    
    @Resource
    private EJBContext ejbContext;
    
    @EJB
    private FlightCabinClassEntitySessionBeanLocal flightCabinClassEntitySessionBean;
    
    @EJB
    private FlightBookingEntitySessionBeanLocal flightBookingEntitySessionBean;

    @EJB
    private FlightScheduleEntitySessionBeanLocal flightScheduleEntitySessionBean;

    @EJB
    private FlightReservationEntitySessionBeanLocal flightReservationEntitySessionBean;

    @EJB
    private PassengerEntitySessionBeanLocal passengerEntitySessionBean;
    
    
    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    @EJB
    private PartnerEntitySessionBeanLocal partnerEntitySessionBean;
    
    @Override
    public long partnerLogin(String username, String password) {
        try {
                Partner partner = partnerEntitySessionBean.authenticatePartner(username, password);
                System.out.println("Login Successful");
                long id = partner.getId();
                partnerEntitySessionBean.updateLogInStatus(true, id);
                return id;

            } catch (InvalidLoginCredentialsException exception) {
                System.out.println(exception.getMessage());
                return -1;
        }
    }
    
    @Override
    public void partnerLogout(long id) {
        partnerEntitySessionBean.updateLogInStatus(false, id);
        System.out.println("Logout Successful");
    }
    
    // this is not fsp id this is FS id
    @Override
    public Flight retrieveFlight(long fspID) {
        FlightSchedule fs = flightScheduleEntitySessionBean.getFlightScheduleById(fspID);
        Flight f = fs.getFlightSchedulePlan().getFlight();
        
        // manual detach
        em.detach(f.getAircraftConfiguration());
        f.getFlightSchedulePlanList().stream().forEach(x -> em.detach(x));
        em.detach(f.getFlightRoute());
        em.detach(fs);
        em.detach(fs.getFlightSchedulePlan());
        em.detach(fs.getFlightSchedulePlan().getFlight());
        return f;
    }
    
    
    @Override
    public FlightReservation makeFlightReservation(long partnerId, long flightScheduleId, List<Passenger> passengerDetails, String creditCardNumber, List<FlightBooking> bookings) {
        int numPassengers = passengerDetails.size();
        
        // check whether flight schedule plan disabled
        FlightSchedule test = flightScheduleEntitySessionBean.getFlightScheduleById(flightScheduleId);
        if (test.getFlightSchedulePlan().getStatus() == FlightSchedulePlanStatus.DISABLED) {
            ejbContext.setRollbackOnly();
        }
        
        // get Partner 
        Partner partner = partnerEntitySessionBean.findPartner(partnerId);

        // association between flight reservation and customer,
        FlightReservation flightReservation = new FlightReservation(partner, creditCardNumber);
        
         //  persist the flight reservation
        flightReservation = flightReservationEntitySessionBean.makeFlightReservation(flightReservation);

        
        
        for (Passenger p : passengerDetails) {
            Passenger g = passengerEntitySessionBean.findPassenger(p.getId());
            // association between flight reservation and passenger
            flightReservation.getPassengerList().add(g);
        }
        
        // association between partner to flight reservation
        partner.getReservations().size();
        partner.getReservations().add(flightReservation);
        
        // association between flight reservation and flightBooking 
        int init = flightReservation.getFlightBookingList().size();

        // assuming flight booking has been persisted
        // association flight booking -> flight reservation
        for (FlightBooking b : bookings) {
            // this is detached so need to find the exact instance 
            FlightBooking c = flightBookingEntitySessionBean.findBooking(b.getId());
            c.setFlightReservation(flightReservation);
            flightReservation.getFlightBookingList().add(c);            
        }
        
        return flightReservation;
    }
    
    @Override
    public FlightBooking makeFlightBooking(long flightSchdeduleId, String flightCabinClassName, List<String> seatNumber, double ticketPricesForEachFlightSchedule, List<Passenger> passengerList) {
            FlightSchedule flightSchedule = flightScheduleEntitySessionBean.getFlightScheduleById(flightSchdeduleId);
            if (flightSchedule.getFlightSchedulePlan().getStatus() == FlightSchedulePlanStatus.DISABLED) {
                // throw null if we cannot make a flight booking under this disabled FSP
                return null;
            }
            
            String flightNumber = flightSchedule.getFlightSchedulePlan().getFlight().getFlightNumber();
            FlightCabinClass flightCabinClass = flightCabinClassEntitySessionBean.findFccWithFSAndFCCType(flightSchdeduleId, flightCabinClassName).get(0);
            
            BigDecimal prevReserved = flightCabinClass.getNumReservedSeats();
            BigDecimal newReserved = prevReserved.add(new BigDecimal(seatNumber.size()));
            BigDecimal newBalanced = flightCabinClass.getNumAvailableSeats().subtract(newReserved);
            // updated the numbers 
            flightCabinClass.setNumReservedSeats(newReserved);
            flightCabinClass.setNumBalanceSeats(newBalanced);
            
            List<Seat> toAdd = new ArrayList<Seat>();
            
            // navigate to the correct flight cabin class
            int counter = 0;
            flightCabinClass.getSeatList().size();
            for (String str : seatNumber) {
                for (Seat s : flightCabinClass.getSeatList()) {
                    if (s.getSeatNumber().equals(str)) {
                        s.setSeatStatus(SeatStatus.RESERVED);
                        s.setPassenger(passengerList.get(counter));
                        toAdd.add(s);
                    }
                }
                counter += 1;
            }
            
            FlightBooking flightBooking = new FlightBooking(flightNumber, flightSchedule, new BigDecimal(ticketPricesForEachFlightSchedule));
           
            // persist flight booking
            flightBooking = flightBookingEntitySessionBean.makeBooking(flightBooking);
            // association flight schedule to flight booking 
            int initLazy = flightSchedule.getFlightBookingList().size();
            flightSchedule.getFlightBookingList().add(flightBooking);
            
            int init = flightBooking.getReservedSeats().size();
            // association done flight booking to seats
            flightBooking.getReservedSeats().addAll(toAdd);
            
            
            // before returning back to the client 
            em.detach(flightBooking);
            return flightBooking;
    }
    
    @Override
    public Passenger persistPassengers(List<String> passenger) {
        Passenger newPassenger = new Passenger(passenger.get(0), passenger.get(1), passenger.get(2));
        newPassenger = passengerEntitySessionBean.persistPassenger(newPassenger);
        // em.detach(newPassenger);
        return newPassenger;
    }
    
    @Override    
    public CabinClass retrieveCabinClass(long fspID, CabinClassType name) {
        System.out.println(fspID);
        System.out.println(name);
        FlightSchedule fs = flightScheduleEntitySessionBean.getFlightScheduleById(fspID);
        System.out.println(fs);

        fs.getFccList().size();
        CabinClass toReturn = null;

        List<FlightCabinClass> fccList = fs.getFccList();
        for (int i = 0; i < fccList.size(); i++) {
            FlightCabinClass x = fccList.get(i);
            CabinClass cc = x.getCabinClass();
            if (cc.getCabinClassName().equals(name)) {
                em.detach(cc);
                cc.getAircraftConfigurationList().size();
                for (AircraftConfiguration y : cc.getAircraftConfigurationList()) {
                    em.detach(y);
                }
                toReturn = cc;
                break;
            }
        }

    return toReturn;
    }
    
    @Override
    public List<Seat> retrieveSeats(long fspID, CabinClassType name) {
        FlightSchedule fs = flightScheduleEntitySessionBean.getFlightScheduleById(fspID);
        fs.getFccList().size();
        List<Seat> toReturn = new ArrayList<Seat>();
        fs.getFccList().stream().forEach(x -> {
            if (x.getCabinClass().getCabinClassName().equals(name)) {
                x.getSeatList().size();
                x.getSeatList().stream().forEach(y -> {
                    em.detach(y);
                    toReturn.add(y);
                });
            }
        });
        
        return toReturn;
    }
    
    @Override
    public List<String> retrieveFlightRoute(long fspID) {
        FlightSchedule fs = flightScheduleEntitySessionBean.getFlightScheduleById(fspID);
        Flight f = fs.getFlightSchedulePlan().getFlight();
        FlightRoute fr = f.getFlightRoute();
        List<String> airports = new ArrayList<String>();
        airports.add(fr.getOrigin().getIataAirportCode());
        airports.add(fr.getDestination().getIataAirportCode());
        return airports;
    }
    
    @Override 
    public List<FlightSchedule> partnerSearchForFlightRoutesConnecting(String departureAirport, String departureDateS, String destinationAirport, boolean isFirst) {
        Date departureDate = formatDate(departureDateS);
        
        ArrayList<FlightSchedule> toReturn = new ArrayList<>();

        List<Pair<FlightSchedule, FlightSchedule>> listOfAllCombinations = getConnectingFlightsOneWay(departureAirport, destinationAirport);
        Calendar cal3 = Calendar.getInstance();
        cal3.setTime(departureDate);
        cal3.add(Calendar.DAY_OF_YEAR, -3);
        Date startDate3 = cal3.getTime();
        cal3.add(Calendar.DAY_OF_YEAR, 6);
        Date endDate3 = cal3.getTime();

        listOfAllCombinations = listOfAllCombinations.stream().filter(x -> x.getKey().getDepartureTime().after(startDate3) && x.getKey().getDepartureTime().before(endDate3)).collect(Collectors.toList());
        for (Pair<FlightSchedule, FlightSchedule> connectingFlightPair : listOfAllCombinations) {
            if (isFirst) {
                toReturn.add(connectingFlightPair.getKey());
            } else {
                toReturn.add(connectingFlightPair.getValue());
            }
        }
        toReturn.stream().forEach(y -> {
            em.detach(y.getFlightSchedulePlan().getFlight().getAircraftConfiguration().getAircraftType());
            em.detach(y.getFlightSchedulePlan().getFlight().getAircraftConfiguration());
            em.detach(y.getFlightSchedulePlan().getFlight());
            y.getFlightBookingList().stream().forEach(z -> z.getReservedSeats().stream().forEach(b -> em.detach(b)));
            y.getFlightBookingList().stream().forEach(z -> em.detach(z));
            y.getFccList().stream().forEach(z -> {
                z.getCabinClass().getAircraftConfigurationList().stream().forEach(a -> em.detach(a.getAircraftType()));
                z.getCabinClass().getAircraftConfigurationList().stream().forEach(a -> em.detach(a));
                em.detach(z.getCabinClass());
                em.detach(z);
            });
            em.detach(y.getFlightSchedulePlan());
            em.detach(y);
        });
        return toReturn;
    }
    
    
    
    @Override 
    public List<FlightSchedule> partnerSearchForFlightRoutes(
        String departureAirport, String departureDateS, String destinationAirport) {
        
        Date departureDate = formatDate(departureDateS);

        Calendar cal = Calendar.getInstance();
        cal.setTime(departureDate);
        cal.add(Calendar.DAY_OF_YEAR, -3); // Subtract 3 days for the start date
        Date startDate = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, 6); // Add 6 days to the start date to get the end date (3 days after)
        Date endDate = cal.getTime();

        System.out.println("a" + departureAirport);
        System.out.println("b" + departureDateS);
        System.out.println("c" + destinationAirport);
        
        
        // Use the calculated dates in your JPQL query
        List<FlightSchedule> flightScheduleList1 = em.createQuery(
            "SELECT fs FROM FlightSchedule fs WHERE fs.departureTime >= :startDate AND fs.departureTime <= :endDate AND fs.flightSchedulePlan.flight.flightRoute.origin.iataAirportCode = :departureAirport AND fs.flightSchedulePlan.flight.flightRoute.destination.iataAirportCode = :destinationAirport")
            .setParameter("startDate", startDate)
            .setParameter("endDate", endDate)
            .setParameter("departureAirport", departureAirport)
            .setParameter("destinationAirport", destinationAirport)
            .getResultList();
        System.out.println("WEB SERVICE SIZE " + flightScheduleList1.size());
        flightScheduleList1.stream().forEach(x -> {
            int init = x.getFccList().size();
            x.getFccList().stream().forEach(y -> y.getSeatList().size());
            x.getFlightSchedulePlan().getFares().size();
        });
        
        flightScheduleList1.stream().forEach(y -> {
            em.detach(y.getFlightSchedulePlan().getFlight().getAircraftConfiguration().getAircraftType());
            em.detach(y.getFlightSchedulePlan().getFlight().getAircraftConfiguration());
            em.detach(y.getFlightSchedulePlan().getFlight());
            y.getFlightBookingList().stream().forEach(z -> z.getReservedSeats().stream().forEach(b -> em.detach(b)));
            y.getFlightBookingList().stream().forEach(z -> em.detach(z));
            y.getFccList().stream().forEach(z -> {
                z.getCabinClass().getAircraftConfigurationList().stream().forEach(a -> em.detach(a.getAircraftType()));
                z.getCabinClass().getAircraftConfigurationList().stream().forEach(a -> em.detach(a));
                em.detach(z.getCabinClass());
                em.detach(z);
            });
            em.detach(y.getFlightSchedulePlan());
            em.detach(y);
        });
        
        System.out.println("WEB SERVICE SIZE " + flightScheduleList1.size());
        
        return flightScheduleList1;
    }
    
   public List<Pair<FlightSchedule, FlightSchedule>> getConnectingFlightsOneWay(String originIATACode, String destinationIATACode) {
          // NEED CHECK AGAIN
        List<FlightSchedule> originList = flightScheduleEntitySessionBean.getFlightSchedulesByAirportOrigin(originIATACode);
        List<FlightSchedule> destinationList = flightScheduleEntitySessionBean.getFlightSchedulesByAirportDestination(destinationIATACode);
        List<Pair<FlightSchedule, FlightSchedule>> connectingFlightList = new ArrayList<Pair<FlightSchedule, FlightSchedule>>();
        originList.stream().forEach(x -> {
            Date arrivalDate = x.getArrivalTime();
            destinationList.stream().forEach(y -> {
                Date departureDate = y.getDepartureTime();
                // check 3 conditions
                // checks whether the departuredate is always after the arrivaldate
                if (!arrivalDate.after(departureDate)) {
                    // compute the difference between the duraiton and ensure that it is under one day of level over
                    long milisecondsDifference = departureDate.getTime() - arrivalDate.getTime();
                    // checks whether the destination of the first flight route is the same as the orign of the second flight route
                    boolean startEndSame = x.getFlightSchedulePlan().getFlight().getFlightRoute().getDestination().getIataAirportCode().equals(y.getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin().getIataAirportCode());
                    if (milisecondsDifference <= (24 * 60 * 60 * 1000) && startEndSame){
                        // means layover is lesser than 1 day 
                        Pair<FlightSchedule, FlightSchedule> connectingFlightSchedule = new Pair<FlightSchedule, FlightSchedule>(x, y);
                        connectingFlightList.add(connectingFlightSchedule);
                    }
                }
            });
        });
        return connectingFlightList;
    }
   
   @Override
   public List<Fare> retreiveFares(long id, String cabinClass) {
       FlightSchedule fs = flightScheduleEntitySessionBean.getFlightScheduleById(id);
        fs.getFccList().size();
        fs.getFlightSchedulePlan().getFares().size();
        List<Fare> fares =  fs.getFlightSchedulePlan().getFares().stream().filter(x -> x.getCabinClass().getCabinClassName().name().equals(cabinClass)).collect(Collectors.toList());
        fares.stream().forEach(x -> em.detach(x.getFlightSchedulePlan()));
        fares.stream().forEach(x -> em.detach(x.getCabinClass()));
        fares.stream().forEach(x -> em.detach(x));
        return fares;
   }
   
   
    public static Date formatDate(String dateTimeInput) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeInput, formatter);
        ZoneId zoneId = ZoneId.of("Asia/Singapore");
        Date date = Date.from(dateTime.atZone(zoneId).toInstant());
        return date;
    }
       
    @Override
    public List<FlightReservation> getFlightReservations(long partnerID) {
        return partnerEntitySessionBean.getFlightReservations(partnerID);
    }
    
    @Override
    public List<FlightBooking> getFlightBookingsForReservation(long flightReservationId) {
        FlightReservation flightReservation = em.find(FlightReservation.class, flightReservationId);
        if (flightReservation != null) {
            flightReservation.getFlightBookingList().size();
            List<FlightBooking> list = flightReservation.getFlightBookingList();
            list.stream().forEach(x -> em.detach(x));
            em.detach(flightReservation.getCustomer());
            em.detach(flightReservation.getPartner());
            flightReservation.getPassengerList().size();
            flightReservation.getPassengerList().stream().forEach(x -> em.detach(x));
            return list;
        } else {
            return Collections.emptyList();
        }
    }
    
    @Override
    public List<Seat> getListOfSeats(long flightBookingId) {
        FlightBooking flightBooking = flightBookingEntitySessionBean.findBooking(flightBookingId);
        flightBooking.getReservedSeats().size();
        em.detach(flightBooking);
        List<Seat> toReturn = new ArrayList<Seat>();
        flightBooking.getReservedSeats().stream().forEach(x -> em.detach(x));
        flightBooking.getReservedSeats().stream().forEach(x -> {
            em.detach(x.getFlightCabinClass());
            if (x.getPassenger() != null) em.detach(x.getPassenger());
            toReturn.add(x);
        });
        return toReturn;
    }
    
    @Override
    public FlightCabinClass getFlightCabinClass(long seatId) {
        Seat s = seatEntitySessionBean.findSeat(seatId);
        if (s != null) {
            FlightCabinClass fcc = s.getFlightCabinClass();
            em.detach(fcc);
            fcc.getSeatList().stream().forEach(x -> em.detach(x));
            em.detach(fcc.getCabinClass());
            em.detach(fcc.getFlightSchedule());
            return fcc;
        } else {
            return null;
        }
    }
    
    @Override
    public Passenger getPassengerForSeat(long seatId) {
        Seat s = seatEntitySessionBean.findSeat(seatId);
        if (s != null) {
            Passenger p = s.getPassenger();
            em.detach(s);
            em.detach(p);
            return p;
        } else {
            return null;
        }
    }


}
