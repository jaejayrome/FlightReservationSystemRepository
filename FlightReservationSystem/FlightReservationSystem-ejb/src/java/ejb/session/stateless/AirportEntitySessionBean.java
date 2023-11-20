/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftType;
import entity.Airport;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.AirportNotFoundException;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class AirportEntitySessionBean implements AirportEntitySessionBeanLocal {
    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static Validator validator = validatorFactory.getValidator();
    
    @Override
    public Airport createNewAirport(String airportName, String iataAirportCode, String city, String state, String country) {
        Airport airport = new Airport (airportName, iataAirportCode, city, state, country);
         Set<ConstraintViolation<Airport>> errors = validator.validate(airport);
        if (errors.size() == 0) {
            em.persist(airport);
            em.flush();
            return airport;
        } else return null;
    }
    
    @Override
    public List<Airport> getAllAirports() {
        return em.createQuery("SELECT a FROM Airport a").getResultList();
    }
    
    @Override
    public Airport findAirport(String iataCode) throws AirportNotFoundException {
        try {
        return (Airport)(em.createQuery("SELECT a FROM Airport a WHERE a.iataAirportCode = :airportCode").setParameter("airportCode", iataCode).getSingleResult());
        } catch (NoResultException e) {
            throw new AirportNotFoundException("TRANSACTION ABORTED: AIRPORT NOT FOUND IN DATABASE");
        }
        
    }
    
    @Override
    public Airport findAirportForDataInit(String iataCode) {
        return (Airport)(em.createQuery("SELECT a FROM Airport a WHERE a.iataAirportCode = :airportCode").setParameter("airportCode", iataCode).getSingleResult());
    }
    
    
}
