/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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
@Stateless
public class EmployeeEntitySessionBean implements EmployeeEntitySessionBeanLocal {
    
    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static Validator validator = validatorFactory.getValidator();
    
    Map<String, Object> props = new HashMap<String, Object>();
    
    @Resource
    private EJBContext ejbContext;

    public EmployeeEntitySessionBean() {
        // cache retrieval strategy
        props.put("javax.persistence.cache.retrieveMode", "USE");
        
    }
    
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public long createNewEmployee(String firstName, String lastName, GenderType gender, String email, String phoneNumber, JobTitle jobTitle, EmploymentType typeOfEmployment, String loginUsername, String loginPassword) {
        Employee employee = new Employee(firstName, lastName, gender, email, phoneNumber, jobTitle, typeOfEmployment, loginUsername, loginPassword);
        validator.validate(employee);
        em.persist(employee);
        em.flush();
        return employee.getId();
    }
    
//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Employee retrieveEmployeeById(long id) throws EmployeeNotFoundException{
        Employee employee = em.find(Employee.class, id, this.props);
        if (employee == null) {
            // ejbContext.setRollbackOnly();
            throw new EmployeeNotFoundException("Employee has not been found yet!");
//            return null;
        } else {
            return employee;
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Employee authenticateEmployeeDetails(String username, String password) throws InvalidLoginCredentialsException{
        
        String query = "SELECT employee FROM Employee employee WHERE employee.loginUsername = :username";
        List<Employee> employeeList = em.createQuery(query)
                .setParameter("username", username)
                .getResultList();     
        
        if (employeeList.isEmpty()) {
            //no employee in db
             ejbContext.setRollbackOnly();
             throw new InvalidLoginCredentialsException("Your have entered an invalid username! Please try logging in again");
        } else {
            Employee employee = employeeList.get(0);
            if (!employee.getLoginPassword().equals(password)) {
             ejbContext.setRollbackOnly();
             throw new InvalidLoginCredentialsException("You have entered an invalid password! Please try logging in again");
            } else {
                employee.setIsLoggedIn(true);
                return employee;
           }
        }
    }
    
    @Override
    public Employee checkActualDataInitialised(String username) throws InitialDatabaseException {
        try {
        Employee e = (Employee)em.createQuery("SELECT e FROM Employee e WHERE e.loginUsername = :username")
                .setParameter("username", username)
                .getSingleResult();
                return e;
        } catch (NoResultException e) {
            throw new InitialDatabaseException("Database Not Initalised");
        }
    }
    
     public void processLogout(long employeeId) {
         Employee employee = em.find(Employee.class, employeeId);
         employee.setIsLoggedIn(false);
     }
}
