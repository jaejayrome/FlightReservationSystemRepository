/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import util.enumerations.FlightSchedulePlanStatus;

/**
 *
 * @author jeromegoh
 */
@Entity
public class RecurrentWeeklyFlightSchedulePlan extends FlightSchedulePlan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column (nullable = false)
    private BigDecimal frequency;
    
    @Column (nullable = false)
    private Date endDate;

    public RecurrentWeeklyFlightSchedulePlan() {
        super();
    }

    public RecurrentWeeklyFlightSchedulePlan(FlightSchedulePlanStatus status, Date endDate) {
        super(status);
        this.endDate = endDate;
        this.frequency = new BigDecimal(7);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getFrequency() {
        return frequency;
    }

    public void setFrequency(BigDecimal frequency) {
        this.frequency = frequency;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
        if (!(object instanceof RecurrentWeeklyFlightSchedulePlan)) {
            return false;
        }
        RecurrentWeeklyFlightSchedulePlan other = (RecurrentWeeklyFlightSchedulePlan) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RecurrentWeeklyFlightSchedulePlan[ id=" + id + " ]";
    }
    
}
