package com.crescendo.ats.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A HiringContact.
 */
@Entity
@Table(name = "hiring_contact")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "hiringcontact")
public class HiringContact implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @Column(name = "nick_name")
    private String nickName;
    
    @Column(name = "phone1")
    private String phone1;
    
    @Column(name = "phone2")
    private String phone2;
    
    @Column(name = "email1")
    private String email1;
    
    @Column(name = "job_title")
    private String jobTitle;
    
    @Column(name = "referral_source")
    private String referralSource;
    
    @Column(name = "contact_type")
    private String contactType;
    
    @Column(name = "email2")
    private String email2;
    
    @Column(name = "middle_name")
    private String middleName;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getNickName() {
        return nickName;
    }
    
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone1() {
        return phone1;
    }
    
    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }
    
    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getEmail1() {
        return email1;
    }
    
    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getJobTitle() {
        return jobTitle;
    }
    
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getReferralSource() {
        return referralSource;
    }
    
    public void setReferralSource(String referralSource) {
        this.referralSource = referralSource;
    }

    public String getContactType() {
        return contactType;
    }
    
    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getEmail2() {
        return email2;
    }
    
    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getMiddleName() {
        return middleName;
    }
    
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HiringContact hiringContact = (HiringContact) o;
        if(hiringContact.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, hiringContact.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HiringContact{" +
            "id=" + id +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            ", nickName='" + nickName + "'" +
            ", phone1='" + phone1 + "'" +
            ", phone2='" + phone2 + "'" +
            ", email1='" + email1 + "'" +
            ", jobTitle='" + jobTitle + "'" +
            ", referralSource='" + referralSource + "'" +
            ", contactType='" + contactType + "'" +
            ", email2='" + email2 + "'" +
            ", middleName='" + middleName + "'" +
            '}';
    }
}
