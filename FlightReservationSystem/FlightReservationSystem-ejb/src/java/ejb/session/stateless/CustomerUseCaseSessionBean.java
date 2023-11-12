/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import com.sun.xml.wss.util.DateUtils;
import entity.Customer;
import entity.Flight;
import entity.FlightBooking;
import entity.FlightCabinClass;
import entity.FlightReservation;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.Passenger;
import entity.Seat;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import util.exception.NoFlightFoundException;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import util.enumerations.FlightSchedulePlanStatus;
import util.enumerations.SeatStatus;
import util.exception.CustomerAuthenticationFailedException;
import util.exception.CustomerNotRegisteredException;
import util.util.Pair;

/**
 *
 * @author geraldtan
 */
@Stateless
public class CustomerUseCaseSessionBean implements CustomerUseCaseSessionBeanRemote, CustomerUseCaseSessionBeanLocal {

    
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

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;
    
    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    
    
    public void persist(Object object) {
        em.persist(object);
    }
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    
    @Override
    public long customerLogin(String email, String password) throws CustomerAuthenticationFailedException {
        Customer authCustomer = null;
        try {
        authCustomer = (Customer)em.createQuery(
                "SELECT cust FROM Customer cust "
                    + "WHERE cust.email = :email", 
                Customer.class)
                .setParameter("email", email)
                .getSingleResult();
        } catch (NoResultException e) {
            // this means the customer doesn't exist at all
            throw new CustomerAuthenticationFailedException("noAccount");
        }
        
        try {
            authCustomer = (Customer)em.createQuery(
                "SELECT cust FROM Customer cust "
                    + "WHERE cust.password = :password "
                    + "AND cust.email = :email", 
                Customer.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .getSingleResult();
        } catch (NoResultException e) {
            // this means that the customer password entered is wrong
             throw new CustomerAuthenticationFailedException("passwordFailed");
        }
        
        // if authenticated 
        if (authCustomer != null) return authCustomer.getId();
        else return -1;
    }
    
    
    

    @Override
    //no need add return flights
    public List<List<FlightSchedule>> searchForFlightRoutes(
        String departureAirport, Date departureDate, String destinationAirport, Date returnDate, int directFlight) throws NoFlightFoundException {
        
        List<List<FlightSchedule>> toReturn = new ArrayList<List<FlightSchedule>>();
        //if directFlight == 1, mean prefer directlight 
        //if directFlight == 2, means no pref
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(departureDate);
        cal.add(Calendar.DAY_OF_YEAR, -3); // Subtract 3 days for the start date
        Date startDate = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, 6); // Add 6 days to the start date to get the end date (3 days after)
        Date endDate = cal.getTime();

        // Use the calculated dates in your JPQL query
        List<FlightSchedule> flightScheduleList1 = em.createQuery(
            "SELECT fs FROM FlightSchedule fs WHERE fs.departureTime >= :startDate AND fs.departureTime <= :endDate AND fs.flightSchedulePlan.flight.flightRoute.origin.iataAirportCode = :departureAirport AND fs.flightSchedulePlan.flight.flightRoute.destination.iataAirportCode = :destinationAirport")
            .setParameter("startDate", startDate)
            .setParameter("endDate", endDate)
            .setParameter("departureAirport", departureAirport)
            .setParameter("destinationAirport", destinationAirport)
            .getResultList();
        flightScheduleList1.stream().forEach(x -> {
            int init = x.getFccList().size();
            x.getFccList().stream().forEach(y -> y.getSeatList().size());
            x.getFlightSchedulePlan().getFares().size();
        });
        
        if (flightScheduleList1.size() == 0) {
               throw new NoFlightFoundException("No Flight has been found!");
        }

        toReturn.add(flightScheduleList1);

        if (returnDate != null) {
            // Calculate the start and end dates
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(returnDate);
            cal2.add(Calendar.DAY_OF_YEAR, -3); // Subtract 3 days for the start date
            Date startDate2 = cal2.getTime();
            cal2.add(Calendar.DAY_OF_YEAR, 6);
            Date endDate2 = cal2.getTime();

            // Use the calculated dates in your JPQL query
           List<FlightSchedule> flightScheduleList2 = em.createQuery(
                "SELECT fs FROM FlightSchedule fs WHERE fs.departureTime >= :startDate AND fs.departureTime <= :endDate AND fs.flightSchedulePlan.flight.flightRoute.origin.iataAirportCode = :destinationAirport AND fs.flightSchedulePlan.flight.flightRoute.destination.iataAirportCode = :departureAirport")
                .setParameter("startDate", startDate2)
                .setParameter("endDate", endDate2)
                .setParameter("departureAirport", departureAirport)
                .setParameter("destinationAirport", destinationAirport)
                .getResultList();
           flightScheduleList2.stream().forEach(x -> {
            int init = x.getFccList().size();
            x.getFlightSchedulePlan().getFares().size();
            x.getFccList().stream().forEach(y -> y.getSeatList().size());
        });
           if (flightScheduleList2.size() == 0) {
               throw new NoFlightFoundException("No Flight has been found!");
            }
            toReturn.add(flightScheduleList2);
        }
            
        if (directFlight == 2) { // 1 way. connecting
                // add 2 lists: to -> 1 & 2
            List<FlightSchedule> leg1 = new ArrayList<FlightSchedule>();
            List<FlightSchedule> leg2 = new ArrayList<FlightSchedule>();
            List<Pair<FlightSchedule>> listOfAllCombinations = getConnectingFlightsOneWay(departureAirport, destinationAirport);
            Calendar cal3 = Calendar.getInstance();
            cal3.setTime(departureDate);
            cal3.add(Calendar.DAY_OF_YEAR, -3);
            Date startDate3 = cal3.getTime();
            cal3.add(Calendar.DAY_OF_YEAR, 6);
            Date endDate3 = cal3.getTime();
            
            listOfAllCombinations = listOfAllCombinations.stream().filter(x -> x.getFirst().getDepartureTime().after(startDate3) && x.getFirst().getDepartureTime().before(endDate3)).collect(Collectors.toList());
            for (Pair<FlightSchedule> connectingFlightPair : listOfAllCombinations) {
                connectingFlightPair.getFirst().getFccList().size();
                connectingFlightPair.getFirst().getFlightSchedulePlan().getFares().size();
                connectingFlightPair.getFirst().getFccList().stream().forEach(x -> x.getSeatList().size());
                leg1.add(connectingFlightPair.getFirst());
                
                connectingFlightPair.getSecond().getFccList().size();
                connectingFlightPair.getSecond().getFlightSchedulePlan().getFares().size();
                connectingFlightPair.getSecond().getFccList().stream().forEach(x -> x.getSeatList().size());
                leg2.add(connectingFlightPair.getSecond());
            }
           toReturn.add(leg1);
           toReturn.add(leg2);
           // return leg
            if (returnDate != null) {
                List<FlightSchedule> leg3 = new ArrayList<FlightSchedule>();
                List<FlightSchedule> leg4 = new ArrayList<FlightSchedule>();
                List<Pair<FlightSchedule>> listOfAllCombinationsS = getConnectingFlightsOneWay(destinationAirport, departureAirport);
                Calendar cal4 = Calendar.getInstance();
                cal4.setTime(departureDate);
                cal4.add(Calendar.DAY_OF_YEAR, -3);
                Date startDate4 = cal4.getTime();
                cal4.add(Calendar.DAY_OF_YEAR, 6);
                Date endDate4 = cal4.getTime();
                listOfAllCombinations = listOfAllCombinations.stream().filter(x -> x.getFirst().getDepartureTime().after(startDate4) && x.getFirst().getDepartureTime().before(endDate4)).collect(Collectors.toList());
                for (Pair<FlightSchedule> connectingFlightPair : listOfAllCombinationsS) {
                    connectingFlightPair.getFirst().getFccList().size();
                    connectingFlightPair.getFirst().getFlightSchedulePlan().getFares().size();
                    connectingFlightPair.getFirst().getFccList().stream().forEach(x -> x.getSeatList().size());
                    leg3.add(connectingFlightPair.getFirst());
                    
                    connectingFlightPair.getSecond().getFccList().size();
                    connectingFlightPair.getSecond().getFlightSchedulePlan().getFares().size();
                    connectingFlightPair.getSecond().getFccList().stream().forEach(x -> x.getSeatList().size());
                    leg4.add(connectingFlightPair.getSecond());
                }
                toReturn.add(leg3);
                toReturn.add(leg4);
            }
        } 
        return toReturn;
    }
    
