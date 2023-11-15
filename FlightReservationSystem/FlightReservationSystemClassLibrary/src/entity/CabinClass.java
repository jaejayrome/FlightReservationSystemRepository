/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import util.enumerations.CabinClassType;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jeromegoh
 */
@Entity
public class CabinClass implements Serializable {
    
    // attributes 
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(nullable = false)
    @NotNull
    private CabinClassType cabinClassName;
    
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private BigDecimal numAisles;
    
    @Column(nullable = false)
    @NotNull
    @Min(1)
    private BigDecimal numRows;
    
    @Column(nullable = false)
    @NotNull
    @Min(1)
    private BigDecimal numSeatsAbreast;
    
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String seatingConfiguration;

    
    // relationships
    @ManyToMany(mappedBy= "cabinClassList", cascade = CascadeType.PERSIST)
    private List<AircraftConfiguration> aircraftConfigurationList;
    

    // constructors
    public CabinClass() {
    }
    
    public CabinClass(CabinClassType cabinClassName, BigDecimal numAisles, BigDecimal numRows, BigDecimal numSeatsAbreast, String seatingConfiguration) {
        this.cabinClassName = cabinClassName;
        this.numAisles = numAisles;
        this.numRows = numRows;
        this.numSeatsAbreast = numSeatsAbreast;
        this.seatingConfiguration = seatingConfiguration;
        this.aircraftConfigurationList = new ArrayList<AircraftConfiguration>();
    }
    
     // getters and settters
    
    public List<AircraftConfiguration> getAircraftConfigurationList() {
        return aircraftConfigurationList;
    }
    
    public void setAircraftConfigurationList(List<AircraftConfiguration> aircraftConfigurationList) {
        this.aircraftConfigurationList = aircraftConfigurationList;
    }

    public CabinClassType getCabinClassName() {
        return cabinClassName;
    }

    public void setCabinClassName(CabinClassType cabinClassName) {
        this.cabinClassName = cabinClassName;
    }

    public BigDecimal getNumAisles() {
        return numAisles;
    }

    public void setNumAisles(BigDecimal numAisles) {
        this.numAisles = numAisles;
    }

    public BigDecimal getNumRows() {
        return numRows;
    }

    public void setNumRows(BigDecimal numRows) {
        this.numRows = numRows;
    }

    public BigDecimal getNumSeatsAbreast() {
        return numSeatsAbreast;
    }

    public void setNumSeatsAbreast(BigDecimal numSeatsAbreast) {
        this.numSeatsAbreast = numSeatsAbreast;
    }

    public String getSeatingConfiguration() {
        return seatingConfiguration;
    }

    public void setSeatingConfiguration(String seatingConfiguration) {
        this.seatingConfiguration = seatingConfiguration;
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
        if (!(object instanceof CabinClass)) {
            return false;
        }
        CabinClass other = (CabinClass) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CabinClass[ id=" + id + " ]";
    }
    
}
