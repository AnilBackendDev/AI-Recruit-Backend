package com.candidate.service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "candidate_education")
public class CandidateEducation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @Column(length = 100)
    private String degree;

    @Column(length = 150)
    private String institution;

    @Column(name = "graduation_year")
    private Integer graduationYear;

    public CandidateEducation() {
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

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }

    // Manual Builder
    public static class CandidateEducationBuilder {
        private CandidateEducation edu = new CandidateEducation();

        public CandidateEducationBuilder id(Long id) {
            edu.setId(id);
            return this;
        }

        public CandidateEducationBuilder candidate(Candidate candidate) {
            edu.setCandidate(candidate);
            return this;
        }

        public CandidateEducationBuilder degree(String degree) {
            edu.setDegree(degree);
            return this;
        }

        public CandidateEducationBuilder institution(String institution) {
            edu.setInstitution(institution);
            return this;
        }

        public CandidateEducationBuilder graduationYear(Integer graduationYear) {
            edu.setGraduationYear(graduationYear);
            return this;
        }

        public CandidateEducation build() {
            return edu;
        }
    }

    public static CandidateEducationBuilder builder() {
        return new CandidateEducationBuilder();
    }
}
