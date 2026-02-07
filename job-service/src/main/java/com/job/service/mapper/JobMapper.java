package com.job.service.mapper;

import com.job.service.entity.Job;
import com.job.service.entity.JobSkill;
import com.job.service.record.JobDTO;

import java.util.stream.Collectors;

public class JobMapper {

    public static JobDTO toDTO(Job job) {
        if (job == null)
            return null;

        return new JobDTO(
                job.getId(),
                job.getTenantId(),
                job.getCountryId(),
                job.getTitle(),
                job.getDescription(),
                job.getExperienceMin(),
                job.getExperienceMax(),
                job.getJobType(),
                job.getStatus(),
                job.getCreatedAt(),
                job.getJobSkills().stream()
                        .map(skill -> new JobDTO.JobSkillDTO(skill.getSkillId(), skill.getMandatory()))
                        .collect(Collectors.toList()));
    }

    public static Job toEntity(JobDTO dto) {
        if (dto == null)
            return null;

        Job job = new Job();
        job.setId(dto.id());
        job.setTenantId(dto.tenantId());
        job.setCountryId(dto.countryId());
        job.setTitle(dto.title());
        job.setDescription(dto.description());
        job.setExperienceMin(dto.experienceMin());
        job.setExperienceMax(dto.experienceMax());
        job.setJobType(dto.jobType());
        job.setStatus(dto.status());

        if (dto.skills() != null) {
            job.setJobSkills(dto.skills().stream()
                    .map(skillDto -> new JobSkill(job, skillDto.skillId(), skillDto.mandatory()))
                    .collect(Collectors.toList()));
        }

        return job;
    }
}
