/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

/**
 *
 * @author jeromegoh
 */
@Entity
public class Fare implements Serializable {
    
    // attributes
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
   @Column(nullable = false)
   private String fareBasicCode;
   @Column(nullable = false)
   private BigDecimal fareAmount;
   
   // relationships
   @ManyToOne(optional = false)
   @JoinColumn(nullable = false)
   private CabinClass cabinClass;
   
   @ManyToOne(optional = false)
   @JoinColumn(nullable = false)
   private FlightSchedulePlan flightSchedulePlan;
   
   // constructors
    public Fare() {
    }

    public Fare(String fareBasicCode, BigDecimal fareAmount, CabinClass cabinClass, FlightSchedulePlan flightSchedulePlan) {
        this.fareBasicCode = fareBasicCode;
        this.fareAmount = fareAmount;
        this.cabinClass = cabinClass;
        this.flightSchedulePlan = flightSchedulePlan;
    }
    
    // getters and setters

    public FlightSchedulePlan getFlightSchedulePlan() {
        return flightSchedulePlan;
    }

    public void setFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        this.flightSchedulePlan = flightSchedulePlan;
    }
    
    public CabinClass getCabinClass() {    
        return cabinClass;
    }

    public void setCabinClass(CabinClass cabinClass) {
        this.cabinClass = cabinClass;
    }

    public String getFareBasicCode() {
        return fareBasicCode;
    }

    public void setFareBasicCode(String fareBasicCode) {
        this.fareBasicCode = fareBasicCode;
    }

    public BigDecimal getFareAmount() {
        return fareAmount;
    }

    public void setFareAmount(BigDecimal fareAmount) {
        this.fareAmount = fareAmount;
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
        if (!(object instanceof Fare)) {
            return false;
        }
        Fare other = (Fare) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Fare[ id=" + id + " ]";
    }
    
}
