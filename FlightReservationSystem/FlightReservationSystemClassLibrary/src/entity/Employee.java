/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumerations.EmploymentType;
import util.enumerations.GenderType;
import util.enumerations.JobTitle;

/**
 *
 * @author jeromegoh
 */
//@Cacheable(true)
@Entity
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // bean validation
    // database validation 
//    @Column(nullable = false, length = 64)
    @Column (nullable = false, length = 64)
    @Size(max = 64)
    private String firstName; 
    
    @Column(nullable=true, length = 64)
//    @NotNull
//    @Size(min = 1, max = 64)
    private String lastName;
    
    //@Enumerated(EnumType.STRING)
    @Column(nullable = false)
//    @NotNull
    private GenderType gender;
    
    @Column(nullable = false)
    @Size(min = 1, max = 64)
    @NotNull
    private String email; 
    
    @Column (nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String phoneNumber;
    
    // @Enumerated(EnumType.STRING)
    @Column (nullable = false, length = 64)
    @NotNull
//    @Column(nullable = false)
    private JobTitle jobTitle;
    
    // @Enumerated(EnumType.STRING)
//    @NotNull
//    @Column(nullable = false)
    @Column (nullable = false, length = 64)
    @NotNull
    private EmploymentType employmentType;
    
//    @Column(nullable = false)
    
    @Column (nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String loginUsername;
    
    @Size(min = 1, max = 64)
    @NotNull
    @Column(nullable = false)
    private String loginPassword;
    
    @Column(nullable = false)
    private boolean isLoggedIn;
    
    public Employee() {
    }
    
    public Employee(String firstName, String lastName, GenderType gender, String email, 
        String phoneNumber, JobTitle jobTitle, EmploymentType employmentType, 
        String loginUsername, String loginPassword) {
        
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.jobTitle = jobTitle;
        this.employmentType = employmentType;
        this.loginUsername = loginUsername;
        this.loginPassword = loginPassword;
    }

    public boolean isIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
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

    public GenderType getGender() {
        return gender;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
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

    public JobTitle getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(JobTitle jobTitle) {
        this.jobTitle = jobTitle;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public String getLoginUsername() {
        return loginUsername;
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
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
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Employee[ id=" + id + " ]";
    }
    
}
