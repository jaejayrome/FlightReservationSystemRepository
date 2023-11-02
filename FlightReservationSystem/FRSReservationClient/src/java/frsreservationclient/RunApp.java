/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsreservationclient;
import ejb.session.stateless.CustomerSessionBeanRemote;
import entity.Customer;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author geraldtan
 */
public class RunApp {    
    private CustomerSessionBeanRemote customerSessionBeanRemote;
            
    public RunApp() {}
    

    
    public RunApp(CustomerSessionBeanRemote customerSessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
    }

    public void showLoginScreen() {
        Scanner scanner = new Scanner(System.in);
        printPlane();
        printLogo();
        printEstablish();
//        doLogin(scanner);
        
        scanner.close();
    }
    
    public void getAllCustomers() {
        List<Customer> allCustomers = customerSessionBeanRemote.retrieveAllCustomer();
        for (int i = 0; i < allCustomers.size(); i ++ ) {
            System.out.println(allCustomers.get(i).getFirstName());
        }
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
    
    public void showUseCaseOptions(Scanner scanner) {
    
    
    }
    
}
