/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Passenger;
import javax.ejb.Local;

/**
 *
 * @author jeromegoh
 */
@Local
public interface PassengerEntitySessionBeanLocal {
    public Passenger persistPassenger(Passenger passenger);
    public Passenger findPassenger(long id);
}
