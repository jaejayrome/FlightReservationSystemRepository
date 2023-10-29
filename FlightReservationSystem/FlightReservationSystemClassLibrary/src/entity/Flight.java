/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
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
import util.enumerations.FlightStatus;

/**
 *
 * @author jeromegoh
 */
@Entity
public class Flight implements Serializable {
    
    // attributes
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String flightNumber; 
    @Column(nullable = false)
    private FlightStatus status;
    
    // relationships
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AircraftConfiguration aircraftConfiguration;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private FlightRoute flightRoute;
    
    @OneToMany(mappedBy = "flight")
    private List<FlightSchedulePlan> flightSchedulePlanList;
    
    
    // constructors
    public Flight() {
    }

    public Flight(String flightNumber, FlightStatus status) {
        this.flightNumber = flightNumber;
        this.status = status;
        this.flightSchedulePlanList = new ArrayList<FlightSchedulePlan>();
    }
    
    // getters and setters

    public List<FlightSchedulePlan> getFlightSchedulePlanList() {
        return flightSchedulePlanList;
    }

    public void setFlightSchedulePlanList(List<FlightSchedulePlan> flightSchedulePlanList) {
        this.flightSchedulePlanList = flightSchedulePlanList;
    }

    public FlightRoute getFlightRoute() {
        return flightRoute;
    }

    public void setFlightRoute(FlightRoute flightRoute) {
        this.flightRoute = flightRoute;
    }
    
    public AircraftConfiguration getAircraftConfiguration() {
        return aircraftConfiguration;
    }

    public void setAircraftConfiguration(AircraftConfiguration aircraftConfiguration) {
        this.aircraftConfiguration = aircraftConfiguration;
    }
    
    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
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
        if (!(object instanceof Flight)) {
            return false;
        }
        Flight other = (Flight) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Flight[ id=" + id + " ]";
    }
    
}
