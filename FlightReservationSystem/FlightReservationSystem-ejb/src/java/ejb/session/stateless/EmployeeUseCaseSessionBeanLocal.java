/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.AircraftType;
import entity.Employee;
import java.util.List;
import javax.ejb.Local;
import util.enumerations.JobTitle;
import util.exception.InvalidLoginCredentialsException;

/**
 *
 * @author jeromegoh
 */
@Local
public interface EmployeeUseCaseSessionBeanLocal {
    public Employee doLogin(String username, String password) throws InvalidLoginCredentialsException;
    public void doLogout(long employeeId);
    public void employeeCommandOne(JobTitle jobTitle, Object object);
    public List<AircraftType> getAllAircraftTypes();
    public List<AircraftConfiguration> viewAllAircraftConfiguration();
    public AircraftConfiguration viewAircraftConfigurationDetails(String configurationName);
}
