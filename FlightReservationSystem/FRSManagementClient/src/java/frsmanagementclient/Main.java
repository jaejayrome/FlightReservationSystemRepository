/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package frsmanagementclient;

import ejb.stateless.TestSessionBeanRemote;
import entity.Employee;
import javax.ejb.EJB;

/**
 *
 * @author jeromegoh
 */
public class Main {

    @EJB
    private static TestSessionBeanRemote testSessionBean;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(testSessionBean.addEmployee("Jerome"));
    }
    
}
