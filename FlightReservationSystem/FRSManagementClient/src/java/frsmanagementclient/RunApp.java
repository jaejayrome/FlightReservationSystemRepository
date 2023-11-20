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
import java.util.stream.IntStream;

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
//        printEstablish();
        IntStream.rangeClosed(1, 5).forEach(x -> System.out.println());
        System.out.println("Welcome to Merlion Airlines Flight Management System!");
        doLogin(scanner);
        
        scanner.close();
    }
    
    public void printPlane() {
        IntStream.rangeClosed(1, 2).forEach(x -> System.out.println());
        System.out.println("                                   |");
        System.out.println("                                   |");
        System.out.println("                                 .-'-.");
        System.out.println("                                ' ___ '");
        System.out.println("                       ---------'  .-.  '---------");
        System.out.println("       _________________________'  '-'  '_________________________");
        System.out.println("        ''''''-|---|--/    \\==][^',_m_,'^][==/    \\--|---|-''''''");
        System.out.println("                      \\    /  ||/   H   \\||  \\    /");
        System.out.println("                       '--'   OO   O|O   OO   '--'");
    }

    
    public void printLogo() {

 System.out.println("                      _ _                     _      _ _                                     ");
        System.out.println("  _ __ ___   ___ _ __| (_) ___  _ __     __ _(_)_ __| (_)_ __   ___  ___                     ");
        System.out.println(" | '_ ` _ \\ / _ \\ '__| | |/ _ \\| '_ \\   / _` | | '__| | | '_ \\ / _ \\/ __|                    ");
        System.out.println(" | | | | | |  __/ |  | | | (_) | | | | | (_| | | |  | | | | | |  __/\\__ \\                    ");
        System.out.println(" |_|_|_|_|_|\\___|_|  |_|_|\\___/|_| |_|  \\__,_|_|_|  |_|_|_| |_|\\___||___/               _    ");
        System.out.println("  / _| (_) __ _| |__ | |_   _ __ ___   __ _ _ __   __ _  __ _  ___ _ __ ___   ___ _ __ | |_  ");
        System.out.println(" | |_| | |/ _` | '_ \\| __| | '_ ` _ \\ / _` | '_ \\ / _` |/ _` |/ _ \\ '_ ` _ \\ / _ \\ '_ \\| __| ");
        System.out.println(" |  _| | | (_| | | | | |_  | | | | | | (_| | | | | (_| | (_| |  __/ | | | | |  __/ | | | |_  ");
        System.out.println(" |_| |_|_\\__, |_| |_|\\__| |_| |_| |_|\\__,_|_| |_|\\__,_|\\__, |\\___|_| |_| |_|\\___|_| |_|\\__| ");
        System.out.println("  ___ _   |___/| |_ ___ _ __ ___                        |___/                                ");
        System.out.println(" / __| | | / __| __/ _ \\ '_ ` _ \\                                                            ");
        System.out.println(" \\__ \\ |_| \\__ \\ ||  __/ | | | | |                                                           ");
        System.out.println(" |___/\\__, |___/\\__\\___|_| |_| |_|                                                           ");
        System.out.println("      |___/                                                                                   ");
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
        System.out.println("Enter Username: ");
        System.out.print("> ");
        String username = scanner.next();
        scanner.nextLine();
        System.out.println("Enter Password: ");
        System.out.print("> ");
        String password = scanner.next();
        scanner.nextLine();
 
        try {
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
                System.out.println("-------------------------------");
                FleetManagerUseCase fleetManagerUseCase = new FleetManagerUseCase(this.fleetManagerUseCaseSessionBean, this.currentEmployee);
                System.out.print("> ");
                int choice = scanner.nextInt();
                switch (choice) {
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
                System.out.println("-------------------------------");
                RoutePlannerUseCase routerPlannerUseCase = new RoutePlannerUseCase(routePlannerUseCaseSessionBeanRemote, this.currentEmployee);
                System.out.print("> ");
                int choice1 = scanner.nextInt();
                switch (choice1) {
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
                SalesManagerUseCase salesManagerUseCase = new SalesManagerUseCase(this.currentEmployee, salesManagerUseCaseSessionBeanRemote);
                System.out.println("-------------------------------");
                System.out.print("> ");
                
                int choice2 = scanner.nextInt();
                System.out.println("");
                switch (choice2) {
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
                System.out.println("Press '11' to View Seat Inventory");
                System.out.println("Press '12' to View Flight Reservations");
                System.out.println("Press '0' to Logout from this session");
                System.out.println("-------------------------------");
                ScheduleManagerUseCase scheduleManagerUseCase = new ScheduleManagerUseCase(employeeUseCaseSessionBeanRemote, scheduleManagerUseCaseSessionBeanRemote);
                System.out.print("> ");
                int choice3 = scanner.nextInt();
                switch (choice3) {
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
                    case 11: 
                        scheduleManagerUseCase.viewSeatsInventory();
                        showUseCaseOptions(scanner, jobtitle);
                        break;
                    case 12: 
                        scheduleManagerUseCase.viewFlightReservations();
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
