/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
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
        props.put("javax.persistence.cache.retrieveMode", "USE");
        
    }
    
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public long createNewEmployee(String firstName, String lastName, GenderType gender, String email, String phoneNumber, JobTitle jobTitle, EmploymentType typeOfEmployment, String loginUsername, String loginPassword) {
        Employee employee = new Employee(firstName, lastName, gender, email, phoneNumber, jobTitle, typeOfEmployment, loginUsername, loginPassword);
        Set<ConstraintViolation<Employee>> constraints = validator.validate(employee);
        if (constraints.size() == 0) {
            em.persist(employee);
            em.flush();
            return employee.getId();
        } else {
            ejbContext.setRollbackOnly();
            return -1;
        }
    }
    
    public Employee retrieveEmployeeById(long id) throws EmployeeNotFoundException{
        Employee employee = em.find(Employee.class, id, this.props);
        if (employee == null) {
            throw new EmployeeNotFoundException("Employee has not been found yet!");
        } else {
            return employee;
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
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
