/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.EmployeeUseCaseSessionBeanRemote;
import java.util.Scanner;
import javax.ejb.EJB;
import util.enumerations.JobTitle;
import util.exception.InvalidLoginCredentialsException;

/**
 *
 * @author jeromegoh
 */
public class RunApp {
        
    @EJB
    private EmployeeUseCaseSessionBeanRemote employeeUseCaseSessionBeanRemote;
    
    public RunApp() {
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
 
//        try {
            System.out.println(employeeUseCaseSessionBeanRemote == null);
            employeeUseCaseSessionBeanRemote.test();
//            JobTitle jobTitle = employeeUseCaseSessionBeanRemote.doLogin(username, password);
//            showUseCaseOptions(scanner, jobTitle);
//        } catch (InvalidLoginCredentialsException exception) {
//            System.out.println(exception.getMessage());
//            doLogin(scanner);
//        } 
    }
    
    public void showUseCaseOptions(Scanner scanner, JobTitle jobtitle) {
        switch (jobtitle) {
            case FLEET_MANAGER:
                System.out.println("Press '1' to Create Aircraft Configuration");
                System.out.println("Press '2' to View All Aircraft Configurations");
                System.out.println("Press '3' to View Specific Aircraft Configuration Details");
                System.out.print("> ");
                FleetManagerUseCase fleetManagerUseCase = new FleetManagerUseCase();
                switch (scanner.nextInt()) {
                    case 1: 
                        fleetManagerUseCase.creatAircraftConfiguration();
                        break;
                    case 2: 
                        fleetManagerUseCase.viewAllAircraftConfiguration();
                        break;
                    case 3: 
                        fleetManagerUseCase.viewAircraftConfigurationDetails();
                        break;
                    default:
                        invalidOption();
                }
                break;
            case ROUTE_PLANNER:
                System.out.println("Press '1' to Create Flight Route");
                System.out.println("Press '2' to View All Flight Routes");
                System.out.println("Press '3' to Delete Flight Route");
                System.out.print("> ");
                RoutePlannerUseCase routerPlannerUseCase = new RoutePlannerUseCase();
                switch (scanner.nextInt()) {
                    case 1: 
                        routerPlannerUseCase.createFlightRoute();
                    case 2: 
                        routerPlannerUseCase.viewAllFlightRoutes();
                        break;
                    case 3: 
                        routerPlannerUseCase.deleteFlighRoute();
                        break;
                    default:
                        invalidOption();
                }
                break;
            case SALES_MANAGER:
                System.out.println("Press '1' to View Seats Inventory");
                System.out.println("Press '2' to View Flight Reservations");
                System.out.print("> ");
                SalesManagerUseCase salesManagerUseCase = new SalesManagerUseCase();
                switch (scanner.nextInt()) {
                    case 1: 
                        salesManagerUseCase.viewSeatsinventory();
                        break;
                    case 2: 
                        salesManagerUseCase.viewFlightReservations();
                        break;
                    default:
                        invalidOption();
                }
                break;
                
            case SCHEDULE_MANAGER:
                System.out.println("Press '1' to Create Flight");
                System.out.println("Press '2' to View All Flights");
                System.out.println("Press '3' to View Specific Flight Details");
                System.out.println("Press '4' to Create Flight Schedule Plan");
                System.out.println("Press '5' to View All Flight Schedule Plans");
                System.out.println("Press '6' to View Flight Schedule Plan Details");
                System.out.print("> ");
                ScheduleManagerUseCase scheduleManagerUseCase = new ScheduleManagerUseCase();
                switch (scanner.nextInt()) {
                    case 1: 
                        scheduleManagerUseCase.createFlight();
                        break;
                    case 2: 
                        scheduleManagerUseCase.viewAllFlights();
                        break;
                    case 3: 
                        scheduleManagerUseCase.viewSpecificFlightDetails();
                        break;
                    case 4: 
                        scheduleManagerUseCase.createFlightSchedulePlan();
                        break;
                    case 5: 
                        scheduleManagerUseCase.viewAllFlightSchedulePlan();
                        break;
                    case 6: 
                        scheduleManagerUseCase.viewFlightSchedulePlanDetails();
                        break;
                    default:
                        invalidOption();
                }
                break;
        }
     
    }
    
    public void invalidOption() {
        System.out.println("You have selected an invalid option!");
    }
    
 
    
    
}
