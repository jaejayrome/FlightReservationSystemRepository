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
import javax.persistence.OneToMany;

/**
 *
 * @author jeromegoh
 */
@Entity
public class AircraftType implements Serializable {
    
    // attributes
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String aircraftTypeName;
    @Column(nullable = false)
    private String manufacturer;
    @Column(nullable = false)
    private BigDecimal passengerSeatCapacity;
    
    // relationships
    // owned entity specify the mapping
    @OneToMany (mappedBy = "aircraftType") 
    private List<AircraftConfiguration> aircraftConfigurations; 
    
    // constructors
    public AircraftType() {
    }

    public AircraftType(String aircraftTypeName, String manufacturer, BigDecimal passengerSeatCapacity) {
        this.aircraftTypeName = aircraftTypeName;
        this.manufacturer = manufacturer;
        this.passengerSeatCapacity = passengerSeatCapacity;
        this.aircraftConfigurations = new ArrayList<AircraftConfiguration>();
    }
    
    // getters and setters
    public List<AircraftConfiguration> getAircraftConfigurations() {
        return aircraftConfigurations;
    }

    public void setAircraftConfigurations(List<AircraftConfiguration> aircraftConfigurations) {
        this.aircraftConfigurations = aircraftConfigurations;
    }

    public String getAircraftTypeName() {
        return aircraftTypeName;
    }

    public void setAircraftTypeName(String aircraftTypeName) {
        this.aircraftTypeName = aircraftTypeName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public BigDecimal getPassengerSeatCapacity() {
        return passengerSeatCapacity;
    }

    public void setPassengerSeatCapacity(BigDecimal passengerSeatCapacity) {
        this.passengerSeatCapacity = passengerSeatCapacity;
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
        if (!(object instanceof AircraftType)) {
            return false;
        }
        AircraftType other = (AircraftType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AircraftType[ id=" + id + " ]";
    }
    
}
