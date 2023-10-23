/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import util.exception.InitialDatabaseException;

/**
 *
 * @author jeromegoh
 */
@Singleton
@LocalBean
@Startup

public class DataInitSessionBean  {

    public DataInitSessionBean() {
    }

    // initialised all the data in the post construct 
    @PostConstruct
    public void seedDatabase() {
//        try {
//            
//        } catch (InitialDatabaseException exception){
//            
//        }
    }
    
    public void initialiseData() {
        
    }
}
