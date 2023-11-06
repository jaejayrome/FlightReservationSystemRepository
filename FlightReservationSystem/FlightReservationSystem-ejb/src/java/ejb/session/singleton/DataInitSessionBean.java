/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.AircraftTypeEntitySessionBeanLocal;
import javax.annotation.PostConstruct;
import ejb.session.stateless.AirportEntitySessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import entity.Employee;
import java.math.BigDecimal;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import util.enumerations.AircraftTypeName;
import util.enumerations.EmploymentType;
import util.enumerations.GenderType;
import util.enumerations.JobTitle;
import util.enumerations.RoleType;
import util.exception.EmployeeNotFoundException;
import util.exception.InitialDatabaseException;

/**
 *
 * @author jeromegoh
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean  {

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    
    @EJB
    private AirportEntitySessionBeanLocal airportEntitySessionBeanLocal;
    @EJB
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBeanLocal;
    @EJB
    private PartnerEntitySessionBeanLocal partnerEntitySessionBeanLocal;
    @EJB
    private AircraftTypeEntitySessionBeanLocal aircraftTypeEntitySessionBeanLocal;
    
    public DataInitSessionBean() {
    }

    // initialised all the data in the post construct 
    @PostConstruct
    public void seedDatabase() {
        try {
            checkDatabaseInitialised();
        } catch (InitialDatabaseException exception) {
            initialiseData();
            System.out.println(exception.getMessage());
        }
    }
    // 
    public void checkDatabaseInitialised() throws InitialDatabaseException{
        // there's an issue i don't know why which is when i delete the entire database
        // i would have to load the schema once so basically not run the intiialise data portion
        // then i run it once more with the test data
        try {
            Employee employee = employeeEntitySessionBeanLocal.retrieveEmployeeById(1l);
        } catch (EmployeeNotFoundException exception) {
            throw new InitialDatabaseException("Database has not been seeded yet!");
        }
    }
    
    public void initialiseData() {
        System.out.println("Seeding the database with initial data");
//        // create a new airport
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
        
        // there would be total 16 employees initialised
        // String firstName, String lastName, GenderType gender, String email, String phoneNumber, JobTitle jobTitle, EmploymentType employmentType, String loginUsername, String loginPassword
        // create 2 sales manager
        employeeEntitySessionBeanLocal.createNewEmployee("John", "Doe", GenderType.MALE, "johndoe@email.com", "91234567", JobTitle.SALES_MANAGER, EmploymentType.FULL_TIME, "johndoe", "12345678");
        employeeEntitySessionBeanLocal.createNewEmployee("Isabel", "Tan", GenderType.FEMALE, "isabeltan@email.com", "92324546", JobTitle.SALES_MANAGER, EmploymentType.FULL_TIME, "isabeltan", "12345678");
        
        // create 2 fleet managers
        employeeEntitySessionBeanLocal.createNewEmployee("Chris", "Chia", GenderType.MALE, "chrischia@email.com", "93568623", JobTitle.FLEET_MANAGER, EmploymentType.FULL_TIME, "chrischia", "12345678");
        employeeEntitySessionBeanLocal.createNewEmployee("Samantha", "Lee", GenderType.FEMALE, "samanthalee@email.com", "97753324", JobTitle.FLEET_MANAGER, EmploymentType.FULL_TIME, "samanthalee", "12345678");
        
        // create 2 schedule managers
        employeeEntitySessionBeanLocal.createNewEmployee("Lucas", "Seah", GenderType.MALE, "lucasseah@email.com", "95334522", JobTitle.SCHEDULE_MANAGER, EmploymentType.FULL_TIME, "lucasseah", "12345678");
        employeeEntitySessionBeanLocal.createNewEmployee("Amanda", "Koh", GenderType.FEMALE, "amandakoh@email.com", "99232937", JobTitle.SCHEDULE_MANAGER, EmploymentType.FULL_TIME, "amandakoh", "12345678");
        
        // create 10 route planners
        employeeEntitySessionBeanLocal.createNewEmployee("Jason", "Tan", GenderType.MALE, "jasontan@email.com", "91224232", JobTitle.ROUTE_PLANNER, EmploymentType.FULL_TIME, "jasontan", "12345678");
        employeeEntitySessionBeanLocal.createNewEmployee("Cheryl", "Chia", GenderType.FEMALE, "cherylchia@email.com", "92324133", JobTitle.ROUTE_PLANNER, EmploymentType.FULL_TIME, "cherylchia", "12345678");
        employeeEntitySessionBeanLocal.createNewEmployee("Ben", "Tan", GenderType.MALE, "bentan@email.com", "92324242", JobTitle.ROUTE_PLANNER, EmploymentType.FULL_TIME, "bentan", "12345678");
        employeeEntitySessionBeanLocal.createNewEmployee("Jasmine", "Lim", GenderType.FEMALE, "jasminelim@email.com", "92424267", JobTitle.ROUTE_PLANNER, EmploymentType.FULL_TIME, "jasminelim", "12345678");
        employeeEntitySessionBeanLocal.createNewEmployee("Jayden", "Lee", GenderType.MALE, "jaydenlee@email.com", "92457234", JobTitle.ROUTE_PLANNER, EmploymentType.FULL_TIME, "jaydenlee", "12345678");
        employeeEntitySessionBeanLocal.createNewEmployee("Cynthia", "Chong", GenderType.FEMALE, "cynthiachong@email.com", "96383484", JobTitle.ROUTE_PLANNER, EmploymentType.FULL_TIME, "cynthiachong", "12345678");
        employeeEntitySessionBeanLocal.createNewEmployee("Elliot", "Teo", GenderType.MALE, "elliotteo@email.com", "92432424", JobTitle.ROUTE_PLANNER, EmploymentType.FULL_TIME, "elliottoh", "12345678");
        employeeEntitySessionBeanLocal.createNewEmployee("Tania", "Wong", GenderType.FEMALE, "taniawong@email.com", "98234733", JobTitle.ROUTE_PLANNER, EmploymentType.FULL_TIME, "taniawong", "12345678");
        employeeEntitySessionBeanLocal.createNewEmployee("Jordan", "Soh", GenderType.MALE, "jordansoh@email.com", "92242374", JobTitle.ROUTE_PLANNER, EmploymentType.FULL_TIME, "jordansoh", "12345678");
        employeeEntitySessionBeanLocal.createNewEmployee("Nicole", "Goh", GenderType.FEMALE, "nicolegoh@email.com", "93547345", JobTitle.ROUTE_PLANNER, EmploymentType.FULL_TIME, "nicolegoh", "12345678");
        
        
        // create 3 partners
        partnerEntitySessionBeanLocal.createNewPartner("Booking.com", "booking.com", "password");
        partnerEntitySessionBeanLocal.createNewPartner("Trivago", "trivago", "password");
        partnerEntitySessionBeanLocal.createNewPartner("Skyscanner", "skyscanner", "password");
        
        // initialise 2 aircraft type // BOEIGN 747 and BOEING 737
        aircraftTypeEntitySessionBeanLocal.createNewAircraftType(AircraftTypeName.BOEING_737, new BigDecimal(215));
        aircraftTypeEntitySessionBeanLocal.createNewAircraftType(AircraftTypeName.BOEING_747, new BigDecimal(416));
    
        //default customer creation is already init as roleType.customer
        customerSessionBeanLocal.createNewCustomer("FirstName", "LastName", "email@test", "phoneNumber", "Address", "Password");

    }
}
