/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
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
    
    Map<String, Object> props = new HashMap<String, Object>();
    
    @Resource
    private EJBContext ejbContext;

    public EmployeeEntitySessionBean() {
        // cache retrieval strategy
        props.put("javax.persistence.cache.retrieveMode", "USE");
        
    }
    
//    @PostConstruct
//    public void initialise() {
//        // cache store mode strategy
//        if (this.em != null) em.setProperty("javax.persistence.cache.storeMode", "USE");
//    }
    
    
//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public long createNewEmployee(String firstName, String lastName, GenderType gender, String email, String phoneNumber, JobTitle jobTitle, EmploymentType typeOfEmployment, String loginUsername, String loginPassword) {
        Employee employee = new Employee(firstName, lastName, gender, email, phoneNumber, jobTitle, typeOfEmployment, loginUsername, loginPassword);
        em.persist(employee);
        em.flush();
        return employee.getId();
    }
    
//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    
    public Employee retrieveEmployeeById(long id) throws EmployeeNotFoundException{
        Employee employee = em.find(Employee.class, id, this.props);
        if (employee == null) {
            ejbContext.setRollbackOnly();
            throw new EmployeeNotFoundException("Employee has not been found yet!");
//            return null;
        } else {
            return employee;
        }
    }
    
//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Employee authenticateEmployeeDetails(String username, String password) throws InvalidLoginCredentialsException{
        String query = "SELECT employee FROM Employee employee WHERE employee.loginUsername = :username";
        Employee employee = (Employee)em.createQuery(query)
                .setParameter("username", username)
                .getSingleResult();
        if (employee == null) {
             ejbContext.setRollbackOnly();
             throw new InvalidLoginCredentialsException("Your have entered an invalid username!");
        } else if (!employee.getLoginPassword().equals(password)) {
             ejbContext.setRollbackOnly();
             throw new InvalidLoginCredentialsException("You have entered an invalid password!");
        } else {
            employee.setIsLoggedIn(true);
            return employee;
        }
    }
    
     public void processLogout(long employeeId) {
         Employee employee = em.find(Employee.class, employeeId);
         employee.setIsLoggedIn(false);
     }
}
