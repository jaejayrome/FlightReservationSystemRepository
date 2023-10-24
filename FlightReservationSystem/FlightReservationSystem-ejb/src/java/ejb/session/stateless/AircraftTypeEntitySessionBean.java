/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftType;
import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class AircraftTypeEntitySessionBean implements AircraftTypeEntitySessionBeanLocal {
    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public long createNewAircraftType(String aircraftTypeName, String manufacturer, BigDecimal passengerSeatCapacity) {
        AircraftType aircraftType = new AircraftType(aircraftTypeName, manufacturer, passengerSeatCapacity);
        em.persist(aircraftType);
        em.flush();
        return aircraftType.getId();
    }
}