      public List<Pair<FlightSchedule>> getConnectingFlightsOneWay(String originIATACode, String destinationIATACode) {
          // NEED CHECK AGAIN
        List<FlightSchedule> originList = flightScheduleEntitySessionBean.getFlightSchedulesByAirportOrigin(originIATACode);
        List<FlightSchedule> destinationList = flightScheduleEntitySessionBean.getFlightSchedulesByAirportDestination(destinationIATACode);
        List<Pair<FlightSchedule>> connectingFlightList = new ArrayList<Pair<FlightSchedule>>();
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
                        Pair<FlightSchedule> connectingFlightSchedule = new Pair<FlightSchedule>(x, y);
                        connectingFlightList.add(connectingFlightSchedule);
                    }
                }
            });
        });
        return connectingFlightList;
    }
    
      // need a list<list<string>>, each list would contain the seat numbers, ned take in flightcabinclass 
    @Override
    public FlightReservation makeFlightReservation(long customerId, List<Long> flightScheduleIdList, List<Long> flightCabinClassList, List<List<String>> seatNumberList, List<HashMap<Integer, String>> passengerDetails, String creditCardNumber) {
        int numPassengers = passengerDetails.size();

        // get Customer 
        Customer customer = customerSessionBean.getCustomerById(customerId);

        // association between flight reservation and customer,
        FlightReservation flightReservation = new FlightReservation(customer, creditCardNumber);
        
         //  persist the flight reservation
        flightReservation = flightReservationEntitySessionBean.makeFlightReservation(flightReservation);

        int init1 = flightReservation.getPassengerList().size();
        
        // take in passenger data
        for (HashMap<Integer, String> passenger : passengerDetails) {
            String firstName = passenger.get(1);
            String lastName = passenger.get(2);
            String passportNumber = passenger.get(3);
            // persist the passenger into the database
            Passenger persistPassenger = new Passenger(firstName, lastName, passportNumber);
            passengerEntitySessionBean.persistPassenger(persistPassenger);
            // association between flight reservation and passenger
            flightReservation.getPassengerList().add(persistPassenger);
        }

        
        // association between customer to flight reservation
        customer.getFlightReservationList().size();
        customer.getFlightReservationList().add(flightReservation);

        // persist the flight booking
        for (int i = 0; i < flightScheduleIdList.size(); i++) {
            // cannot use seat booking because there might be duplciate seat numbers between cabin classes
            // massive assumption, assuming that one flight schedule and one cabin class is chosen
            FlightBooking flightBooking = this.makeFlightBooking(flightScheduleIdList.get(i), flightCabinClassList.get(i), seatNumberList.get(i), flightReservation);
            // association between flight reservation and flightBooking 
            int init = flightReservation.getFlightBookingList().size();
            flightReservation.getFlightBookingList().add(flightBooking);            
            flightBooking.setFlightReservation(flightReservation);
        }

        return flightReservation;
    }
    
    // persist the flight booking
    // handle the lgogic of the seats
    public FlightBooking makeFlightBooking(long flightSchdeduleId, long flightCabinClassId, List<String> seatNumber, FlightReservation flightReservation) {
            FlightSchedule flightSchedule = flightScheduleEntitySessionBean.getFlightScheduleById(flightSchdeduleId);
            String flightNumber = flightSchedule.getFlightSchedulePlan().getFlight().getFlightNumber();
            FlightCabinClass flightCabinClass = flightCabinClassEntitySessionBean.getFCCByID(flightCabinClassId);
            
            BigDecimal prevReserved = flightCabinClass.getNumReservedSeats();
            BigDecimal newReserved = prevReserved.add(new BigDecimal(seatNumber.size()));
            BigDecimal newBalanced = flightCabinClass.getNumAvailableSeats().subtract(newReserved);
            // updated the numbers 
            flightCabinClass.setNumReservedSeats(newReserved);
            flightCabinClass.setNumBalanceSeats(newBalanced);
            
            List<Seat> toAdd = new ArrayList<Seat>();
            
            // navigate to the correct flight cabin class
            flightCabinClass.getSeatList().size();
            for (String str : seatNumber) {
                for (Seat s : flightCabinClass.getSeatList()) {
                    if (s.getSeatNumber().equals(str)) {
                        s.setSeatStatus(SeatStatus.RESERVED);
                        toAdd.add(s);
                    }
                }
            }
            // associationdone flight booking to flight schedule
            FlightBooking flightBooking = new FlightBooking(flightNumber, flightSchedule);
            flightBooking.setFlightReservation(flightReservation);
            flightBooking = flightBookingEntitySessionBean.makeBooking(flightBooking);
            int init = flightBooking.getReservedSeats().size();
            // association done flight booking to seats
            flightBooking.getReservedSeats().addAll(toAdd);
            return flightBooking;
    }
    
}
