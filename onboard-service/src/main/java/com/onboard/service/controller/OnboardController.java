package com.onboard.service.controller;

import com.onboard.service.record.TenantDTO;
import com.onboard.service.service.OnboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/onboard")
public class OnboardController {

    private final OnboardService onboardService;

    public OnboardController(OnboardService onboardService) {
        this.onboardService = onboardService;
    }

    @PostMapping("/tenant")
    public ResponseEntity<TenantDTO> onboardTenant(@jakarta.validation.Valid @RequestBody TenantDTO tenantDTO) {
        return ResponseEntity.ok(onboardService.onboardTenant(tenantDTO));
    }

    @GetMapping("/tenants")
    public ResponseEntity<List<TenantDTO>> getAllTenants() {
        return ResponseEntity.ok(onboardService.getAllTenants());
    }

    @GetMapping("/tenants/type/{type}")
    public ResponseEntity<List<TenantDTO>> getTenantsByType(@PathVariable String type) {
        return ResponseEntity.ok(onboardService.getTenantsByType(type));
    }

    @GetMapping("/tenant/{id}")
    public ResponseEntity<TenantDTO> getTenantById(@PathVariable Long id) {
        return ResponseEntity.ok(onboardService.getTenantById(id));
    }

    @PatchMapping("/tenant/{id}/status")
    public ResponseEntity<TenantDTO> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(onboardService.updateTenantStatus(id, status));
    }
}
