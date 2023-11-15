/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import util.enumerations.AircraftTypeName;

/**
 *
 * @author jeromegoh
 */
@Cacheable(true)
@Entity
public class AircraftType implements Serializable {
    
    // attributes
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 64)
    @NotNull
    private AircraftTypeName aircraftTypeName;

    @Column(nullable = false, length = 64)
    @NotNull
    @Min(1)
    private BigDecimal passengerSeatCapacity;
    
    // relationships
    // owned entity specify the mapping
    @OneToMany (mappedBy = "aircraftType", cascade = CascadeType.PERSIST)
    private List<AircraftConfiguration> aircraftConfigurations; 
    
    // constructors
    public AircraftType() {
    }

    public AircraftType(AircraftTypeName aircraftTypeName, BigDecimal passengerSeatCapacity) {
        this.aircraftTypeName = aircraftTypeName;
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

    public AircraftTypeName getAircraftTypeName() {
        return aircraftTypeName;
    }

    public void setAircraftTypeName(AircraftTypeName aircraftTypeName) {
        this.aircraftTypeName = aircraftTypeName;
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
