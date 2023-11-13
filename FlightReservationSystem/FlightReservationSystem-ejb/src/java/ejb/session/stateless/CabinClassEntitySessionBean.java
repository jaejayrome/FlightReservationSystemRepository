/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.CabinClass;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class CabinClassEntitySessionBean implements CabinClassEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public long createCabinClass(CabinClass cabinClass) {
        em.persist(cabinClass);
        em.flush();
        return cabinClass.getId();
        // associate cabinClass with aircraftConfiguration
        // association done on the other side already
    }
    // public CabinClass(CabinClassType cabinClassName, BigDecimal numAisles, BigDecimal numRows, BigDecimal numSeatsAbreast, String seatingConfiguration)
    @Override
    public List<CabinClass> recreateCabinClass(List<CabinClass> list, AircraftConfiguration aircraftConfiguration) {
        List<CabinClass> newList = new ArrayList<CabinClass>();
        for (CabinClass cabinClass : newList) {
            CabinClass newCabinClass = new CabinClass(cabinClass.getCabinClassName(), cabinClass.getNumAisles(), cabinClass.getNumRows(), cabinClass.getNumSeatsAbreast(), cabinClass.getSeatingConfiguration());
            newCabinClass.getAircraftConfigurationList().size();
            newCabinClass.getAircraftConfigurationList().add(aircraftConfiguration);
            em.persist(newCabinClass);
            em.flush();
            newList.add(newCabinClass);
        }
        return newList;
    }


}
