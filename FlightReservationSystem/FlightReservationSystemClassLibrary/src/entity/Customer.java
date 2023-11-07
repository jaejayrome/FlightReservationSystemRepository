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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumerations.RoleType;

/**
 *
 * @author jeromegoh
 */
@Entity
public class Customer implements Serializable {

    // attributes
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String firstName;
    
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)       
    private String lastName;

    @Column (nullable = false)        
    private String email;
    
    @Column (nullable = false)
    private String phoneNumber;
    
    @Column (nullable = false)
    private String address;
    
    @Column (nullable = false)
    private String password;
    
    // relationships
    @OneToMany (mappedBy = "customer")
    private List<FlightReservation> flightReservationList;
    
    @Column (nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
    
    // constructors

    public Customer() {
    }
    
    public Customer(Customer customer) {
        this.firstName = customer.firstName;
        this.lastName = customer.lastName;
        this.email = customer.email;
        this.phoneNumber = customer.phoneNumber;
        this.address = customer.address;
        this.password = customer.password;
        this.flightReservationList = customer.flightReservationList;
    }


    public Customer(String firstName, 
            String lastName, String email, String phoneNumber, String address, 
            String password, RoleType roleType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.password = password;
        this.flightReservationList = new ArrayList<FlightReservation>();
        this.roleType = roleType;
    }
    
    public Customer(String firstName, 
            String lastName, String email, String phoneNumber, String address, 
            String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.password = password;
        this.flightReservationList = new ArrayList<FlightReservation>();
        this.roleType = roleType.VISITOR;
    }
    
    // getters and setters 

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<FlightReservation> getFlightReservationList() {
        return flightReservationList;
    }

    public void setFlightReservationList(List<FlightReservation> flightReservationList) {
        this.flightReservationList = flightReservationList;
    }
    
    public List<FlightReservation> addFlightReservationList(FlightReservation fr) {
        this.flightReservationList.add(fr);
        return this.flightReservationList;
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
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Person[ id=" + id + " ]";
    }
    
}
