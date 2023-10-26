/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import util.enumerations.ScheduleType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import util.enumerations.FlightSchedulePlanStatus;

/**
 *
 * @author jeromegoh
 */
@Entity
public class FlightSchedulePlan implements Serializable {
    
    // attributes
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private ScheduleType scheduleType;
    
    @Column(nullable = false)
    private FlightSchedulePlanStatus status;
    
    @Column(nullable = false)
    private String flightNumber;
    
    // relationships
    // relationship is optional as a FSP don't need a F
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Flight flight;
    
    @OneToMany(mappedBy = "flightSchedulePlan")
    private List<CabinClass> cabinClassList;
    
    @OneToMany(mappedBy= "flightSchedulePlan")
    private List<FlightSchedule> flightScheduleList;
    
    // constructors
    public FlightSchedulePlan() {
    }

    public FlightSchedulePlan(ScheduleType scheduleType, Flight flight, FlightSchedulePlanStatus status, String flightNumber) {
        this.scheduleType = scheduleType;
        this.flight = flight; 
        this.status = status;
        this.flightNumber = flightNumber;
        this.cabinClassList = new ArrayList<CabinClass>();
        this.flightScheduleList = new ArrayList<FlightSchedule>();
    }
    
    // getters and setters

    public FlightSchedulePlanStatus getStatus() {
        return status;
    }

    public void setStatus(FlightSchedulePlanStatus status) {
        this.status = status;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
    
    

    public List<CabinClass> getCabinClassList() {
        return cabinClassList;
    }

    public void setCabinClassList(List<CabinClass> cabinClassList) {
        this.cabinClassList = cabinClassList;
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

    public ScheduleType getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(ScheduleType scheduleType) {
        this.scheduleType = scheduleType;
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
