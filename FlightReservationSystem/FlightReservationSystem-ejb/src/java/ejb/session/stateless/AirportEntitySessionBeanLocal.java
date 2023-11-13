/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Airport;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jeromegoh
 */
@Local
public interface AirportEntitySessionBeanLocal {
    public Airport createNewAirport(String airportName, String iataAirportCode, String city, String state, String country);
    public List<Airport> getAllAirports();
    public Airport findAirport(String iataCode);
}
