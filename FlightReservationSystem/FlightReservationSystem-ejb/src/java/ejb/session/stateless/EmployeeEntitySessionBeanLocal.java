/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import javax.ejb.Local;
import util.enumerations.EmploymentType;
import util.enumerations.GenderType;
import util.enumerations.JobTitle;
import util.exception.EmployeeNotFoundException;
import util.exception.InitialDatabaseException;
import util.exception.InvalidLoginCredentialsException;

/**
 *
 * @author jeromegoh
 */
@Local
public interface EmployeeEntitySessionBeanLocal {
    public long createNewEmployee(String firstName, String lastName, GenderType gender, String email, String phoneNumber, JobTitle jobTitle, EmploymentType typeOfEmployment, String loginUsername, String loginPassword);
    // public Employee retrieveEmployeeById(long id);
     public Employee retrieveEmployeeById(long id) throws EmployeeNotFoundException;
    // public Employee authenticateEmployeeDetails(String username, String password);
    public void processLogout(long employeeId);
    // edited from this
     public Employee authenticateEmployeeDetails(String username, String password) throws InvalidLoginCredentialsException;
     public Employee checkActualDataInitialised(String username) throws InitialDatabaseException;
    
}
