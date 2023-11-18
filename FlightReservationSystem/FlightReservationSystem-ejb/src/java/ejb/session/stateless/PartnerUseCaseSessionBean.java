/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Fare;
import entity.Flight;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.Partner;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
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

/**
 *
 * @author jeromegoh
 */
@Stateless
public class PartnerUseCaseSessionBean implements PartnerUseCaseSessionBeanLocal {
    
    
    
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
    
    @Override 
    public List<FlightSchedule> partnerSearchForFlightRoutesConnecting(String departureAirport, String departureDateS, String destinationAirport, boolean isFirst) {
        Date departureDate = formatDate(departureDateS);
       
        Calendar cal = Calendar.getInstance();
        cal.setTime(departureDate);
        cal.add(Calendar.DAY_OF_YEAR, -3); // Subtract 3 days for the start date
        Date startDate = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, 6); // Add 6 days to the start date to get the end date (3 days after)
        Date endDate = cal.getTime();
        
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

}
