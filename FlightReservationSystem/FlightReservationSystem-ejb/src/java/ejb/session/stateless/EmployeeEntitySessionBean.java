/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumerations.EmploymentType;
import util.enumerations.GenderType;
import util.enumerations.JobTitle;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialsException;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class EmployeeEntitySessionBean implements EmployeeEntitySessionBeanLocal {
    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    public long createNewEmployee(String firstName, String lastName, GenderType gender, String email, String phoneNumber, JobTitle jobTitle, EmploymentType typeOfEmployment, String loginUsername, String loginPassword) {
        Employee employee = new Employee(firstName, lastName, gender, email, phoneNumber, jobTitle, typeOfEmployment, loginUsername, loginPassword);
        em.persist(employee);
        em.flush();
        return employee.getId();
    }
    
    public Employee retrieveEmployeeById(long id) throws EmployeeNotFoundException {
        Employee employee = em.find(Employee.class, id);
        if (employee == null) {
            throw new EmployeeNotFoundException("Employee has not been found yet!");
        } else {
            return employee;
        }
    }
    
    public JobTitle authenticateEmployeeDetails(String username, String password) throws InvalidLoginCredentialsException{
        String query = "SELECT employee FROM Employee employee WHERE employee.loginUsername = :username";
        Employee employee = (Employee)em.createQuery(query)
                .setParameter("username", username)
                .getSingleResult();
        if (employee == null) {
            throw new InvalidLoginCredentialsException("Your have entered an invalid username!");
        } else if (!employee.getLoginPassword().equals(password)) {
            throw new InvalidLoginCredentialsException("You have entered an invalid password!");
        } else {
            employee.setIsLoggedIn(true);
            return employee.getJobTitle();
        }
    }
    
    // can be done better
    public JobTitle retrieveJobTitleById(long id) {
        try {
        return this.retrieveEmployeeById(id).getJobTitle();
        } catch (EmployeeNotFoundException exception) {
            System.out.println("No Employee Has Been Found!");
        }
        return null;
    }
}
