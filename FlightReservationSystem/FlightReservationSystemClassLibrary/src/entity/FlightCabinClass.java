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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jeromegoh
 */
@Entity
public class FlightCabinClass implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    @NotNull
    private BigDecimal numAvailableSeats;
    
    @Column(nullable = false)
    @NotNull
    private BigDecimal numReservedSeats;
    
    @Column(nullable = false)
    @NotNull
    private BigDecimal numBalanceSeats;
    
    
    // relationships 
    @OneToOne
    private CabinClass cabinClass;
    
    @OneToMany(mappedBy = "flightCabinClass")
    private List<Seat> seatList; 
    
    @ManyToOne(optional=false)
    @JoinColumn(nullable=false)
    private FlightSchedule flightSchedule;
    
    public FlightCabinClass() {
    }

    public FlightCabinClass(BigDecimal numAvailableSeats, BigDecimal numReservedSeats, BigDecimal numBalanceSeats, FlightSchedule flightSchedule) {
        this.numAvailableSeats = numAvailableSeats;
        this.numReservedSeats = numReservedSeats;
        this.numBalanceSeats = numBalanceSeats;
        this.seatList = new ArrayList<Seat>();
        this.flightSchedule = flightSchedule;
    }

    public FlightSchedule getFlightSchedule() {
        return flightSchedule;
    }

    public void setFlightSchedule(FlightSchedule flightSchedule) {
        this.flightSchedule = flightSchedule;
    }
    
    public List<Seat> getSeatList() {
        return seatList;
    }

    public void setSeatList(List<Seat> seatList) {
        this.seatList = seatList;
    }

    public CabinClass getCabinClass() {
        return cabinClass;
    }

    public void setCabinClass(CabinClass cabinClass) {
        this.cabinClass = cabinClass;
    }
    
     public BigDecimal getNumAvailableSeats() {
        return numAvailableSeats;
    }

    public void setNumAvailableSeats(BigDecimal numAvailableSeats) {
        this.numAvailableSeats = numAvailableSeats;
    }

    public BigDecimal getNumReservedSeats() {
        return numReservedSeats;
    }

    public void setNumReservedSeats(BigDecimal numReservedSeats) {
        this.numReservedSeats = numReservedSeats;
    }

    public BigDecimal getNumBalanceSeats() {
        return numBalanceSeats;
    }

    public void setNumBalanceSeats(BigDecimal numBalanceSeats) {
        this.numBalanceSeats = numBalanceSeats;
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
        if (!(object instanceof FlightCabinClass)) {
            return false;
        }
        FlightCabinClass other = (FlightCabinClass) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightCabinClass[ id=" + id + " ]";
    }
    
}
