package com.onboard.service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tenant")
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TenantType type;

    @Column(length = 50)
    private String plan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TenantStatus status = TenantStatus.ACTIVE;

    @Column(name = "admin_email", length = 150)
    private String adminEmail;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<TenantCountry> tenantCountries = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<TenantFeature> tenantFeatures = new java.util.ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum TenantType {
        COMPANY, VENDOR
    }

    public enum TenantStatus {
        ACTIVE, INACTIVE
    }

    public Tenant() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public TenantType getType() { return type; }
    public void setType(TenantType type) { this.type = type; }
    public String getPlan() { return plan; }
    public void setPlan(String plan) { this.plan = plan; }
    public TenantStatus getStatus() { return status; }
    public void setStatus(TenantStatus status) { this.status = status; }
    public String getAdminEmail() { return adminEmail; }
    public void setAdminEmail(String adminEmail) { this.adminEmail = adminEmail; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public java.util.List<TenantCountry> getTenantCountries() { return tenantCountries; }
    public void setTenantCountries(java.util.List<TenantCountry> tenantCountries) { this.tenantCountries = tenantCountries; }

    public java.util.List<TenantFeature> getTenantFeatures() { return tenantFeatures; }
    public void setTenantFeatures(java.util.List<TenantFeature> tenantFeatures) { this.tenantFeatures = tenantFeatures; }
}
