package com.onboard.service.mapper;

import com.onboard.service.entity.Tenant;
import com.onboard.service.entity.TenantCountry;
import com.onboard.service.entity.TenantFeature;
import com.onboard.service.record.TenantDTO;

import java.util.stream.Collectors;

public class TenantMapper {

    public static TenantDTO toDTO(Tenant tenant) {
        if (tenant == null) return null;
        
        return new TenantDTO(
            tenant.getId(),
            tenant.getName(),
            tenant.getType(),
            tenant.getPlan(),
            tenant.getStatus(),
            tenant.getAdminEmail(),
            tenant.getCreatedAt(),
            tenant.getTenantCountries().stream()
                .map(loc -> new TenantDTO.TenantLocationDTO(loc.getCountryId(), loc.getStateId()))
                .collect(Collectors.toList()),
            tenant.getTenantFeatures().stream()
                .map(TenantFeature::getFeatureId)
                .collect(Collectors.toList())
        );
    }

    public static Tenant toEntity(TenantDTO dto) {
        if (dto == null) return null;
        Tenant tenant = new Tenant();
        tenant.setId(dto.id());
        tenant.setName(dto.name());
        tenant.setType(dto.type());
        tenant.setPlan(dto.plan());
        tenant.setStatus(dto.status() != null ? dto.status() : Tenant.TenantStatus.ACTIVE);
        tenant.setAdminEmail(dto.adminEmail());
        
        if (dto.locations() != null) {
            tenant.setTenantCountries(dto.locations().stream()
                .map(loc -> new TenantCountry(tenant, loc.countryId(), loc.stateId()))
                .collect(Collectors.toList()));
        }

        if (dto.featureIds() != null) {
            tenant.setTenantFeatures(dto.featureIds().stream()
                .map(featureId -> new TenantFeature(tenant, featureId, true))
                .collect(Collectors.toList()));
        }
        
        return tenant;
    }
}
