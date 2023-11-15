/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author jeromegoh
 */
@Entity
public class FlightReservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // attributes
    @Column (nullable = false)
    private String creditCardNumber; 
    
    @Column (nullable = false)
    private Date date;
    
    // relationships
    @OneToMany (mappedBy= "flightReservation", cascade = CascadeType.PERSIST)
    private List<FlightBooking> flightBookingList;
    
    @OneToMany
    private List<Passenger> passengerList;
    
    @ManyToOne (optional = false)
    @JoinColumn (nullable = false)
    private Customer customer;

    public FlightReservation() {    
    }

    public FlightReservation(Customer customer, String creditCardNumber) {
        this.customer = customer;
        this.flightBookingList = new ArrayList<FlightBooking>();
        this.passengerList = new ArrayList<Passenger>();
        this.creditCardNumber = creditCardNumber;
        this.date = new Date();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }
    
    

    public List<Passenger> getPassengerList() {
        return passengerList;
    }

    public void setPassengerList(List<Passenger> passengerList) {
        this.passengerList = passengerList;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<FlightBooking> getFlightBookingList() {
        return flightBookingList;
    }

    public void setFlightBookingList(List<FlightBooking> flightBookingList) {
        this.flightBookingList = flightBookingList;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FlightReservation)) {
            return false;
        }
        FlightReservation other = (FlightReservation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightReservation[ id=" + id + " ]";
    }
    
}
