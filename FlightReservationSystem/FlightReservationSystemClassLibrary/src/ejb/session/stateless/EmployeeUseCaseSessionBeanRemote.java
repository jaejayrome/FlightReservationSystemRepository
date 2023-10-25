/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import javax.ejb.Remote;
import util.exception.InvalidLoginCredentialsException;

/**
 *
 * @author jeromegoh
 */
@Remote
public interface EmployeeUseCaseSessionBeanRemote {
    public Employee doLogin(String username, String password) throws InvalidLoginCredentialsException;
    public void doLogout(long employeeId);
//    public void command1(long ); 
}
