package com.candidate.service.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "candidate_experience")
public class CandidateExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @Column(name = "company_name", length = 150)
    private String companyName;

    @Column(length = 100)
    private String designation;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;

    public CandidateExperience() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    // Manual Builder
    public static class CandidateExperienceBuilder {
        private CandidateExperience exp = new CandidateExperience();

        public CandidateExperienceBuilder id(Long id) {
            exp.setId(id);
            return this;
        }

        public CandidateExperienceBuilder candidate(Candidate candidate) {
            exp.setCandidate(candidate);
            return this;
        }

        public CandidateExperienceBuilder companyName(String companyName) {
            exp.setCompanyName(companyName);
            return this;
        }

        public CandidateExperienceBuilder designation(String designation) {
            exp.setDesignation(designation);
            return this;
        }

        public CandidateExperienceBuilder fromDate(LocalDate fromDate) {
            exp.setFromDate(fromDate);
            return this;
        }

        public CandidateExperienceBuilder toDate(LocalDate toDate) {
            exp.setToDate(toDate);
            return this;
        }

        public CandidateExperience build() {
            return exp;
        }
    }

    public static CandidateExperienceBuilder builder() {
        return new CandidateExperienceBuilder();
    }
}
