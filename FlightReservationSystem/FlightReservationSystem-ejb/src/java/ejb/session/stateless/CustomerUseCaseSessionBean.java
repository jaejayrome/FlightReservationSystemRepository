/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Flight;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private FlightScheduleEntitySessionBeanLocal flightScheduleEntitySessionBean;
    
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
    
    
    
    /*
    TODOs: 
    
    - Change query type to origin iata code etc. 
    - Implement array tacking where arr [0] is first leg of flight, arr[1] is 2nd leg of flight
    
    */
    @Override
    //no need add return flights
    public List<List<FlightSchedule>> searchForFlightRoutes(
        String departureAirport, Date departureDate, String destinationAirport, Date returnDate, int directFlight) {
        
        List<List<FlightSchedule>> toReturn = new ArrayList<List<FlightSchedule>>();
        //if directFlight == 1, mean prefer directlight 
        //if directFlight == 2, means no pref
        
        
        if (directFlight == 1) { //one way, direct
            
            List<FlightSchedule> flightScheduleList = em.createQuery("Select fs FROM FlightSchedule "
                + "WHERE fs.DEPARTURETIME >= DATEADD(userDepartDate, 2 @1200"
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
            
            if (returnDate != null) {
                
                
                flightScheduleList = em.createQuery("Select fs FROM FlightSchedule "
                + "WHERE fs.DEPARTURETIME >= DATEADD(userDepartDate, -2 @1200"
                + "AND fs.DEPARTURETIME <= DATEADD(userDepartDate, -2 @1200")
            .setParameter("userDepartDate", returnDate)
            .getResultList();
            }
            
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
            
        } else if (directFlight == 2) { //1 way, no preference
            if (returnDate == null) {
                List<Pair<FlightSchedule>> allConnectingFlights = 
                        getConnectingFlightsOneWay(departureAirport, destinationAirport);
                
                List<FlightSchedule> tmpToReturn3 = new ArrayList<FlightSchedule>();

                
                for (int i = 0; i < allConnectingFlights.size(); i ++) {
                    Pair tmp = allConnectingFlights.get(i);
                    FlightSchedule fs1 = (FlightSchedule)tmp.getFirst();
                    
                    Calendar cal = Calendar.getInstance();
                    
                    cal.setTime(departureDate);
                    
                    cal.add(Calendar.DATE, - 3);
                    
                    Date threeDaysBefore = cal.getTime();
                    
                    cal.add(Calendar.DATE, + 3);
                    
                    Date threeDaysAfter = cal.getTime();
                    
                    
                    if (departureDate.compareTo(threeDaysAfter) <=  0 && departureDate.compareTo(threeDaysBefore) >= 0 ) {
                        //within range
                        
                        
                        FlightSchedulePlan fsp = fs1.getFlightSchedulePlan();
                    
                        if (fsp.getStatus() == FlightSchedulePlanStatus.ACTIVE) {
                            Flight flight = fsp.getFlight();
                            FlightRoute fr = flight.getFlightRoute();
                    
                        if (fr.getOrigin().equals(departureAirport) 
                            && fr.getDestination().equals(destinationAirport)) {
                            //when processing arr need to take into account that this whole thing will be joined flights
                            
                            tmpToReturn3.add((FlightSchedule)tmp.getFirst());
                            tmpToReturn3.add((FlightSchedule)tmp.getSecond());
                        }
                    } 
                }
                    //arr in arr will work like this:
                    //arr[1] will be connecting leg 1
                    //arr[2] will be connecting leg 2
                    
                    
                    
                toReturn.add(tmpToReturn3);
                
            } else if (returnDate != null) {
                
            }
            
        } else {
            return null;
        }
        return toReturn;
    }
    
    
    
    
    // for case 4: return the opposite
    // one way connecting flight
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
                        Pair<FlightSchedule> connectingFlightSchedule = new Pair(x, y);
                        connectingFlightList.add(connectingFlightSchedule);
                    }
                }
            });
        });
        return connectingFlightList;
    }
    
}
