/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.EmployeeUseCaseSessionBeanRemote;
import ejb.session.stateless.FleetManagerUseCaseSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.enumerations.JobTitle;
import util.exception.InvalidLoginCredentialsException;
import ejb.session.stateless.RoutePlannerUseCaseSessionBeanRemote;
import ejb.session.stateless.SalesManagerUseCaseSessionBeanRemote;
import ejb.session.stateless.ScheduleManagerUseCaseSessionBeanRemote;

/**
 *
 * @author jeromegoh
 */
public class RunApp {
        
    
    private EmployeeUseCaseSessionBeanRemote employeeUseCaseSessionBeanRemote;
    private RoutePlannerUseCaseSessionBeanRemote routePlannerUseCaseSessionBeanRemote;
    private FleetManagerUseCaseSessionBeanRemote fleetManagerUseCaseSessionBean;
    private ScheduleManagerUseCaseSessionBeanRemote scheduleManagerUseCaseSessionBeanRemote;
    private SalesManagerUseCaseSessionBeanRemote salesManagerUseCaseSessionBeanRemote;
    private Employee currentEmployee;
    
    public RunApp() {
    }
    
    public RunApp(EmployeeUseCaseSessionBeanRemote employeeUseCaseSessionBeanRemote, 
            RoutePlannerUseCaseSessionBeanRemote routePlannerUseCaseSessionBeanRemote, 
            FleetManagerUseCaseSessionBeanRemote fleetManagerUseCaseSessionBeanRemote, 
            ScheduleManagerUseCaseSessionBeanRemote scheduleManagerUseCaseSessionBeanRemote, 
            SalesManagerUseCaseSessionBeanRemote salesManagerUseCaseSessionBeanRemote) {
        this.employeeUseCaseSessionBeanRemote = employeeUseCaseSessionBeanRemote;
        this.routePlannerUseCaseSessionBeanRemote = routePlannerUseCaseSessionBeanRemote;
        this.fleetManagerUseCaseSessionBean = fleetManagerUseCaseSessionBeanRemote;
        this.scheduleManagerUseCaseSessionBeanRemote = scheduleManagerUseCaseSessionBeanRemote;
        this.salesManagerUseCaseSessionBeanRemote = salesManagerUseCaseSessionBeanRemote;
        this.currentEmployee = null;
    }
    
    
    
    public void showLoginScreen() {
        Scanner scanner = new Scanner(System.in);
        printPlane();
        printLogo();
        printEstablish();
        doLogin(scanner);
        
        scanner.close();
    }
    
    public void printPlane() {
    System.out.println("");
    System.out.println("                                      \\ \\");
    System.out.println("                                       \\ `\\");
    System.out.println("                    ___                 \\  \\");
    System.out.println("                   |    \\                \\  `\\");
    System.out.println("                   |_____\\                \\    \\");
    System.out.println("                   |______\\                \\    `\\");
    System.out.println("                   |       \\                \\     \\");
    System.out.println("                   |      __\\__---------------------------------._.");
    System.out.println("                 __|---~~~__o_o_o_o_o_o_o_o_o_o_o_o_o_o_o_o_o_o_[]\\[__");
    System.out.println("                |___                         /~      )                \\__");
    System.out.println("                    ~~~---..._______________/      ,/_________________/");
    System.out.println("                                           /      /");
    System.out.println("                                          /     ,/");
    System.out.println("                                         /     /");
    System.out.println("                                        /    ,/");
    System.out.println("                                       /    /");
    System.out.println("                                      //  ,/");
    System.out.println("                                     //  /");
    System.out.println("                                    // ,/");
    System.out.println("                                   //_/");
    System.out.println("");
    }

    
    public void printLogo() {

    String asciiArt =
        "███╗   ███╗███████╗██████╗ ██╗     ██╗ ██████╗ ███╗   ██╗     █████╗ ██╗██████╗ ██╗     ██╗███╗   ██╗███████╗███████╗\n" +
        "████╗ ████║██╔════╝██╔══██╗██║     ██║██╔═══██╗████╗  ██║    ██╔══██╗██║██╔══██╗██║     ██║████╗  ██║██╔════╝██╔════╝\n" +
        "██╔████╔██║█████╗  ██████╔╝██║     ██║██║   ██║██╔██╗ ██║    ███████║██║██████╔╝██║     ██║██╔██╗ ██║█████╗  ███████╗\n" +
        "██║╚██╔╝██║██╔══╝  ██╔══██╗██║     ██║██║   ██║██║╚██╗██║    ██╔══██║██║██╔══██╗██║     ██║██║╚██╗██║██╔══╝  ╚════██║\n" +
        "██║ ╚═╝ ██║███████╗██║  ██║███████╗██║╚██████╔╝██║ ╚████║    ██║  ██║██║██║  ██║███████╗██║██║ ╚████║███████╗███████║";
    System.out.println("\n");
    System.out.println(asciiArt);
    System.out.println("\n");
    }
    
