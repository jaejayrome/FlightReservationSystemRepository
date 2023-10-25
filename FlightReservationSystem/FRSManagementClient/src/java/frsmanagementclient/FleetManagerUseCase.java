/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.EmployeeUseCaseSessionBeanRemote;
import entity.AircraftConfiguration;
import entity.AircraftType;
import entity.Employee;
import java.util.List;
import java.util.Scanner;
import util.enumerations.JobTitle;

/**
 *
 * @author jeromegoh
 */
public class FleetManagerUseCase {
    private EmployeeUseCaseSessionBeanRemote employeeUseCaseSessionBeanRemote;
    private Employee currentEmployee;
    private List<AircraftType> aircraftTypeList;
    private Scanner scanner;
    
    public FleetManagerUseCase() {
    }
    
    public FleetManagerUseCase(EmployeeUseCaseSessionBeanRemote employeeUseCaseSessionBeanRemote, Employee currentEmployee) {
        this.employeeUseCaseSessionBeanRemote = employeeUseCaseSessionBeanRemote;
        this.currentEmployee = currentEmployee;
        this.aircraftTypeList = employeeUseCaseSessionBeanRemote.getAllAircraftTypes();
        this.scanner = new Scanner(System.in);
    }
    
    /*
    Fetch for the aircraft types (cached) first Ask the route planner what aircraft type does he want 
    He would choose 
    Then ask for the configuration name
    */
    public void createAircraftConfiguration() {
           if (aircraftTypeList.size() > 0) {
               printAircraftType();
               System.out.print("Enter the Name of the Aircraft Type: ");
               String name = this.scanner.nextLine();
               
               if(!name.isEmpty()) {
                System.out.print("Enter the name of the configuration: ");
                String configurationName = this.scanner.nextLine();
                employeeUseCaseSessionBeanRemote.createAircraftConfigurationForFleetManager(JobTitle.FLEET_MANAGER, name, configurationName);
               }
           }
    }
    
    public void viewAllAircraftConfiguration() {
          List<AircraftConfiguration> aircraftConfigurations =employeeUseCaseSessionBeanRemote.viewAllAircraftConfiguration();
          int counter = 1;
          for (AircraftConfiguration aircraftConfiguration : aircraftConfigurations) {
                    printSingleAircraftConfiguration(aircraftConfiguration, counter);
                    counter+=1;                
            }
    
//        if (aircraftTypeList.size() > 0) {
//            int counter = 1;
//            for (AircraftType aircraftType : aircraftTypeList) {
//                List<AircraftConfiguration> aircraftConfigurations = aircraftType.getAircraftConfigurations();
//                for (AircraftConfiguration aircraftConfiguration : aircraftConfigurations) {
//                    printSingleAircraftConfiguration(aircraftConfiguration, counter, aircraftType);
//                    counter+=1;
//                }
//            }
//        }
    }
    
    public void viewAircraftConfigurationDetails() {
        System.out.println("Enter Configuration Name: ");
        String configurationName = scanner.nextLine();
        AircraftConfiguration aircraftConfiguration = employeeUseCaseSessionBeanRemote.viewAircraftConfigurationDetails(configurationName);
        printSingleAircraftConfiguration(aircraftConfiguration, 1);
    }
    
    public void printSingleAircraftConfiguration(AircraftConfiguration aircraftConfiguration, int counter) {
        System.out.println("Aircraft Configuration #" + counter);
        System.out.println("Configuration Name: " + aircraftConfiguration.getConfigurationName());
        System.out.println("Configuration Aircraft Type Name: " + aircraftConfiguration.getAircraftType().getAircraftTypeName());
        System.out.println("Configuration Aircraft Type Manufacturer: " + aircraftConfiguration.getAircraftType().getManufacturer());
        System.out.println("-----------------------------------------------");
    }
    
    public void printAircraftType() {
        for (AircraftType aircraftType: aircraftTypeList) {
            System.out.println("Name: " + aircraftType.getAircraftTypeName());
            System.out.println("Manufacturer: " + aircraftType.getManufacturer());
            System.out.println("Passenger Seat Capacity : " + aircraftType.getPassengerSeatCapacity());
            System.out.println("-----------------------------------------------");
        }
    }
    
}
