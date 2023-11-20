/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.CabinClass;
import entity.Fare;
import entity.Flight;
import entity.FlightCabinClass;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.MultipleFlightSchedulePlan;
import entity.RecurrentFlightSchedulePlan;
import entity.WeeklyFlightSchedulePlan;
import entity.Seat;
import entity.SingleFlightSchedulePlan;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import util.enumerations.CabinClassType;
import util.enumerations.FlightRouteStatus;
import util.enumerations.FlightSchedulePlanStatus;
import util.enumerations.FlightStatus;
import util.enumerations.SeatStatus;
import util.exception.AirportNotFoundException;
import util.exception.InitialFlightNotInstantiatedException;
import util.exception.NoFlightRouteFoundException;
import util.exception.UpdateFlightSchedulePlanException;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class ScheduleManagerUseCaseSessionBean implements ScheduleManagerUseCaseSessionBeanRemote, ScheduleManagerUseCaseSessionBeanLocal {

    @EJB
    private FlightCabinClassEntitySessionBeanLocal flightCabinClassEntitySessionBean;

    @EJB
    private SeatEntitySessionBeanLocal seatEntitySessionBean;

    @EJB
    private FareEntitySessionBeanLocal fareEntitySessionBean;

    @EJB
    private FlightRouteEntitySessionBeanLocal flightRouteEntitySessionBean;

    @EJB
    private AircraftConfigurationEntitySessionBeanLocal aircraftConfigurationEntitySessionBean;

    @EJB
    private FlightSchedulePlanEntitySessionBeanLocal flightSchedulePlanEntitySessionBean;

    @EJB
    private FlightScheduleEntitySessionBeanLocal flightScheduleEntitySessionBean;

    @EJB
    private FlightEntitySessionBeanLocal flightEntitySessionBean;
    
    
    
    
    
    
    @Override
    public long createNewFlight(String flightNumber, String configurationName, String originAirport, String destinationAirport, boolean createReturn, long initialId) throws InitialFlightNotInstantiatedException {
        
        Flight flight = null;
//        try {
//            long flightId = flightEntitySessionBean.getIdByFlightNumber(flightNumber);
//            throw new DuplicateFlightMadeException("TRANSACTION ABORTED: A flight with this flight number has already been made previously!");
//    } catch (NoResultException exception) {
                try {
                    // cannot use city because there an be a city with 2 airports
                    AircraftConfiguration aircraftConfiguration = aircraftConfigurationEntitySessionBean.getAircraftConfigurationPerConfigurationName(configurationName);
                    FlightRoute flightRoute = flightRouteEntitySessionBean.getFlightRouteByCityName(originAirport, destinationAirport);
                    flight = new Flight(flightNumber, FlightStatus.DISABLED);


                    // associate flight -> aircraft configuration
                    flight.setAircraftConfiguration(aircraftConfiguration);
                    if (!createReturn) {
                        // flight.setAircraftConfiguration(aircraftConfiguration);
                    } else {
        //                AircraftConfiguration returnAC = aircraftConfigurationEntitySessionBean.recreateAircraftConfiguration(aircraftConfiguration);
                    }
                    // associate flight -> flightroute
                    flight.setFlightRoute(flightRoute);

                    // persist to database
                    flightEntitySessionBean.createFlight(flight);

                    // associate flightRoute -> flight
                    int init = flightRoute.getFlightList().size();
                    flightRoute.getFlightList().add(flight);
                    flightRoute.setFlightRouteStatus(FlightRouteStatus.ACTIVE);

                    // associate aircraftConfig -> flight
                    int init2 = aircraftConfiguration.getFlightList().size();
                    aircraftConfiguration.getFlightList().add(flight);

                    // (if createReturnFlight is true)

                    if (createReturn) {
                        long flightGroup = flightEntitySessionBean.getFlightById(initialId).getFlightGroup();
                        flight.setFlightGroup(flightGroup);
                    } else {
                        flight.setFlightGroup(flight.getId());
                    }
                } catch (AirportNotFoundException e) {
                    return -1;
                } catch (NoFlightRouteFoundException e) {
                    // exception is thrown if initial flight cannot be created in the first place
                    throw new InitialFlightNotInstantiatedException("Invalid Flight Route Entered!");
                }

                // would try to create return flight but would return true or false based on this
                try {
                    // if return flight route can be created
                    FlightRoute returnFlightRoute = flightRouteEntitySessionBean.getFlightRouteByCityName(destinationAirport, originAirport);
                    return flight.getId();

                } catch (AirportNotFoundException e) {
                    return -1;
                } catch (NoFlightRouteFoundException e) {
                    // if flight route cannot be created
                    return -1;
                }
        }
    
     @Override
    public long createNewFlightForDataInit(String flightNumber, String configurationName, String originAirport, String destinationAirport, boolean createReturn, long initialId){
        Flight flight = null;
        try {
            // cannot use city because there an be a city with 2 airports
            AircraftConfiguration aircraftConfiguration = aircraftConfigurationEntitySessionBean.getAircraftConfigurationPerConfigurationName(configurationName);
            FlightRoute flightRoute = flightRouteEntitySessionBean.getFlightRouteByCityName(originAirport, destinationAirport);
            flight = new Flight(flightNumber, FlightStatus.DISABLED);
            
            
            // associate flight -> aircraft configuration
            flight.setAircraftConfiguration(aircraftConfiguration);
           
            // associate flight -> flightroute
            flight.setFlightRoute(flightRoute);

            // persist to database
            flightEntitySessionBean.createFlight(flight);

            // associate flightRoute -> flight
            int init = flightRoute.getFlightList().size();
            flightRoute.getFlightList().add(flight);
            flightRoute.setFlightRouteStatus(FlightRouteStatus.ACTIVE);

            // associate aircraftConfig -> flight
            int init2 = aircraftConfiguration.getFlightList().size();
            aircraftConfiguration.getFlightList().add(flight);
            
            // (if createReturnFlight is true)
            
            if (createReturn) {
                long flightGroup = flightEntitySessionBean.getFlightById(initialId).getFlightGroup();
                flight.setFlightGroup(flightGroup);
            } else {
                flight.setFlightGroup(flight.getId());
            }
        } catch (AirportNotFoundException e) {
            return -1;
        } catch (NoFlightRouteFoundException e) {
            // exception is thrown if initial flight cannot be created in the first place
        }
        
        // would try to create return flight but would return true or false based on this
        try {
            // if return flight route can be created
            FlightRoute returnFlightRoute = flightRouteEntitySessionBean.getFlightRouteByCityName(destinationAirport, originAirport);
            return flight.getId();
            
        }  catch (AirportNotFoundException e) {
            return -1;
        }
        catch (NoFlightRouteFoundException e) {
            // if flight route cannot be created
            return -1;
        }
    }
    

    
    @Override
    public List<Flight> viewAllFlights() {
        return flightEntitySessionBean.viewAllFlights();
    }
    
    @Override
    public Flight viewSpecificFlightDetails(String flightNumber) {
        long FlightID = flightEntitySessionBean.getIdByFlightNumber(flightNumber);
        Flight flight = flightEntitySessionBean.getFlightById(FlightID);
        flight.getAircraftConfiguration().getCabinClassList().size();
        flight.getFlightSchedulePlanList().size();
        
        flight.getFlightSchedulePlanList().stream().forEach(x -> {
            x.getFlightScheduleList().size();
            x.getFares().size();
            x.getFlightScheduleList().stream().forEach(y -> {
                y.getFlightBookingList().size();
                y.getFccList().size();
                y.getFccList().stream().forEach(z -> {
                    z.getSeatList().size();
                });
                y.getFlightBookingList().stream().forEach(a -> a.getReservedSeats().size());
            });
            
        });
        
        return flight;
    }
    
    @Override
    public void updateFlightNumber(String flightNumber, String newFlightNumber) {
        long id = flightEntitySessionBean.getIdByFlightNumber(flightNumber);
        Flight flight = flightEntitySessionBean.getFlightById(id);
        flight.setFlightNumber(newFlightNumber);
    }
    
    @Override
    public void updateFlightStatus(String flightNumber, FlightStatus newStatus) {
        long id = flightEntitySessionBean.getIdByFlightNumber(flightNumber);
        Flight flight = flightEntitySessionBean.getFlightById(id);
        flight.setStatus(newStatus);
    } 
    
    @Override
    public boolean deleteFlight(String flightNumber) {
        // check whether is there any flight schedule plan that's associated with this flight 
        try {
        long id = flightEntitySessionBean.getIdByFlightNumber(flightNumber);
               
        Flight flight = flightEntitySessionBean.getFlightById(id);
        
        // check here 
        int init = flight.getFlightSchedulePlanList().size();
        if (init > 0) {
            return flightEntitySessionBean.disableFlight(flight);
            // means that flight associated with 1 or more FSP
        } else {
            // flight is not being used can be deleted
            return flightEntitySessionBean.deleteFlight(flight);
            
        }
        } catch (NoResultException e) {
            System.out.println(e.toString());
        }
        return false;
    }
    
 
    
    @Override
    public long createNewFlightSchedulePlan(String flightNumber, List<Date> departureDateList, Duration duration, Date endDate, int frequency, HashMap<CabinClassType, List<Fare>> faresForCabinClassList, boolean makeReturn, long id, Duration layover) {
        // need to add the fares for cabin class
        // should check for whether duration is null
        
        long flightId = flightEntitySessionBean.getIdByFlightNumber(flightNumber);
        Flight flight = flightEntitySessionBean.getFlightById(flightId);
        
        // update the flight status is active
        flight.setStatus(FlightStatus.ACTIVE);
        
        if (departureDateList.size() == 1 && frequency == 0 ) {
            if (!makeReturn) {
                // create a single flight schedule
                SingleFlightSchedulePlan flightSchedulePlan = this.makeSingleFlightSchedulePlan(flight, departureDateList, duration, faresForCabinClassList);
                flightSchedulePlan.setFlightSchedulePlanGroup(flightSchedulePlan.getId());
                long flightGroupId = flight.getFlightGroup();
                Flight returnFlight = flightEntitySessionBean.checkReturnFlight(flight.getFlightRoute().getOrigin().getIataAirportCode(), flight.getFlightRoute().getDestination().getIataAirportCode(), flightGroupId);
                return returnFlight != null ? flightSchedulePlan.getId() : -1;
            } else {
                // add layover duration to this (return)
                long flightGroupId = flight.getFlightGroup();
                Flight returnFlight = flightEntitySessionBean.checkReturnFlight(flight.getFlightRoute().getOrigin().getIataAirportCode(), flight.getFlightRoute().getDestination().getIataAirportCode(), flightGroupId);
                Date arrivalTime = this.computeArrivalTime(departureDateList.get(0), duration);
                departureDateList.set(0, this.computeArrivalTime(arrivalTime, layover));
                // i should reference the same fares for cabin class and not refernce new one? 
                SingleFlightSchedulePlan flightSchedulePlan = this.makeSingleFlightSchedulePlan(returnFlight, departureDateList, duration, faresForCabinClassList);
                flightSchedulePlan.setFlightSchedulePlanGroup(id);
                return id;
            }
            
        } else if (departureDateList.size() > 1 && frequency == 0) {
            // create a multiple flight schedule
            if (!makeReturn) {
                MultipleFlightSchedulePlan flightSchedulePlan = this.makeMultipleFlightSchedulePlan(flight, departureDateList, duration, faresForCabinClassList);
                flightSchedulePlan.setFlightSchedulePlanGroup(flightSchedulePlan.getId());
                long flightGroupId = flight.getFlightGroup();
                Flight returnFlight = flightEntitySessionBean.checkReturnFlight(flight.getFlightRoute().getOrigin().getIataAirportCode(), flight.getFlightRoute().getDestination().getIataAirportCode(), flightGroupId);
                return returnFlight != null ? flightSchedulePlan.getId() : -1;
            } else {
                // add layover duration to this (return)
                long flightGroupId = flight.getFlightGroup();
                Flight returnFlight = flightEntitySessionBean.checkReturnFlight(flight.getFlightRoute().getOrigin().getIataAirportCode(), flight.getFlightRoute().getDestination().getIataAirportCode(), flightGroupId);
                List<Date> modifiedDepatureList = departureDateList.stream().map(x -> {
                    Date arrivalTime = this.computeArrivalTime(x, duration);
                    return this.computeArrivalTime(arrivalTime, layover);
                }).collect(Collectors.toList());
                MultipleFlightSchedulePlan flightSchedulePlan = this.makeMultipleFlightSchedulePlan(returnFlight, modifiedDepatureList, duration, faresForCabinClassList);
                flightSchedulePlan.setFlightSchedulePlanGroup(id);
                return id;
            }
            
        } else if (departureDateList.size() == 1 && endDate!=null && frequency > 0 && frequency != 7) {
            // create a recurrent flight schedule
            if (!makeReturn) {
                RecurrentFlightSchedulePlan flightSchedulePlan = this.makeRecurrentFlightSchedulePlan(flight, departureDateList, duration, faresForCabinClassList, endDate, frequency);
                flightSchedulePlan.setFlightSchedulePlanGroup(flightSchedulePlan.getId());
                // get the flight group id 
                long flightGroupId = flight.getFlightGroup();
                Flight returnFlight = flightEntitySessionBean.checkReturnFlight(flight.getFlightRoute().getOrigin().getIataAirportCode(), flight.getFlightRoute().getDestination().getIataAirportCode(), flightGroupId);
                return returnFlight != null ? flightSchedulePlan.getId() : -1;
            } else {
                // add layover duration to this (return)
                long flightGroupId = flight.getFlightGroup();
                Flight returnFlight = flightEntitySessionBean.checkReturnFlight(flight.getFlightRoute().getOrigin().getIataAirportCode(), flight.getFlightRoute().getDestination().getIataAirportCode(), flightGroupId);
                departureDateList.set(0, this.computeArrivalTime(this.computeArrivalTime(departureDateList.get(0), duration), layover));
                RecurrentFlightSchedulePlan flightSchedulePlan = this.makeRecurrentFlightSchedulePlan(returnFlight, departureDateList, duration, faresForCabinClassList, endDate, frequency);
                flightSchedulePlan.setFlightSchedulePlanGroup(id);
                return id;
            }
            
        } else {
            // create a recurrent weekly flight schedule (need to assume that a day of the week is being chosen for this)
           if (!makeReturn) {
                WeeklyFlightSchedulePlan flightSchedulePlan = this.makeRecurrentWeeklyFlightSchedulePlan(flight, departureDateList, duration, faresForCabinClassList, endDate);
                flightSchedulePlan.setFlightSchedulePlanGroup(flightSchedulePlan.getId());
                // get the flight group id 
                long flightGroupId = flight.getFlightGroup();
                Flight returnFlight = flightEntitySessionBean.checkReturnFlight(flight.getFlightRoute().getOrigin().getIataAirportCode(), flight.getFlightRoute().getDestination().getIataAirportCode(), flightGroupId);
                return returnFlight != null ? flightSchedulePlan.getId() : -1;
            } else {
               // add layover duration to this (return)
               // idea is that if they have the same flight group and opposite flight route 
                long flightGroupId = flight.getFlightGroup();
                Flight returnFlight = flightEntitySessionBean.checkReturnFlight(flight.getFlightRoute().getOrigin().getIataAirportCode(), flight.getFlightRoute().getDestination().getIataAirportCode(), flightGroupId);
                departureDateList.set(0, this.computeArrivalTime(this.computeArrivalTime(departureDateList.get(0), duration), layover));
                WeeklyFlightSchedulePlan flightSchedulePlan = this.makeRecurrentWeeklyFlightSchedulePlan(returnFlight, departureDateList, duration, faresForCabinClassList, endDate);
                flightSchedulePlan.setFlightSchedulePlanGroup(id);
                return id;
           }
        }
    }
    
    public WeeklyFlightSchedulePlan makeRecurrentWeeklyFlightSchedulePlan(Flight flight, List<Date> departureDateList, Duration duration, HashMap<CabinClassType, List<Fare>> faresForCabinClassList, Date endDate) {
        // persist FSP
        WeeklyFlightSchedulePlan flightSchedulePlan = new WeeklyFlightSchedulePlan(FlightSchedulePlanStatus.ACTIVE, endDate, flight);
        flightSchedulePlanEntitySessionBean.createFlightSchedulePlan(flightSchedulePlan);

        System.out.println();
        // generate the flight schedules for this flight schedule plan
        List<FlightSchedule> collatedFlightSchedules = generateFlightSchedulePlan(departureDateList.get(0), endDate, duration, 7, flightSchedulePlan); 

        // associate FSP to FS
        int init = flightSchedulePlan.getFlightScheduleList().size();
        flightSchedulePlan.getFlightScheduleList().addAll(collatedFlightSchedules);
        
        // make FCC AND FS 
        collatedFlightSchedules.forEach(x -> createFCCsAndSeats(flight, x));
        
        // associate flight to fsp
        int initialise = flight.getFlightSchedulePlanList().size();
        flight.getFlightSchedulePlanList().add(flightSchedulePlan);

        // associate each FS with the new Fare through Cabin Class
       updateAndPersistFare(faresForCabinClassList, flightSchedulePlan);
       return flightSchedulePlan;
    }
    
    public RecurrentFlightSchedulePlan makeRecurrentFlightSchedulePlan(Flight flight, List<Date> departureDateList, Duration duration, HashMap<CabinClassType, List<Fare>> faresForCabinClassList, Date endDate, int frequency) {
        // persist FSP
        RecurrentFlightSchedulePlan flightSchedulePlan = new RecurrentFlightSchedulePlan(FlightSchedulePlanStatus.ACTIVE, endDate, new BigDecimal(frequency), flight);
        flightSchedulePlanEntitySessionBean.createFlightSchedulePlan(flightSchedulePlan);

        // generate the flight schedules for this flight schedule plan
        List<FlightSchedule> collatedFlightSchedules = generateFlightSchedulePlan(departureDateList.get(0), endDate, duration, frequency, flightSchedulePlan); 

        // associate FSP to FS
        int init = flightSchedulePlan.getFlightScheduleList().size();
        flightSchedulePlan.getFlightScheduleList().addAll(collatedFlightSchedules);
        
        // make FCC AND FS 
        collatedFlightSchedules.forEach(x -> createFCCsAndSeats(flight, x));
        
        // associate fsp to flight
        flightSchedulePlan.setFlight(flight);
        
        // associate flight to fsp
        int initialise = flight.getFlightSchedulePlanList().size();
        flight.getFlightSchedulePlanList().add(flightSchedulePlan);

        // associate each FS with the new Fare through Cabin Class
        updateAndPersistFare(faresForCabinClassList, flightSchedulePlan);
        return flightSchedulePlan;
    }
    
    public SingleFlightSchedulePlan makeSingleFlightSchedulePlan(Flight flight, List<Date> departureDateList, Duration duration, HashMap<CabinClassType, List<Fare>> faresForCabinClassList) {
        // persist FSP
        SingleFlightSchedulePlan flightSchedulePlan = new SingleFlightSchedulePlan(FlightSchedulePlanStatus.ACTIVE, flight);
        flightSchedulePlanEntitySessionBean.createFlightSchedulePlan(flightSchedulePlan);

        // persist FS
        FlightSchedule flightSchedule = new FlightSchedule(departureDateList.get(0), duration, computeArrivalTime(departureDateList.get(0), duration));
        flightSchedule.setFlightSchedulePlan(flightSchedulePlan);
        flightScheduleEntitySessionBean.createFlightSchedule(flightSchedule);
        
        flightSchedule = this.createFCCsAndSeats(flight, flightSchedule);
 
        // associate the fsp with the fs (one to many lazy initialisation)
        int init = flightSchedulePlan.getFlightScheduleList().size();
        flightSchedulePlan.getFlightScheduleList().add(flightSchedule);

        // associate fs with fsp
        // flightSchedule.setFlightSchedulePlan(flightSchedulePlan);

        // associate fsp with flight 
        flightSchedulePlan.setFlight(flight);

        // associate flight with fsp
        int initFlight = flight.getFlightSchedulePlanList().size();
        flight.getFlightSchedulePlanList().add(flightSchedulePlan);


        // associate each fs to cabin class
        // associate eaach cabin class to fares
        List<FlightSchedule> flightScheduleList = new ArrayList<FlightSchedule>();
        flightScheduleList.add(flightSchedule);
        updateAndPersistFare(faresForCabinClassList, flightSchedulePlan);
        return flightSchedulePlan;
    }
    
    public MultipleFlightSchedulePlan makeMultipleFlightSchedulePlan(Flight flight, List<Date> departureDateList, Duration duration, HashMap<CabinClassType, List<Fare>> faresForCabinClassList) {
        // persist FSP
            MultipleFlightSchedulePlan flightSchedulePlan = new MultipleFlightSchedulePlan( FlightSchedulePlanStatus.ACTIVE, flight);
            flightSchedulePlanEntitySessionBean.createFlightSchedulePlan(flightSchedulePlan);
            
            // persist FS
            List<FlightSchedule> flightScheduleList = new ArrayList<FlightSchedule>();
            for (int i = 0; i < departureDateList.size(); i++) {
                FlightSchedule flightschedule = new FlightSchedule(departureDateList.get(i), duration, computeArrivalTime(departureDateList.get(i), duration));
                // persist FS
                flightschedule.setFlightSchedulePlan(flightSchedulePlan);
                flightScheduleEntitySessionBean.createFlightSchedule(flightschedule);
                // associate FS to FSP
                // flightschedule.setFlightSchedulePlan(flightSchedulePlan);
                flightScheduleList.add(flightschedule);
            }
            
            
            // associate flight to fsp
            int initialise = flight.getFlightSchedulePlanList().size();
            flight.getFlightSchedulePlanList().add(flightSchedulePlan);
            
            // associate FSP to FS         
            int init = flightSchedulePlan.getFlightScheduleList().size();
            flightSchedulePlan.getFlightScheduleList().addAll(flightScheduleList);
            
            // make FCC AND FS 
            flightScheduleList.forEach(x -> createFCCsAndSeats(flight, x));
            // associate each fs with the new Fare
            updateAndPersistFare(faresForCabinClassList, flightSchedulePlan);
            return flightSchedulePlan;
    }
    
    
    public FlightSchedule createFCCsAndSeats(Flight flight, FlightSchedule flightSchedule) {
        int size = flight.getAircraftConfiguration().getCabinClassList().size();
        int intialise = flightSchedule.getFccList().size();
        
        // fcc is a copy of each cabin class
        // one flight schedule would have 2 fcc if fs follows a flight with ac that's 2 cc
        for (CabinClass cabinClass : flight.getAircraftConfiguration().getCabinClassList()) {
            String seatConfiguration = cabinClass.getSeatingConfiguration();
            int numRows = cabinClass.getNumRows().intValue();
            int numAisles = cabinClass.getNumAisles().intValue();
            int numSeatAbreast = cabinClass.getNumSeatsAbreast().intValue();

            BigDecimal numAvailableSeats = cabinClass.getNumSeatsAbreast().multiply(cabinClass.getNumRows());
            BigDecimal numReservedSeats = BigDecimal.ZERO;
            BigDecimal numBalancedSeats = numAvailableSeats;
            // mandatory relationship: association fcc -> flightschedule, fcc to cabin class
            FlightCabinClass fcc = new FlightCabinClass(numAvailableSeats, numReservedSeats, numBalancedSeats, flightSchedule);
            // mandatory relationship: fcc -> cabinClass association
            fcc.setCabinClass(cabinClass);
            // persist fcc
            flightCabinClassEntitySessionBean.createFlightCabinClass(fcc);
            
            // seats -> fcc done hre
            List<Seat> seatList = this.createSeatsForCabinClass(numRows, numAisles, numSeatAbreast, seatConfiguration, fcc);
            // association fcc -> seats
            int initAgn = fcc.getSeatList().size();
            fcc.getSeatList().addAll(seatList);

            // association -> fs to fcc
            flightSchedule.getFccList().add(fcc);
        }
        return flightSchedule;
    }
    
    @Override
    public List<FlightSchedulePlan> viewAllFlightSchedulePlan() {
        List<FlightSchedulePlan> collatedList = flightSchedulePlanEntitySessionBean.viewAllFlightSchedulePlan();
        Comparator<FlightSchedulePlan> compare = (x, y) -> {
            // sort by ascending order in flight number 
            if (x.getFlight().getFlightNumber().compareTo(y.getFlight().getFlightNumber()) == 0) {
               int init1 = x.getFlightScheduleList().size();
               int init2 = y.getFlightScheduleList().size();
               
               Date xFirstDepature = x.getFlightScheduleList().get(0).getDepartureTime();
               Date yFirstDepature = y.getFlightScheduleList().get(0).getDepartureTime();
               
               // if x firstDeparture is after y, then x should come first 
               return (xFirstDepature.after(yFirstDepature)) ? -1 : 1;
            } else {
               return x.getFlight().getFlightNumber().compareTo(y.getFlight().getFlightNumber());
            }
        };
        
        collatedList = collatedList.stream().sorted(compare).collect(Collectors.toList());
        // instantiate all the flight schedule plans 
        collatedList.forEach(x -> x.getFlightScheduleList().size());
        return collatedList;
    }
    
        @Override
        public void updateFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) throws UpdateFlightSchedulePlanException {
         if (flightSchedulePlan.getId() != null && flightSchedulePlan != null) {
            // find the oldOne 
            long oldId = flightSchedulePlan.getId();
            FlightSchedulePlan oldPlan = flightSchedulePlanEntitySessionBean.getFSPfromID(oldId);
            // check for any flight schedules that ar enot inside the new FSP
            int init = flightSchedulePlan.getFlightScheduleList().size();
            int init2 = oldPlan.getFlightScheduleList().size();
            // iterate through the old plans 
            List<FlightSchedule> deletedPlans = oldPlan.getFlightScheduleList().stream().filter(x -> !(flightSchedulePlan.getFlightScheduleList().contains(x))).collect(Collectors.toList());
            for (FlightSchedule fs : deletedPlans) {
                int init3 = fs.getFlightBookingList().size();
                if (fs.getFlightBookingList().size() > 0) {
                    throw new UpdateFlightSchedulePlanException("You are trying to delete flight schedule plans that have current flight bookings ongoing!");
                }
            }
            // ensure that the price of the booking is not affected, make each booking store their own fare 
            oldPlan.setFares(flightSchedulePlan.getFares());
            oldPlan.setFlightScheduleList(flightSchedulePlan.getFlightScheduleList());
         }


        }

        @Override
        public void deleteFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
            if (flightSchedulePlan != null) {
                 // find the flightSchedulePlan
                 long planToDeleteId = flightSchedulePlan.getId();
                 FlightSchedulePlan planToDelete = flightSchedulePlanEntitySessionBean.getFSPfromID(planToDeleteId);
                 // need to check whether is there any flight schedules that currently have reservations 
                 int init = planToDelete.getFlightScheduleList().size();
                 boolean businessRuleViolated = false;
                 for (FlightSchedule fs : planToDelete.getFlightScheduleList()) {
                     int init1 = fs.getFlightBookingList().size();
                     if (fs.getFlightBookingList().size() > 0) {
                        // means there is at least one fs that has a booking 
                        flightSchedulePlanEntitySessionBean.disableFlightSchedulePlan(planToDelete);
                        businessRuleViolated = true;
                     }
                 }

                 if (!businessRuleViolated) {
                     flightSchedulePlanEntitySessionBean.deleteFlightSchedulePlan(planToDelete);
                 }

            }

        }


        public Date computeArrivalTime(Date departureTime, Duration flightDuration) {
            Instant departureInstant = departureTime.toInstant();
            LocalDateTime departureDateTime = LocalDateTime.ofInstant(departureInstant, ZoneId.systemDefault());
            LocalDateTime arrivalDateTime = departureDateTime.plus(flightDuration);
            Instant arrivalInstant = arrivalDateTime.atZone(ZoneId.systemDefault()).toInstant();
            Date arrivalTime = Date.from(arrivalInstant);

            return arrivalTime;
        }

        public Date addDaysToDate(Date initialDate, int daysToAdd) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(initialDate);
            calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);
            Date updatedDate = calendar.getTime();

            return updatedDate;
        }

        public List<FlightSchedule> generateFlightSchedulePlan(Date startDate, Date endDate, Duration duration, int frequency, FlightSchedulePlan recurrentFlightSchedulePlan) {
            List<FlightSchedule> flightSchedules = new ArrayList<FlightSchedule>();

            Date currentDate = startDate;
            while (currentDate.before(endDate)) {
                // persist FS
                FlightSchedule flightSchedule = new FlightSchedule(currentDate, duration, computeArrivalTime(currentDate, duration));
                flightSchedule.setFlightSchedulePlan(recurrentFlightSchedulePlan);
                flightScheduleEntitySessionBean.createFlightSchedule(flightSchedule);
                // associate each FS to FSP
                flightSchedules.add(flightSchedule);
                currentDate = addDaysToDate(currentDate, frequency);
            }
            return flightSchedules;
        }

        public FlightSchedulePlan updateAndPersistFare(HashMap<CabinClassType, List<Fare>> fareForEveryCabinClass, FlightSchedulePlan flightSchedulePlan) {
            int numberOfCabinClass = flightSchedulePlan.getFlight().getAircraftConfiguration().getCabinClassList().size();
            List<CabinClass> cabinClassList = flightSchedulePlan.getFlight().getAircraftConfiguration().getCabinClassList();
            for (CabinClass cabinClass : cabinClassList) {
                CabinClassType cabinClassName = cabinClass.getCabinClassName();
                List<Fare> fareListForThisCabinClass = fareForEveryCabinClass.get(cabinClassName);
                fareListForThisCabinClass.stream().forEach(x -> {
                    // persist fare
                    // associate fare to fsp
                    Fare f = new Fare(x.getFareBasicCode(), x.getFareAmount(), x.getCabinClass()); 
                    f.setFlightSchedulePlan(flightSchedulePlan);
                    // association between fare -> cabinClass done 
                    fareEntitySessionBean.createFare(f);

                    // associate FSP to fare
                    int init = flightSchedulePlan.getFares().size();
                    flightSchedulePlan.getFares().add(x); 
                });

            }
            return flightSchedulePlan;
        }

        public List<Seat> createSeatsForCabinClass(int numRows, int numAisles, int numSeatAbreast, String seatConfiguration, FlightCabinClass cabinClass) {
            int numCols = numAisles + 1;
            List<String> alphabets = generateLetters(numSeatAbreast);
            List<Seat> seatingList = new ArrayList<Seat>();

            for (int i = 1; i <= numRows; i++) {
                int counter = 0;
                for (int j = 1; j <= numSeatAbreast; j++) {
                    String alphabet = alphabets.get(counter);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(i + alphabet);
                    counter +=1;
                    // creating the seat, associate seat with flight cabin class
                    Seat seat = new Seat(stringBuilder.toString(), SeatStatus.AVAILABLE);
                    seatEntitySessionBean.createSeat(seat);
                    // association seat -> FlightCabinClass
                     seat.setFlightCabinClass(cabinClass);
                    seatingList.add(seat);
                }
            }
            return seatingList;
        }

        public List<String> generateLetters(int numAbreast) {
            List<String> letters = new ArrayList<>();

            char letter = 'A';
            for (int i = 0; i < numAbreast; i++) {
                letters.add(String.valueOf(letter));
                letter++;
            }
            return letters;
        }

}

    
    

        
       
    
    
    
