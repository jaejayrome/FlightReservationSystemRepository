/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hrspartnerclient;

import ws.entity.FlightReservationSystemWebService;
import ws.entity.FlightReservationSystemWebService_Service;

/**
 *
 * @author jeromegoh
 */
public class HRSPartnerClient {

    public static void main(String[] args) {
        FlightReservationSystemWebService_Service service = new FlightReservationSystemWebService_Service();
        FlightReservationSystemWebService port = service.getFlightReservationSystemWebServicePort();
        port.partnerLogin("trivago", "password");
         
    }
    
}
