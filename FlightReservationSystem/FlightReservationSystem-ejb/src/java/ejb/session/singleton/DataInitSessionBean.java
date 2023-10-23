/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import javax.annotation.PostConstruct;
import ejb.session.stateless.AirportEntitySessionBeanLocal;
import javax.ejb.EJB;
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
    
    @EJB
    AirportEntitySessionBeanLocal airportEntitySessionBeanLocal;
    
    public DataInitSessionBean() {
    }

    // initialised all the data in the post construct 
    @PostConstruct
    public void seedDatabase() {
        initialiseData();
//        try {
//            
//        } catch (InitialDatabaseException exception){
//            
//        }
    }
    
    public void initialiseData() {
        airportEntitySessionBeanLocal.createNewAirport("Singapore Changi Airport", "SIN", "Singapore", "Singapore", "Singapore");
        airportEntitySessionBeanLocal.createNewAirport("Kuala Lumpur International Airport", "KUL", "Kuala Lumpur", "Kuala Lumpur", "Malaysia");
        airportEntitySessionBeanLocal.createNewAirport("Ngurah Rai International Airport", "DPS", "Depansar", "Depansar", "Indonesia");
        airportEntitySessionBeanLocal.createNewAirport("Halim Perdanakusuma International Airport", "HLP", "Jakarta", "Jakarta", "Indonesia");
        airportEntitySessionBeanLocal.createNewAirport("Husein Sastranegara International Airport", "BDO", "Bandung", "Bandung", "Indonesia");
        airportEntitySessionBeanLocal.createNewAirport("Suvarnabhumi Airport", "BKK", "Bangkok", "Bangkok", "Thailand");
        airportEntitySessionBeanLocal.createNewAirport("Chiang Mai International Airport", "CNX", "Chiang Mai", "Chiang Mai", "Thailand");
        airportEntitySessionBeanLocal.createNewAirport("Krabi Airport", "KBV", "Krabi", "Krabi", "Thailand");
        airportEntitySessionBeanLocal.createNewAirport("Kansai International Airport", "KIX", "Osaka", "Osaka", "Japan");
        airportEntitySessionBeanLocal.createNewAirport("Narita International Airport", "NRT", "Tokyo", "Tokyo", "Japan");
        airportEntitySessionBeanLocal.createNewAirport("Chubu Centrair International Airport", "NGO", "Nagoya", "Nagoya", "Japan");
        airportEntitySessionBeanLocal.createNewAirport("Taiwan Taoyuan International Airport", "TPE", "Taipei", "Taipei", "Taiwan");
        airportEntitySessionBeanLocal.createNewAirport("Kaohsiung International Airport", "KHH", "Kaohsiung", "Kaohsiung", "Taiwan");
        airportEntitySessionBeanLocal.createNewAirport("Beijing Daxing International Airport", "PKX", "Beijing", "Beijing", "China");
        airportEntitySessionBeanLocal.createNewAirport("Shanghai Pudong International Airport", "PVG", "Shanghai", "Shanghai", "China");
        airportEntitySessionBeanLocal.createNewAirport("Guangzhou Baiyun International Airport", "CAN", "Guangzhou", "Guangzhou", "China");
        airportEntitySessionBeanLocal.createNewAirport("Shenzhen Bao'an International Airport", "SGX", "Shenzhen", "Shenzhen", "China");
        airportEntitySessionBeanLocal.createNewAirport("Perth International Airport", "PER", "Perth", "Perth", "Australia");
        airportEntitySessionBeanLocal.createNewAirport("Sydney Kingsford Smith International Airport", "SYD", "Sydney", "Sydney", "Australia");
        airportEntitySessionBeanLocal.createNewAirport("Adelaide International Airport", "ADL", "Adelaide", "Adelaide", "Australia");
        airportEntitySessionBeanLocal.createNewAirport("Melbourne International Airport", "MEL", "Melbourne", "Melbourne", "Australia");
        airportEntitySessionBeanLocal.createNewAirport("Noi Bai International Airport", "HAN", "Hanoi", "Hanoi", "Vietnam");
        airportEntitySessionBeanLocal.createNewAirport("Da Nang International Airport", "DAD", "Danang", "Danang", "Vietnam");
        airportEntitySessionBeanLocal.createNewAirport("Tan Son Nhat International Airport", "SGN", "Ho Chi Minh City", "Ho Chi Minh City", "Vietnam");
        airportEntitySessionBeanLocal.createNewAirport("Gimhae International Airport", "PUS", "Busan", "Busan", "South Korea");
        airportEntitySessionBeanLocal.createNewAirport("Incheon International Airport", "ICN", "Seoul", "Seoul", "South Korea");
        airportEntitySessionBeanLocal.createNewAirport("Jeju International Airport", "CJU", "Jeju", "Jeju", "South Korea");
    }
}
