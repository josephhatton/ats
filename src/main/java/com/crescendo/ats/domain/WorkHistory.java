package com.crescendo.ats.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A WorkHistory.
 */
@Entity
@Table(name = "work_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "workhistory")
public class WorkHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "company")
    private String company;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "starting_compensation")
    private Double startingCompensation;
    
    @Column(name = "ending_compensation")
    private Double endingCompensation;
    
    @Column(name = "compensation_type")
    private Integer compensationType;
    
    @Column(name = "supervisor")
    private String supervisor;
    
    @Column(name = "supervisor_title")
    private String supervisorTitle;
    
    @Column(name = "supervisor_phone")
    private String supervisorPhone;
    
    @Column(name = "duties")
    private String duties;
    
    @Column(name = "reason_for_leaving")
    private String reasonForLeaving;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }
    
    public void setCompany(String company) {
        this.company = company;
    }

    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getStartingCompensation() {
        return startingCompensation;
    }
    
    public void setStartingCompensation(Double startingCompensation) {
        this.startingCompensation = startingCompensation;
    }

    public Double getEndingCompensation() {
        return endingCompensation;
    }
    
    public void setEndingCompensation(Double endingCompensation) {
        this.endingCompensation = endingCompensation;
    }

    public Integer getCompensationType() {
        return compensationType;
    }
    
    public void setCompensationType(Integer compensationType) {
        this.compensationType = compensationType;
    }

    public String getSupervisor() {
        return supervisor;
    }
    
    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getSupervisorTitle() {
        return supervisorTitle;
    }
    
    public void setSupervisorTitle(String supervisorTitle) {
        this.supervisorTitle = supervisorTitle;
    }

    public String getSupervisorPhone() {
        return supervisorPhone;
    }
    
    public void setSupervisorPhone(String supervisorPhone) {
        this.supervisorPhone = supervisorPhone;
    }

    public String getDuties() {
        return duties;
    }
    
    public void setDuties(String duties) {
        this.duties = duties;
    }

    public String getReasonForLeaving() {
        return reasonForLeaving;
    }
    
    public void setReasonForLeaving(String reasonForLeaving) {
        this.reasonForLeaving = reasonForLeaving;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkHistory workHistory = (WorkHistory) o;
        if(workHistory.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, workHistory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WorkHistory{" +
            "id=" + id +
            ", company='" + company + "'" +
            ", title='" + title + "'" +
            ", startDate='" + startDate + "'" +
            ", endDate='" + endDate + "'" +
            ", startingCompensation='" + startingCompensation + "'" +
            ", endingCompensation='" + endingCompensation + "'" +
            ", compensationType='" + compensationType + "'" +
            ", supervisor='" + supervisor + "'" +
            ", supervisorTitle='" + supervisorTitle + "'" +
            ", supervisorPhone='" + supervisorPhone + "'" +
            ", duties='" + duties + "'" +
            ", reasonForLeaving='" + reasonForLeaving + "'" +
            '}';
    }
}
