/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jeromegoh
 */
@Entity
public class Airport implements Serializable {
    
    // attributes
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    @NotNull
    private String airportName;
    @Column(nullable = false, unique = true)
    private String iataAirportCode;
    @Column(nullable = false)
    @NotNull
    private String city; 
    @Column(nullable = false)
    @NotNull
    private String state; 
    @Column(nullable = false)
    @NotNull
    private String country;
    
    // relationships
    
    // constructor
    public Airport() {
    }
    
    public Airport(String airportName, String iataAirportCode, String city, String state, String country) {
        this.airportName = airportName;
        this.iataAirportCode = iataAirportCode;
        this.city = city;
        this.state = state;
        this.country = country;
    }
    
    // getters and setters
    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getIataAirportCode() {
        return iataAirportCode;
    }

    public void setIataAirportCode(String iataAirportCode) {
        this.iataAirportCode = iataAirportCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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
        if (!(object instanceof Airport)) {
            return false;
        }
        Airport other = (Airport) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Airport[ id=" + id + " ]";
    }
    
}