    public void printEstablish() {
        System.out.println(" _____ ____ _____   ____   ___ ____  _____ ");
        System.out.println(" | ____/ ___|_   _| |___ \\ / _ |___ \\___ / ");
        System.out.println(" |  _| \\___ \\ | |     __) | | | |__) | |_ \\ ");
        System.out.println(" | |___ ___) || |_   / __/| |_| / __/ ___) |");
        System.out.println(" |_____|____/ |_(_) |_____|\\___|_____|____/ ");
    }
    
    public  void subHeader() {
        System.out.println(" ____    ____                                                          _     _______               _        __   ");
        System.out.println("|_   \\  /   _|                                                        / |_  |_   __ \\             / |_     [  |  ");
        System.out.println("  |   \\/   |  ,--.  _ .--.  ,--.  .--./).---. _ .--..--. .---. _ .--.`| |-'   | |__) |.--.  _ .--`| |-,--.  | |  ");
        System.out.println("  | |\\  /| | `'_\\ :[ `.-. |`'_\\ :/ /'`\\/ /__[`.-. .-. / /__\\[ `.-. || |     |  ___/ .'`\\ [ `/'`\\| |`'_\\ : | |  ");
        System.out.println(" _| |_\\/_| |_// | |,| | | |// | |\\ \\._/| \\__.,| | | | | | \\__.,| | | || |,   _| |_  | \\__. || |   | |// | |,| |  ");
        System.out.println("|_____||_____\'-;__[___||__\'-;__.',__` '.__.[___||__||__'.__.[___||__\\__/  |_____|  '.__.'[___]  \\__\'-;__[___] ");
        System.out.println("                                ( ( __))                                                                       ");
    }
    
    public void doLogin(Scanner scanner) {
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.print("Enter Username: ");
        String username = scanner.next();
        System.out.print("Enter Password: ");
        String password = scanner.next();
 
        try {
//            System.out.println(employeeUseCaseSessionBeanRemote == null);
            this.currentEmployee = employeeUseCaseSessionBeanRemote.doLogin(username, password);
            showUseCaseOptions(scanner, this.currentEmployee.getJobTitle());
        } catch (InvalidLoginCredentialsException exception) {
            System.out.println(exception.getMessage());
            doLogin(scanner);
        } 
    }
    
