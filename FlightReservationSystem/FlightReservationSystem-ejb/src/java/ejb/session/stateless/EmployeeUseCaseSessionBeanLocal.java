/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import javax.ejb.Local;
import util.enumerations.JobTitle;
import util.exception.InvalidLoginCredentialsException;

/**
 *
 * @author jeromegoh
 */
@Local
public interface EmployeeUseCaseSessionBeanLocal {
    public JobTitle doLogin(String username, String password) throws InvalidLoginCredentialsException;
    public void test();
}
