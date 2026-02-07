package com.onboard.service.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tenant_feature")
public class TenantFeature {

    @EmbeddedId
    private TenantFeatureId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tenantId")
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @Column(nullable = false)
    private Boolean enabled = true;

    public TenantFeature() {}

    public TenantFeature(Tenant tenant, Long featureId, Boolean enabled) {
        this.tenant = tenant;
        this.id = new TenantFeatureId(tenant.getId(), featureId);
        this.enabled = enabled;
    }

    public TenantFeatureId getId() {
        return id;
    }

    public void setId(TenantFeatureId id) {
        this.id = id;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Long getFeatureId() {
        return id != null ? id.getFeatureId() : null;
    }

    public void setFeatureId(Long featureId) {
        if (this.id == null) this.id = new TenantFeatureId();
        this.id.setFeatureId(featureId);
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Embeddable
    public static class TenantFeatureId implements Serializable {
        @Column(name = "tenant_id")
        private Long tenantId;

        @Column(name = "feature_id")
        private Long featureId;

        public TenantFeatureId() {}

        public TenantFeatureId(Long tenantId, Long featureId) {
            this.tenantId = tenantId;
            this.featureId = featureId;
        }

        public Long getTenantId() {
            return tenantId;
        }

        public void setTenantId(Long tenantId) {
            this.tenantId = tenantId;
        }

        public Long getFeatureId() {
            return featureId;
        }

        public void setFeatureId(Long featureId) {
            this.featureId = featureId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TenantFeatureId that = (TenantFeatureId) o;
            return Objects.equals(tenantId, that.tenantId) && Objects.equals(featureId, that.featureId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(tenantId, featureId);
        }
    }
}
