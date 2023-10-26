/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 *
 * @author jeromegoh
 */
@Entity
public class AircraftConfiguration implements Serializable {
    
    // attributes
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String configurationName; 
    
    @Column(nullable = false)
    private BigDecimal numCabinClass;
    
    // relationships
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AircraftType aircraftType;
    
    @ManyToMany
    private List<CabinClass> cabinClassList;
    
    @OneToMany (mappedBy = "aircraftConfiguration")
    private List<Flight> flightList; 
    
    // constructors
    public AircraftConfiguration() {
    }

    public AircraftConfiguration(String configurationName, AircraftType aircraftType, BigDecimal numCabinClass) {
        this.configurationName = configurationName;
        this.aircraftType = aircraftType;
        this.numCabinClass = numCabinClass;
        this.cabinClassList = new ArrayList<CabinClass>();
        this.flightList = new ArrayList<Flight>();
    }

    public BigDecimal getNumCabinClass() {
        return numCabinClass;
    }

    public void setNumCabinClass(BigDecimal numCabinClass) {
        this.numCabinClass = numCabinClass;
    }

    public List<Flight> getFlightList() {
        return flightList;
    }

    public void setFlightList(List<Flight> flightList) {
        this.flightList = flightList;
    }

    public List<CabinClass> getCabinClassList() {
        return cabinClassList;
    }

    public void setCabinClassList(List<CabinClass> cabinClassList) {
        this.cabinClassList = cabinClassList;
    }
    
    public String getConfigurationName() {
        return configurationName;
    }

    public void setConfigurationName(String configurationName) {
        this.configurationName = configurationName;
    }

    public AircraftType getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(AircraftType aircraftType) {
        this.aircraftType = aircraftType;
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
        if (!(object instanceof AircraftConfiguration)) {
            return false;
        }
        AircraftConfiguration other = (AircraftConfiguration) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AircraftConfiguration[ id=" + id + " ]";
    }
    
}
