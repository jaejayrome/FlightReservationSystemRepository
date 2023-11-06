/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/EjbWebServiceNoOp.java to edit this template
 */
package ejb.session.ws;

import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import ejb.session.stateless.PartnerUseCaseSessionBeanLocal;
import entity.Partner;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import util.exception.InvalidLoginCredentialsException;

/**
 *
 * @author jeromegoh
 */
@WebService(serviceName = "FlightReservationSystemWebService")
@Stateless
public class FlightReservationSystemWebService {
    
    // prof i not sure why need to reference a partner use case sseio bean logo

    @EJB
    private PartnerUseCaseSessionBeanLocal partnerUseCaseSessionBeanLocal;
 
    @WebMethod(operationName = "partnerLogin")
    public long partnerLogin(@WebParam(name = "username") String username, @WebParam(name = "password") String password) {
        return partnerUseCaseSessionBeanLocal.partnerLogin(username, password);
    }
    
    @WebMethod(operationName = "partnerLogout") 
    public void partnerLogout(@WebParam(name = "partnerId") long id) {
        partnerUseCaseSessionBeanLocal.partnerLogout(id);
    }
}

    
