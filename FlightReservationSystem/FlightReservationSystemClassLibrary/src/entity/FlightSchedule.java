/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.time.Duration;
import java.util.ArrayList;
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
import javax.validation.constraints.Future;

/**
 *
 * @author jeromegoh
 */
@Entity
public class FlightSchedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Future
    @Column(nullable = false)
    private Date departureTime; 
    
    @Column(nullable = false)
    private Duration flightDuration;
     
    @Column(nullable = false)
    private Date arrivalTime;

    // relationships
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    private FlightSchedulePlan flightSchedulePlan;
    
    @OneToMany(mappedBy = "flightSchedule")
    private List<FlightBooking> flightBookingList;
    
    @OneToMany(mappedBy = "flightSchedule")
    private List<FlightCabinClass> fccList;
    

    public FlightSchedule() {
    }

    public FlightSchedule(Date departureTime, Duration flightDuration, Date arrivalTime) {
        this.departureTime = departureTime;
        this.flightDuration = flightDuration;
        this.arrivalTime = arrivalTime;
        this.fccList = new ArrayList<FlightCabinClass>();
        this.flightBookingList = new ArrayList<FlightBooking>();
    }

    public List<FlightBooking> getFlightBookingList() {
        return flightBookingList;
    }

    public void setFlightBookingList(List<FlightBooking> flightBookingList) {
        this.flightBookingList = flightBookingList;
    }
    
    public List<FlightCabinClass> getFccList() {
        return fccList;
    }

    public void setFccList(List<FlightCabinClass> fccList) {
        this.fccList = fccList;
    }

    public FlightSchedulePlan getFlightSchedulePlan() {
        return flightSchedulePlan;
    }

    public void setFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        this.flightSchedulePlan = flightSchedulePlan;
    }


    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Duration getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(Duration flightDuration) {
        this.flightDuration = flightDuration;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
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
        if (!(object instanceof FlightSchedule)) {
            return false;
        }
        FlightSchedule other = (FlightSchedule) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightSchedule[ id=" + id + " ]";
    }
    
}
