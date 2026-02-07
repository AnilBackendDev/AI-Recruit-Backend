package com.onboard.service.controller;

import com.onboard.service.entity.Tenant;
import com.onboard.service.record.TenantDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/v1/mock/onboard")
public class MockOnboardController {

    @PostMapping("/tenant")
    public ResponseEntity<TenantDTO> onboardTenant(@RequestBody TenantDTO tenantDTO) {
        // Simulate a delay
        try { Thread.sleep(500); } catch (InterruptedException e) {}

        // Mock response
        TenantDTO mockResponse = new TenantDTO(
                ThreadLocalRandom.current().nextLong(100, 1000),
                tenantDTO.name(),
                tenantDTO.type(),
                tenantDTO.plan() != null ? tenantDTO.plan() : "BASIC",
                Tenant.TenantStatus.ACTIVE,
                tenantDTO.adminEmail(),
                LocalDateTime.now(),
                tenantDTO.locations(),
                tenantDTO.featureIds()
        );
        return ResponseEntity.ok(mockResponse);
    }

    @GetMapping("/tenants")
    public ResponseEntity<List<TenantDTO>> getAllTenants() {
        return ResponseEntity.ok(generateMockTenants(5));
    }
    
    @GetMapping("/tenants/type/{type}")
    public ResponseEntity<List<TenantDTO>> getTenantsByType(@PathVariable String type) {
         return ResponseEntity.ok(generateMockTenants(3));
    }

    private List<TenantDTO> generateMockTenants(int count) {
        List<TenantDTO> tenants = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            tenants.add(new TenantDTO(
                    (long) (i + 1),
                    "Mock Tenant " + (i + 1),
                    i % 2 == 0 ? Tenant.TenantType.COMPANY : Tenant.TenantType.VENDOR,
                    "ENTERPRISE",
                    Tenant.TenantStatus.ACTIVE,
                    "admin" + (i + 1) + "@example.com",
                    LocalDateTime.now().minusDays(i),
                    List.of(new TenantDTO.TenantLocationDTO(1L, null)),
                    List.of(101L, 102L)
            ));
        }
        return tenants;
    }
}
