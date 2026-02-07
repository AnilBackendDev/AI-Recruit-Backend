package com.candidate.service.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "candidate")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "country_id", nullable = false)
    private Long countryId;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(length = 255)
    private String linkedin;

    @Column(length = 255)
    private String website;

    @Column(unique = true, nullable = false, length = 150)
    private String email;

    @Column(length = 20)
    private String mobile;

    @Column(name = "total_experience")
    private BigDecimal totalExperience;

    @Column(name = "current_ctc")
    private BigDecimal currentCtc;

    @Column(name = "expected_ctc")
    private BigDecimal expectedCtc;

    @Column(length = 100)
    private String location;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CandidateEducation> educations = new ArrayList<>();

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CandidateExperience> experiences = new ArrayList<>();

    public Candidate() {
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public BigDecimal getTotalExperience() {
        return totalExperience;
    }

    public void setTotalExperience(BigDecimal totalExperience) {
        this.totalExperience = totalExperience;
    }

    public BigDecimal getCurrentCtc() {
        return currentCtc;
    }

    public void setCurrentCtc(BigDecimal currentCtc) {
        this.currentCtc = currentCtc;
    }

    public BigDecimal getExpectedCtc() {
        return expectedCtc;
    }

    public void setExpectedCtc(BigDecimal expectedCtc) {
        this.expectedCtc = expectedCtc;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<CandidateEducation> getEducations() {
        return educations;
    }

    public void setEducations(List<CandidateEducation> educations) {
        this.educations = educations;
    }

    public List<CandidateExperience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<CandidateExperience> experiences) {
        this.experiences = experiences;
    }

    // Manual Builder
    public static class CandidateBuilder {
        private Candidate candidate = new Candidate();

        public CandidateBuilder id(Long id) {
            candidate.setId(id);
            return this;
        }

        public CandidateBuilder tenantId(Long tenantId) {
            candidate.setTenantId(tenantId);
            return this;
        }

        public CandidateBuilder countryId(Long countryId) {
            candidate.setCountryId(countryId);
            return this;
        }

        public CandidateBuilder firstName(String firstName) {
            candidate.setFirstName(firstName);
            return this;
        }

        public CandidateBuilder lastName(String lastName) {
            candidate.setLastName(lastName);
            return this;
        }

        public CandidateBuilder email(String email) {
            candidate.setEmail(email);
            return this;
        }

        public CandidateBuilder mobile(String mobile) {
            candidate.setMobile(mobile);
            return this;
        }

        public CandidateBuilder summary(String summary) {
            candidate.setSummary(summary);
            return this;
        }

        public CandidateBuilder linkedin(String linkedin) {
            candidate.setLinkedin(linkedin);
            return this;
        }

        public CandidateBuilder website(String website) {
            candidate.setWebsite(website);
            return this;
        }

        public CandidateBuilder totalExperience(BigDecimal totalExperience) {
            candidate.setTotalExperience(totalExperience);
            return this;
        }

        public CandidateBuilder currentCtc(BigDecimal currentCtc) {
            candidate.setCurrentCtc(currentCtc);
            return this;
        }

        public CandidateBuilder expectedCtc(BigDecimal expectedCtc) {
            candidate.setExpectedCtc(expectedCtc);
            return this;
        }

        public CandidateBuilder location(String location) {
            candidate.setLocation(location);
            return this;
        }

        public Candidate build() {
            return candidate;
        }
    }

    public static CandidateBuilder builder() {
        return new CandidateBuilder();
    }
}
