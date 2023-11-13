/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.CabinClass;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jeromegoh
 */
@Local
public interface CabinClassEntitySessionBeanLocal {
    public long createCabinClass(CabinClass cabinClass);
    public List<CabinClass> recreateCabinClass(List<CabinClass> list, AircraftConfiguration aircraftConfiguration);
}
