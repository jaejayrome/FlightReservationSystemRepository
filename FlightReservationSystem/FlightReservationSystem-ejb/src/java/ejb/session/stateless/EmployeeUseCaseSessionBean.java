/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.AircraftType;
import entity.Employee;
import entity.FlightRoute;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import util.enumerations.JobTitle;
import util.exception.InvalidLoginCredentialsException;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class EmployeeUseCaseSessionBean implements EmployeeUseCaseSessionBeanRemote, EmployeeUseCaseSessionBeanLocal {

    @EJB
    private AircraftTypeEntitySessionBeanLocal aircraftTypeEntitySessionBean;

    @EJB
    private FlightRouteEntitySessionBeanLocal flightRouteEntitySessionBean;

    @EJB
    private AircraftConfigurationEntitySessionBeanLocal aircraftConfigurationEntitySessionBean;
    
    @EJB
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBeanLocal;
    
    
    public EmployeeUseCaseSessionBean() {
    }
    
    @PostConstruct
    public void postConstruct()
    {
    }
    
    @PreDestroy
    public void preDestroy()
    {
    }
    
    @Override
    public Employee doLogin(String username, String password) throws InvalidLoginCredentialsException {
       return employeeEntitySessionBeanLocal.authenticateEmployeeDetails(username, password);
    }
    
    @Override
    public void doLogout(long employeeId) {
       employeeEntitySessionBeanLocal.processLogout(employeeId);
    }
    
    @Override
    public void employeeCommandOne(JobTitle jobTitle, Object object) {
        if (jobTitle == JobTitle.FLEET_MANAGER && object.getClass().equals(AircraftConfiguration.class)) {
            AircraftConfiguration aircraftConfiguration = (AircraftConfiguration)(object);
            aircraftConfigurationEntitySessionBean.createNewAircraftConfiguration(aircraftConfiguration.getAircraftType(), aircraftConfiguration.getConfigurationName());
        } else if (jobTitle == JobTitle.ROUTE_PLANNER && object.getClass().equals(FlightRoute.class)) {
            FlightRoute flightRoute = (FlightRoute)(object);
            flightRouteEntitySessionBean.createFlightRoute(new FlightRoute(flightRoute.getDestination(), flightRoute.getOrigin()));
        } else {
            return;
        }
        
    }
   
    
    
    @Override
    public long createAircraftConfigurationForFleetManager(JobTitle jobTitle, String aircraftTypeString, String configurationName) {
        if (jobTitle == JobTitle.FLEET_MANAGER) {
            AircraftType aircraftType = aircraftTypeEntitySessionBean.getAircraftTypeFromName(aircraftTypeString);
            return aircraftConfigurationEntitySessionBean.createNewAircraftConfiguration(aircraftType, configurationName);
        } 
        // here should throw an exception (wrong job role exception)
        else return 0L;
    }
    
    @Override
    public List<AircraftType> getAllAircraftTypes() {
        return aircraftTypeEntitySessionBean.getAllAircraftTypes();
    }
    
    @Override
    public List<AircraftConfiguration> viewAllAircraftConfiguration() {
        return aircraftConfigurationEntitySessionBean.getAllAircraftConfigurations();
    }
    
    @Override
    public AircraftConfiguration viewAircraftConfigurationDetails(String configurationName) {
        return aircraftConfigurationEntitySessionBean.getAircraftConfigurationPerConfigurationName(configurationName);
    }
    
    
    
    
}
