package com.job.service.record;

import com.job.service.entity.Job;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record JobDTO(
        Long id,
        Long tenantId,
        Long countryId,
        String title,
        String description,
        BigDecimal experienceMin,
        BigDecimal experienceMax,
        Job.JobType jobType,
        Job.JobStatus status,
        LocalDateTime createdAt,
        List<JobSkillDTO> skills) {
    public record JobSkillDTO(
            Long skillId,
            Boolean mandatory) {
    }
}
