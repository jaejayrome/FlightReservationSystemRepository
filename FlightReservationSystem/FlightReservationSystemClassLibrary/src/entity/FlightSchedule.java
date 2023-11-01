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
public class FlightSchedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Date departureTime; 
    @Column(nullable = false)
    private Duration flightDuration;
    @Column(nullable = false)
    private Date arrivalTime;
//    @Column(nullable = false)
//    private Date endDate;
//    @Column(nullable = false)
//    private BigDecimal frequency;
//    
    // relationships
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private FlightSchedulePlan flightSchedulePlan;
    
    @OneToMany (mappedBy = "flightSchedule")
    private List<CabinClass> cabinClassList;
    // constructors
    

    public FlightSchedule() {
    }

    public FlightSchedule(Date departureTime, Duration flightDuration, Date arrivalTime) {
        this.departureTime = departureTime;
        this.flightDuration = flightDuration;
        this.arrivalTime = arrivalTime;
        this.cabinClassList = new ArrayList<CabinClass>();
    }
    
    // getters and setters
    public List<CabinClass> getCabinClassList() {
        return cabinClassList;
    }

    public void setCabinClassList(List<CabinClass> cabinClassList) {
        this.cabinClassList = cabinClassList;
    }

    public FlightSchedulePlan getFlightSchedulePlan() {
        return flightSchedulePlan;
    }

    public void setFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        this.flightSchedulePlan = flightSchedulePlan;
    }

//    public Date getEndDate() {    
//        return endDate;
//    }
    
//    public void setEndDate(Date endDate) {
//        this.endDate = endDate;
//    }
//
//    public BigDecimal getFrequency() {
//        return frequency;
//    }
//    
//    public void setFrequency(BigDecimal frequency) {
//        this.frequency = frequency;
//    }

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
