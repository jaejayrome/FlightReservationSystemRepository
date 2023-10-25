/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import util.exception.InvalidLoginCredentialsException;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class EmployeeUseCaseSessionBean implements EmployeeUseCaseSessionBeanRemote, EmployeeUseCaseSessionBeanLocal {

    
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
    
    
    
}
