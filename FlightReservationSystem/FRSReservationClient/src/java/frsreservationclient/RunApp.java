/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsreservationclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import java.util.Scanner;

/**
 *
 * @author geraldtan
 */
public class RunApp {
    
    private CustomerSessionBeanRemote customerSessionBean;

    public RunApp() {}
    
    public RunApp(CustomerSessionBeanRemote customerSessionBean) {
        //expose runApp to remote SessionBean methods
        this.customerSessionBean = customerSessionBean; 
    }
    
    public void showVisitorHomeScreen() {
        Scanner sc = new Scanner(System.in);
        
        userAuthPrompt(sc);
        
        sc.close();
    }
    
    public void userAuthPrompt(Scanner sc) {
        System.out.println("Press '0' to continue as guest");
        System.out.println("Press '1' to Login to an existing account");
        System.out.println("Press '2' to Register an account");
        
        int option = Integer.valueOf(sc.nextLine());
        
        switch(option) {
            case 0:
                System.out.println("Continue as guest");
                userAuthPrompt(sc);
                break;
            case 1:
                System.out.println("Login selected");
                userAuthPrompt(sc);
                break;
            case 2:
                System.out.println("Registering your account");
                
                System.out.println("Input First Name: ");
                String firstName = sc.nextLine();
                System.out.println("Input Last Name: ");
                String lastName = sc.nextLine();
                System.out.println("Input Email: ");
                String email = sc.nextLine();
                System.out.println("Input Phone Number: ");
                String phoneNumber = sc.nextLine();
                System.out.println("Input your Address: ");
                String address = sc.nextLine();
                System.out.println("Input your Password: ");
                String password = sc.nextLine();
                
                customerSessionBean.createNewCustomer(firstName, lastName, 
                        email, phoneNumber, address, password);
                
//                System.out.println("firstname: " + firstName + " lastName: " + lastName + " email : " + email + " phone: " + phoneNumber
//                    + " add: " + address + " pw: " + password);
                
                userAuthPrompt(sc);
                break;
            default: 
                invalidOption();
        }
    }
    
     public void invalidOption() {
        System.out.println("You have selected an invalid option!");
     }
}
