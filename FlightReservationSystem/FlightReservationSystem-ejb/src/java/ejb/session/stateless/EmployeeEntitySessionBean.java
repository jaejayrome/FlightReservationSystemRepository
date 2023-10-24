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
}
