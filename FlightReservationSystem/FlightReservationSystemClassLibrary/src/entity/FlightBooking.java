/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jeromegoh
 */
@Entity
public class FlightBooking implements Serializable {

    // attributes
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  
    
    @NotNull
    @Size(max = 10)
    @Column(nullable = false)
    private String flightNumber;
    
    @Column(nullable = false)
    private BigDecimal flightLegCost;
    
    // relationships
    @OneToMany
    private List<Seat> reservedSeats;
    
    @ManyToOne (optional = false)
    @JoinColumn (nullable = false)
    private FlightSchedule flightSchedule;
    
    @ManyToOne (optional = false)
    @JoinColumn (nullable = false)
    private FlightReservation flightReservation;

    // constructor 
    public FlightBooking() {
    }

    public FlightBooking(String flightNumber, FlightSchedule flightSchedule, BigDecimal flightLegCost) {
        this.flightNumber = flightNumber;
        this.reservedSeats = new ArrayList<Seat>();
        this.flightSchedule = flightSchedule;
        this.flightLegCost = flightLegCost;
    }
    
    // getters and setter

    public BigDecimal getFlightLegCost() {
        return flightLegCost;
    }

    public void setFlightLegCost(BigDecimal flightLegCost) {
        this.flightLegCost = flightLegCost;
    }
    
    public FlightReservation getFlightReservation() {
        return flightReservation;
    }

    public void setFlightReservation(FlightReservation flightReservation) {
        this.flightReservation = flightReservation;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public List<Seat> getReservedSeats() {
        return reservedSeats;
    }

    public void setReservedSeats(List<Seat> reservedSeats) {
        this.reservedSeats = reservedSeats;
    }

    public FlightSchedule getFlightSchedule() {
        return flightSchedule;
    }

    public void setFlightSchedule(FlightSchedule flightSchedule) {
        this.flightSchedule = flightSchedule;
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
        if (!(object instanceof FlightBooking)) {
            return false;
        }
        FlightBooking other = (FlightBooking) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightBooking[ id=" + id + " ]";
    }
    
}