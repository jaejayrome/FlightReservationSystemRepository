/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Flight;
import entity.FlightBooking;
import entity.FlightReservation;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.Passenger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumerations.FlightSchedulePlanStatus;
import util.util.Pair;

/**
 *
 * @author geraldtan
 */
@Stateless
public class CustomerUseCaseSessionBean implements CustomerUseCaseSessionBeanRemote, CustomerUseCaseSessionBeanLocal {

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
    public int customerLogin(String email, String password) {
        List <Customer> customerEmailLs = em.createQuery(
                "SELECT cust FROM Customer cust "
                    + "WHERE cust.email = :email", 
                Customer.class)
                .setParameter("email", email)
                .getResultList();
        
        if (customerEmailLs.isEmpty()) {
            System.out.println("Customer account has not been created yet");      
            return -1;
        } else {
            //customer email exists in system 
            List <Customer> customer = em.createQuery(
                "SELECT cust FROM Customer cust "
                    + "WHERE cust.password = :password "
                    + "AND cust.email = :email", 
                Customer.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .getResultList();
            
            if (customer.isEmpty()) {
                System.out.println("Invalid Password");
                return 0;
            } else if (!customer.isEmpty()) {
                System.out.println("Customer Found & Authenticated");
                return 1;
            }
        }
        
            
        
        return -1;
    }
    
    
    

    @Override
    //no need add return flights
    public List<List<FlightSchedule>> searchForFlightRoutes(
        String departureAirport, Date departureDate, String destinationAirport, Date returnDate, int directFlight) {
        
        List<List<FlightSchedule>> toReturn = new ArrayList<List<FlightSchedule>>();
        //if directFlight == 1, mean prefer directlight 
        //if directFlight == 2, means no pref
        
       // would always return direct flights
        List<FlightSchedule> flightScheduleList = em.createQuery("Select fs FROM FlightSchedule "
            + "WHERE fs.DEPARTURETIME >= DATEADD(userDepartDate, -2 @1200"
            + "AND fs.DEPARTURETIME <= DATEADD(userDepartDate, -2 @1200")
        .setParameter("userDepartDate", departureDate)
        .getResultList();
        
List<FlightSchedule> tmpToReturn = new ArrayList<FlightSchedule>();
        for (int i = 0; i < flightScheduleList.size(); i ++) {
            FlightSchedulePlan fsp = flightScheduleList.get(i).getFlightSchedulePlan();
            //check if fsp is disable/active
            if (fsp.getStatus() == FlightSchedulePlanStatus.ACTIVE) {
                Flight flight = fsp.getFlight();
                FlightRoute fr = flight.getFlightRoute();

                if (fr.getDestination().equals(destinationAirport) && fr.getOrigin().equals(departureAirport)) {
                    tmpToReturn.add(flightScheduleList.get(i));
                } 
            }
        }

        toReturn.add(tmpToReturn);

        // collect all the FS where the departure time is 
        if (returnDate != null) {
            flightScheduleList = em.createQuery("Select fs FROM FlightSchedule "
            + "WHERE fs.DEPARTURETIME >= DATEADD(userDepartDate, -2 @1200"
            + "AND fs.DEPARTURETIME <= DATEADD(userDepartDate, -2 @1200")
        .setParameter("userDepartDate", returnDate)
        .getResultList();

           List<FlightSchedule> tmpToReturn2 = new ArrayList<FlightSchedule>();

            for (int i = 0; i < flightScheduleList.size(); i ++) {
                FlightSchedulePlan fsp = flightScheduleList.get(i).getFlightSchedulePlan();
                //check if fsp is disable/active
                if (fsp.getStatus() == FlightSchedulePlanStatus.ACTIVE) {
                    Flight flight = fsp.getFlight();
                    FlightRoute fr = flight.getFlightRoute();
                    if (fr.getDestination().equals(departureAirport) && fr.getOrigin().equals(destinationAirport)) {
                        tmpToReturn2.add(flightScheduleList.get(i));
                    } 
                }
            }
            toReturn.add(tmpToReturn2);
        }
            
        if (directFlight == 2) { // 1 way. connecting
            if (returnDate == null) {
                // add 2 lists: to -> 1 & 2
                List<FlightSchedule> leg1 = new ArrayList<FlightSchedule>();
                List<FlightSchedule> leg2 = new ArrayList<FlightSchedule>();
                List<Pair<FlightSchedule>> listOfAllCombinations = getConnectingFlightsOneWay(departureAirport, destinationAirport);
                for (Pair<FlightSchedule> connectingFlightPair : listOfAllCombinations) {
                    leg1.add(connectingFlightPair.getFirst());
                    leg2.add(connectingFlightPair.getSecond());
                }
               toReturn.add(leg1);
               toReturn.add(leg2);
            } else if (returnDate != null) {
                List<FlightSchedule> leg3 = new ArrayList<FlightSchedule>();
                List<FlightSchedule> leg4 = new ArrayList<FlightSchedule>();
                List<Pair<FlightSchedule>> listOfAllCombinations = getConnectingFlightsOneWay(destinationAirport, departureAirport);
                for (Pair<FlightSchedule> connectingFlightPair : listOfAllCombinations) {
                    leg3.add(connectingFlightPair.getFirst());
                    leg4.add(connectingFlightPair.getSecond());
                }
            }
        } 
        return toReturn;
    }
    
      public List<Pair<FlightSchedule>> getConnectingFlightsOneWay(String originIATACode, String destinationIATACode) {
        List<FlightSchedule> originList = flightScheduleEntitySessionBean.getFlightSchedulesByAirportOrigin(originIATACode);
        List<FlightSchedule> destinationList = flightScheduleEntitySessionBean.getFlightSchedulesByAirportDestination(originIATACode);
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
    
    public FlightReservation makeFlightReservation(long customerId, List<Long> flightScheduleIdList, List<List<String>> seatNumberList, List<HashMap<Integer, String>> passengerDetails, String creditCardNumber) {
        int numPassengers = passengerDetails.size();

        // get Customer 
        Customer customer = customerSessionBean.getCustomerById(customerId);
        // association between flight reservation and customer,
        FlightReservation flightReservation = new FlightReservation(customer, creditCardNumber);
        
         //  persist the flight reservation
        flightReservation = flightReservationEntitySessionBean.makeFlightReservation(flightReservation);

        int init1 = flightReservation.getPassengerList().size();
         // make all flight reservation 
        
        // take in passenger data
        for (HashMap<Integer, String> passenger : passengerDetails) {
            String firstName = passenger.get(1);
            String lastName = passenger.get(2);
            String passportNumber = passenger.get(3);
            Passenger persistPassenger = new Passenger(firstName, lastName, passportNumber);
            passengerEntitySessionBean.persistPassenger(persistPassenger);
            flightReservation.getPassengerList().add(persistPassenger);
        }

        
        // association between customer to flight reservation
        customer.getFlightReservationList().size();
        customer.getFlightReservationList().add(flightReservation);

        // persist the flight booking
        for (int i = 0; i < flightScheduleIdList.size(); i++) {
            FlightBooking flightBooking = this.makeFlightBooking(flightScheduleIdList.get(i), seatNumberList.get(i));
            // association between flight reservation and flightBooking 
            int init = flightReservation.getFlightBookingList().size();
            flightReservation.getFlightBookingList().add(flightBooking);            
            flightBooking.setFlightReservation(flightReservation);
        }

        return flightReservation;
    }

    // make only 1 flight booking 
    // if we assume the same cabin 
    public FlightBooking makeFlightBooking(long flightSchdeduleId, List<String> seatNumber) {
            FlightSchedule flightSchedule = flightScheduleEntitySessionBean.getFlightScheduleById(flightSchdeduleId);
            FlightBooking flightBooking = new FlightBooking();
            return flightBookingEntitySessionBean.makeBooking(flightBooking);
    }
    
}