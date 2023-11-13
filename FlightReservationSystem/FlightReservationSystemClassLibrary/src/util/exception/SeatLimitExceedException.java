/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.exception;

/**
 *
 * @author jeromegoh
 */
public class SeatLimitExceedException extends Exception{

    public SeatLimitExceedException() {
    }

    public SeatLimitExceedException(String string) {
        super(string);
    }
}
