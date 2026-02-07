package com.onboard.service.service;

import com.onboard.service.record.TenantDTO;
import java.util.List;

public interface OnboardService {
    TenantDTO onboardTenant(TenantDTO tenantDTO);
    List<TenantDTO> getAllTenants();
    List<TenantDTO> getTenantsByType(String type);
    TenantDTO getTenantById(Long id);
    TenantDTO updateTenantStatus(Long id, String status);
}
