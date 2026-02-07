package com.onboard.service.serviceImpl;

import com.onboard.service.entity.AppUser;
import com.onboard.service.entity.Tenant;
import com.onboard.service.exception.ResourceNotFoundException;
import com.onboard.service.mapper.TenantMapper;
import com.onboard.service.record.TenantDTO;
import com.onboard.service.repository.AppUserRepository;
import com.onboard.service.repository.CountryRepository;
import com.onboard.service.repository.StateRepository;
import com.onboard.service.repository.TenantRepository;
import com.onboard.service.service.EmailService;
import com.onboard.service.service.OnboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OnboardServiceImpl implements OnboardService {

    private static final Logger logger = LoggerFactory.getLogger(OnboardServiceImpl.class);
    
    private final TenantRepository tenantRepository;
    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;
    private final AppUserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public OnboardServiceImpl(TenantRepository tenantRepository, 
                              CountryRepository countryRepository, 
                              StateRepository stateRepository,
                              AppUserRepository userRepository,
                              EmailService emailService,
                              PasswordEncoder passwordEncoder) {
        this.tenantRepository = tenantRepository;
        this.countryRepository = countryRepository;
        this.stateRepository = stateRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public TenantDTO onboardTenant(TenantDTO tenantDTO) {
        logger.info("Initiating onboarding for tenant: {} of type: {}", tenantDTO.name(), tenantDTO.type());
        
        // 1. Basic Validations
        if (tenantDTO.locations() == null || tenantDTO.locations().isEmpty()) {
            throw new IllegalArgumentException("At least one location (country mapping) is required during onboarding.");
        }

        if (userRepository.existsByEmail(tenantDTO.adminEmail())) {
            throw new IllegalArgumentException("An account with this email already exists: " + tenantDTO.adminEmail());
        }

        // 2. Geographical Validations
        for (TenantDTO.TenantLocationDTO loc : tenantDTO.locations()) {
            if (loc.countryId() == null) {
                throw new IllegalArgumentException("Country ID is mandatory for all location mappings.");
            }
            if (!countryRepository.existsById(loc.countryId())) {
                throw new ResourceNotFoundException("Country not found with ID: " + loc.countryId());
            }
            if (loc.stateId() != null) {
                if (stateRepository.findByIdAndCountryId(loc.stateId(), loc.countryId()).isEmpty()) {
                    throw new ResourceNotFoundException("State ID " + loc.stateId() + " does not belong to Country ID " + loc.countryId());
                }
            }
        }

        try {
            // 3. Save Tenant
            Tenant tenant = TenantMapper.toEntity(tenantDTO);
            Tenant savedTenant = tenantRepository.save(tenant);
            
            // 4. Create Admin User
            String temporaryPassword = UUID.randomUUID().toString().substring(0, 8);
            AppUser admin = new AppUser();
            admin.setTenant(savedTenant);
            admin.setEmail(tenantDTO.adminEmail());
            admin.setPasswordHash(passwordEncoder.encode(temporaryPassword));
            
            // Set Role based on Tenant Type
            if (savedTenant.getType() == Tenant.TenantType.COMPANY) {
                admin.setRole(AppUser.UserRole.COMPANY_ADMIN);
            } else {
                admin.setRole(AppUser.UserRole.VENDOR_ADMIN);
            }
            
            userRepository.save(admin);
            logger.info("Admin account created for email: {}", tenantDTO.adminEmail());

            // 5. Send Welcome Email
            emailService.sendWelcomeEmail(
                tenantDTO.adminEmail(), 
                "Administrator", 
                temporaryPassword, 
                savedTenant.getName()
            );

            logger.info("Successfully onboarded tenant with ID: {}", savedTenant.getId());
            return TenantMapper.toDTO(savedTenant);
        } catch (Exception e) {
            logger.error("Error occurred while onboarding tenant {}: {}", tenantDTO.name(), e.getMessage());
            throw e;
        }
    }

    @Override
    public List<TenantDTO> getAllTenants() {
        return tenantRepository.findAll().stream()
                .map(TenantMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TenantDTO> getTenantsByType(String type) {
        try {
            return tenantRepository.findByType(Tenant.TenantType.valueOf(type.toUpperCase()))
                    .stream()
                    .map(TenantMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid tenant type. Allowed values: COMPANY, VENDOR");
        }
    }

    @Override
    public TenantDTO getTenantById(Long id) {
        return tenantRepository.findById(id)
                .map(TenantMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with id: " + id));
    }

    @Override
    @Transactional
    public TenantDTO updateTenantStatus(Long id, String status) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with id: " + id));
        try {
            tenant.setStatus(Tenant.TenantStatus.valueOf(status.toUpperCase()));
            Tenant updated = tenantRepository.save(tenant);
            return TenantMapper.toDTO(updated);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status. Allowed values: ACTIVE, INACTIVE");
        }
    }
}
