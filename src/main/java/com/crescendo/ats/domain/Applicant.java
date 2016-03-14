package com.crescendo.ats.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Applicant.
 */
@Entity
@Table(name = "applicant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "applicant")
public class Applicant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "middle_name")
    private String middleName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "email1")
    private String email1;
    
    @Column(name = "home_phone")
    private String homePhone;
    
    @Column(name = "cell_phone")
    private String cellPhone;
    
    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Integer isDeleted;
    
    @Column(name = "nick_name")
    private String nickName;
    
    @Column(name = "work_phone")
    private String workPhone;
    
    @Column(name = "email2")
    private String email2;
    
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

    public String getMiddleName() {
        return middleName;
    }
    
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail1() {
        return email1;
    }
    
    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getHomePhone() {
        return homePhone;
    }
    
    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getCellPhone() {
        return cellPhone;
    }
    
    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }
    
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getNickName() {
        return nickName;
    }
    
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getWorkPhone() {
        return workPhone;
    }
    
    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public String getEmail2() {
        return email2;
    }
    
    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Applicant applicant = (Applicant) o;
        if(applicant.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, applicant.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Applicant{" +
            "id=" + id +
            ", firstName='" + firstName + "'" +
            ", middleName='" + middleName + "'" +
            ", lastName='" + lastName + "'" +
            ", title='" + title + "'" +
            ", email1='" + email1 + "'" +
            ", homePhone='" + homePhone + "'" +
            ", cellPhone='" + cellPhone + "'" +
            ", isDeleted='" + isDeleted + "'" +
            ", nickName='" + nickName + "'" +
            ", workPhone='" + workPhone + "'" +
            ", email2='" + email2 + "'" +
            '}';
    }
}