    public void showUseCaseOptions(Scanner scanner, JobTitle jobtitle) {
        switch (jobtitle) {
            case FLEET_MANAGER:
                System.out.println("-------------------------------");
                System.out.println("Press '1' to Create Aircraft Configuration");
                System.out.println("Press '2' to View All Aircraft Configurations");
                System.out.println("Press '3' to View Specific Aircraft Configuration Details");
                System.out.println("Press '0' to Logout from this session");
                System.out.print("> ");
                System.out.println(" ");
                System.out.println("-------------------------------");
                FleetManagerUseCase fleetManagerUseCase = new FleetManagerUseCase(this.fleetManagerUseCaseSessionBean, this.currentEmployee);
                switch (scanner.nextInt()) {
                    case 0: 
                        doLogout(scanner);
                        break;
                    case 1: 
                        fleetManagerUseCase.createAircraftConfiguration();
                        showUseCaseOptions(scanner, jobtitle);
                        break;
                    case 2: 
                        fleetManagerUseCase.viewAllAircraftConfiguration();
                        showUseCaseOptions(scanner, jobtitle);
                        break;
                    case 3: 
                        fleetManagerUseCase.viewAircraftConfigurationDetails();
                        showUseCaseOptions(scanner, jobtitle);
                        break;
                    default:
                        invalidOption();
                }
                break;
            case ROUTE_PLANNER:
                System.out.println("-------------------------------");
                System.out.println("Press '1' to Create Flight Route");
                System.out.println("Press '2' to View All Flight Routes");
                System.out.println("Press '3' to Delete Flight Route");
                System.out.println("Press '0' to Logout from this session");
                System.out.print("> ");
                System.out.println("");
                System.out.println("-------------------------------");
                RoutePlannerUseCase routerPlannerUseCase = new RoutePlannerUseCase(routePlannerUseCaseSessionBeanRemote, this.currentEmployee);
                switch (scanner.nextInt()) {
                    case 0: 
                        doLogout(scanner);
                        break;
                    case 1: 
                         routerPlannerUseCase.createFlightRoute();
                         showUseCaseOptions(scanner, jobtitle);
                         break;
                    case 2: 
                        routerPlannerUseCase.viewAllFlightRoutes();
                        showUseCaseOptions(scanner, jobtitle);
                        break;
                    case 3: 
                        routerPlannerUseCase.deleteFlighRoute();
                        showUseCaseOptions(scanner, jobtitle);
                        break;
                    default:
                        invalidOption();
                }
                break;
            case SALES_MANAGER:
                System.out.println("-------------------------------");
                System.out.println("Press '1' to View Seats Inventory");
                System.out.println("Press '2' to View Flight Reservations");
                System.out.println("Press '0' to Logout from this session");
                System.out.print("> ");
                System.out.println("");
                System.out.println("-------------------------------");
                SalesManagerUseCase salesManagerUseCase = new SalesManagerUseCase(this.currentEmployee, salesManagerUseCaseSessionBeanRemote);
                switch (scanner.nextInt()) {
                    case 0: 
                        doLogout(scanner);
                        break;
                    case 1: 
                        salesManagerUseCase.viewSeatsinventory();
                        showUseCaseOptions(scanner, jobtitle);
                        break;
                    case 2: 
                        salesManagerUseCase.viewFlightReservations();
                        showUseCaseOptions(scanner, jobtitle);
                        break;
                    default:
                        invalidOption();
                }
                break;
                
            case SCHEDULE_MANAGER:
                System.out.println("-------------------------------");
                System.out.println("Press '1' to Create Flight");
                System.out.println("Press '2' to View All Flights");
                System.out.println("Press '3' to View Specific Flight Details");
                System.out.println("Press '4' to Update Flight");
                System.out.println("Press '5' to Delete Flight");
                System.out.println("Press '6' to Create Flight Schedule Plan");
                System.out.println("Press '7' to View All Flight Schedule Plans");
                System.out.println("Press '8' to View Flight Schedule Plan Details");
                System.out.println("Press '9' to Update Flight Schedule Plan Details");
                System.out.println("Press '10' to Delete Flight Schedule Plan Details");
                System.out.println("Press '0' to Logout from this session");
                System.out.print("> ");
                System.out.println("");
                System.out.println("-------------------------------");
                ScheduleManagerUseCase scheduleManagerUseCase = new ScheduleManagerUseCase(employeeUseCaseSessionBeanRemote, scheduleManagerUseCaseSessionBeanRemote);
                switch (scanner.nextInt()) {
                    case 0: 
                        doLogout(scanner);
                        break;
                    case 1: 
                        scheduleManagerUseCase.createFlight();
                        showUseCaseOptions(scanner, jobtitle);
                        break;
                    case 2: 
                        scheduleManagerUseCase.viewAllFlights();
                        showUseCaseOptions(scanner, jobtitle);
                        break;
                    case 3: 
                        scheduleManagerUseCase.viewSpecificFlightDetails();
                        showUseCaseOptions(scanner, jobtitle);
                        break;
                    case 4: 
                        scheduleManagerUseCase.updateFlight();
                        showUseCaseOptions(scanner, jobtitle);
                        break;
                    case 5: 
                        scheduleManagerUseCase.deleteFlight();
                        showUseCaseOptions(scanner, jobtitle);
                        break;
                    case 6: 
                        scheduleManagerUseCase.createFlightSchedulePlan();
                        showUseCaseOptions(scanner, jobtitle);
                        break;
                    case 7: 
                        scheduleManagerUseCase.viewAllFlightSchedulePlan();
                        showUseCaseOptions(scanner, jobtitle);
                        break;
                    case 8: 
                        scheduleManagerUseCase.viewFlightSchedulePlanDetails();
                        showUseCaseOptions(scanner, jobtitle);
                        break;
                    case 9: 
                        scheduleManagerUseCase.updateFlightSchedulePlan();
                        showUseCaseOptions(scanner, jobtitle);
                        break;
                    case 10: 
                        scheduleManagerUseCase.deleteFlightSchedulePlan();
                        showUseCaseOptions(scanner, jobtitle);
                        break;
                    default:
                        invalidOption();
                }
                break;
        }
        
        
     
    }
    
    public void doLogout(Scanner scanner) {
        if (this.currentEmployee != null) {
            employeeUseCaseSessionBeanRemote.doLogout(this.currentEmployee.getId());
            System.out.println("You have sucessfully logged out!");
            doLogin(scanner);
        }
    }
    
    public void invalidOption() {
        System.out.println("You have selected an invalid option!");
    }
}
