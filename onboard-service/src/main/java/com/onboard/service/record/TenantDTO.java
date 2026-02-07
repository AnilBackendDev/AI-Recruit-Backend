package com.onboard.service.record;

import com.onboard.service.entity.Tenant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public record TenantDTO(
    Long id,
    
    @NotBlank(message = "Tenant name is mandatory")
    String name,
    
    @NotNull(message = "Tenant type is mandatory (COMPANY or VENDOR)")
    Tenant.TenantType type,
    
    String plan,
    
    Tenant.TenantStatus status,

    @NotBlank(message = "Admin email is mandatory")
    @Email(message = "Invalid admin email format")
    String adminEmail,
    
    LocalDateTime createdAt,
    
    @NotEmpty(message = "At least one location is mandatory")
    List<TenantLocationDTO> locations,
    
    List<Long> featureIds
) {
    public record TenantLocationDTO(
        @NotNull(message = "Country ID is mandatory")
        Long countryId,
        
        Long stateId
    ) {}
}
