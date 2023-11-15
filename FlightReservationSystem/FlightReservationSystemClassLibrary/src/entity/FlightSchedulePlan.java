/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import util.enumerations.FlightSchedulePlanStatus;

/**
 *
 * @author jeromegoh
 */
@Entity
@Inheritance(strategy= InheritanceType.JOINED)
public abstract class FlightSchedulePlan implements Serializable {
    
    // attributes
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private FlightSchedulePlanStatus status;
    
    @Column(nullable = true, length = 10000000)
    @Max(10000000)
    private long flightSchedulePlanGroup;
    
    // relationships
    // relationship is optional as a FSP don't need a F
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Flight flight;

    @OneToMany(mappedBy= "flightSchedulePlan", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Fare> fares;
    
    @OneToMany(mappedBy= "flightSchedulePlan", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<FlightSchedule> flightScheduleList;
    
    // constructors
    public FlightSchedulePlan() {
    }

    public FlightSchedulePlan(FlightSchedulePlanStatus status, Flight flight) {
        this.flight = flight; 
        this.status = status;
        this.flightScheduleList = new ArrayList<FlightSchedule>();
        // this.cabinClassList = new ArrayList<CabinClass>();
        this.fares = new ArrayList<Fare>();
    }
    
    // getters and setters

    public long getFlightSchedulePlanGroup() {
        return flightSchedulePlanGroup;
    }

    public void setFlightSchedulePlanGroup(long flightSchedulePlanGroup) {
        this.flightSchedulePlanGroup = flightSchedulePlanGroup;
    }

    public List<Fare> getFares() {
        return fares;
    }

    public void setFares(List<Fare> fares) {
        this.fares = fares;
    }

//    public List<CabinClass> getCabinClassList() {
//        return cabinClassList;
//    }
//
//    public void setCabinClassList(List<CabinClass> cabinClassList) {
//        this.cabinClassList = cabinClassList;
//    }

    public FlightSchedulePlanStatus getStatus() {
        return status;
    }

    public void setStatus(FlightSchedulePlanStatus status) {
        this.status = status;
    }
    
    public List<FlightSchedule> getFlightScheduleList() {
        return flightScheduleList;
    }
    
    public void setFlightScheduleList(List<FlightSchedule> flightScheduleList) {    
        this.flightScheduleList = flightScheduleList;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
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
        if (!(object instanceof FlightSchedulePlan)) {
            return false;
        }
        FlightSchedulePlan other = (FlightSchedulePlan) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightSchedulePlan[ id=" + id + " ]";
    }
    
}
