/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.FleetManagerUseCaseSessionBeanRemote;
import entity.AircraftConfiguration;
import entity.AircraftType;
import entity.Employee;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import util.enumerations.CabinClassType;
import util.enumerations.JobTitle;

/**
 *
 * @author jeromegoh
 */
public class FleetManagerUseCase {
    
    private FleetManagerUseCaseSessionBeanRemote fleetManagerUseCaseSessionBeanRemote;
    private Employee currentEmployee;
    private List<AircraftType> aircraftTypeList;
    private Scanner scanner;
    private HashMap<Integer, CabinClassType> hashMap;
    
    public FleetManagerUseCase() {
    }
    
    public FleetManagerUseCase(FleetManagerUseCaseSessionBeanRemote fleetManagerUseCaseSessionBean, Employee currentEmployee) {
        this.fleetManagerUseCaseSessionBeanRemote = fleetManagerUseCaseSessionBean;
        this.currentEmployee = currentEmployee;
        this.aircraftTypeList = this.fleetManagerUseCaseSessionBeanRemote.getAllAircraftTypes();
        this.scanner = new Scanner(System.in);
        this.hashMap = new HashMap<Integer, CabinClassType>();
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
                
                // last step 
                // initialise the cabin class
                List<CabinClassType> cabinClassNameList = new ArrayList<CabinClassType>();
                List<Integer> numAislesList = new ArrayList<Integer>();
                List<Integer> numRowsList = new ArrayList<Integer>();
                List<Integer> numSeatsAbreastList = new ArrayList<Integer>();
                List<String> seatingConfigurationList = new ArrayList<String>();
                promptUserForCabinClass(cabinClassNameList, numAislesList, numRowsList, numSeatsAbreastList, seatingConfigurationList);
                
                fleetManagerUseCaseSessionBeanRemote.createAircraftConfigurationForFleetManager(JobTitle.FLEET_MANAGER, name, configurationName
                    , cabinClassNameList, numAislesList, numRowsList, numSeatsAbreastList, seatingConfigurationList);
               }
           }
    }
    
    public void viewAllAircraftConfiguration() {
          List<AircraftConfiguration> aircraftConfigurations= fleetManagerUseCaseSessionBeanRemote.viewAllAircraftConfiguration();
          int counter = 1;
          Comparator<AircraftConfiguration> comparator = (x, y) -> x.getAircraftType().getAircraftTypeName().name().compareTo(y.getAircraftType().getAircraftTypeName().name()) == 0 ? 
                                                                x.getConfigurationName().compareTo(y.getConfigurationName()) 
                                                                : x.getAircraftType().getAircraftTypeName().name().compareTo(y.getAircraftType().getAircraftTypeName().name());
          // ensures that list is sorted in accordance to the configuration name and the type
          List <AircraftConfiguration> sortedList = aircraftConfigurations.stream().sorted(comparator).collect(Collectors.toList());
          
          for (AircraftConfiguration aircraftConfiguration : sortedList) {
                    printSingleAircraftConfiguration(aircraftConfiguration, counter);
                    counter+=1;                
          }
    }
    
    public void promptUserForCabinClass(List<CabinClassType> cabinClassNameList, List<Integer> numAislesList, List<Integer> numRowsList, List<Integer> numSeatsAbreastList, List<String> seatingConfigurationList) {
        System.out.println("Enter the number of cabin classes you would like to add for this configuration");
        System.out.print("> ");
        int number = scanner.nextInt();
        for (int i = 1; i <= number; i++) {
            System.out.println("Details for cabin class #" + i);
            initialiseMap();
            printAllCabinClassTypes();
            System.out.println("Choose the cabin class type");
            System.out.print("> ");
            int choice = scanner.nextInt();
            cabinClassNameList.add(hashMap.get(choice));
            System.out.println("Enter the number of aisles");
            System.out.print("> ");
            int numAisles = scanner.nextInt();
            numAislesList.add(numAisles);
            System.out.println("Enter the number of rows");
            System.out.print("> ");
            int numRows = scanner.nextInt();
            numRowsList.add(numRows);
            System.out.println("Enter the number of seats abreast");
            System.out.print("> ");
            int numSeatsAbreast = scanner.nextInt();
            numSeatsAbreastList.add(numSeatsAbreast);
            System.out.println("Enter the seating configuration");
            System.out.println("e.g 3-4-3 or 2-3-2");
            System.out.print("> ");
            String seatingConfiguration = scanner.next();
            seatingConfigurationList.add(seatingConfiguration);
        }
    }
    
    public void viewAircraftConfigurationDetails() {
        System.out.println("Enter Configuration Name: ");
        String configurationName = scanner.nextLine();
        AircraftConfiguration aircraftConfiguration = fleetManagerUseCaseSessionBeanRemote.viewAircraftConfigurationDetails(configurationName);
        printSingleAircraftConfiguration(aircraftConfiguration, 1);
    }
    
    public void printSingleAircraftConfiguration(AircraftConfiguration aircraftConfiguration, int counter) {
        System.out.println("Aircraft Configuration #" + counter);
        System.out.println("Configuration Name: " + aircraftConfiguration.getConfigurationName());
        System.out.println("Configuration Aircraft Type Name: " + aircraftConfiguration.getAircraftType().getAircraftTypeName());
        // System.out.println("Configuration Aircraft Type Manufacturer: " + aircraftConfiguration.getAircraftType().getManufacturer());
        System.out.println("-----------------------------------------------");
    }
    
    public void printAircraftType() {
        for (AircraftType aircraftType: aircraftTypeList) {
            System.out.println("Name: " + aircraftType.getAircraftTypeName());
//            System.out.println("Manufacturer: " + aircraftType.getManufacturer());
            System.out.println("Passenger Seat Capacity : " + aircraftType.getPassengerSeatCapacity());
            System.out.println("-----------------------------------------------");
        }
    }
    
    public void printAllCabinClassTypes() {
        System.out.println("Press 1 for First Class");
        System.out.println("Press 2 for Business Class");
        System.out.println("Press 3 for Premium Economy Class");
        System.out.println("Press 4 for Economy Class");
    }
    
    public void initialiseMap() {
        hashMap.put(1, CabinClassType.FIRST);
        hashMap.put(2, CabinClassType.BUSINESS);
        hashMap.put(3, CabinClassType.PREMIUM_ECONOMY);
        hashMap.put(4, CabinClassType.ECONOMY);
    }
    
}
