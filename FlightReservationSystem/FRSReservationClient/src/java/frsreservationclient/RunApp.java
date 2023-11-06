/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsreservationclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.CustomerUseCaseSessionBeanRemote;
import java.util.Scanner;

/**
 *
 * @author geraldtan
 */
public class RunApp {
    
    private CustomerSessionBeanRemote customerSessionBean;
    private CustomerUseCaseSessionBeanRemote customerUseCaseSessionBean;
    private boolean customerIsLoggedIn;

    public RunApp() {}
    
    public RunApp(CustomerSessionBeanRemote customerSessionBean, CustomerUseCaseSessionBeanRemote customerUseCaseSessionBean) {
        //expose runApp to remote SessionBean methods
        this.customerSessionBean = customerSessionBean; 
        this.customerUseCaseSessionBean = customerUseCaseSessionBean;
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
                System.out.println("Enter Your Email: ");
                String loginEmail = sc.nextLine();
                System.out.println("Enter Your Password: ");
                String loginPassword = sc.nextLine();
                int customerLogin = customerUseCaseSessionBean.customerLogin(loginEmail, loginPassword);
                switch (customerLogin) {
                    case -1:
                        System.out.println("Customer account has not been "
                                + "created yet, would you like to create a new "
                                + "account?\n");
                        System.out.println("Enter Y to register new account");
                        System.out.println("Enter N try logging in again");
                        String tmpOption = sc.nextLine();
                        if (tmpOption.equals("Y")) {
                            createNewCustomerAccount(sc);
                            this.customerIsLoggedIn = true;
                            System.out.println("You account has been created and you are logged in");
                        } else {
                            userAuthPrompt(sc);
                        }
                        break;
                    case 0:
                        System.out.println("Invalid Password, please try again");
                        userAuthPrompt(sc);

                    case 1:
                        System.out.println("Customer Found & Authenticated");
                        customerIsLoggedIn = true;
                        break;
                    default: 
                        invalidOption();
                        break;
                }
                //case 1 break
                break;
                
            case 2:
            System.out.println("Registering your account");
                createNewCustomerAccount(sc);
            

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
     
     public void createNewCustomerAccount(Scanner sc) {
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
     }
}
