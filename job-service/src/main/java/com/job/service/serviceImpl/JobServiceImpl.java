package com.job.service.serviceImpl;

import com.job.service.entity.Job;
import com.job.service.mapper.JobMapper;
import com.job.service.record.JobDTO;
import com.job.service.repository.JobRepository;
import com.job.service.service.JobService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    @Transactional
    public JobDTO createJob(JobDTO jobDTO) {
        Job job = JobMapper.toEntity(jobDTO);
        Job saved = jobRepository.save(job);
        return JobMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public JobDTO updateJob(Long id, JobDTO jobDTO) {
        Job existing = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));

        Job updated = JobMapper.toEntity(jobDTO);
        updated.setId(existing.getId());

        Job saved = jobRepository.save(updated);
        return JobMapper.toDTO(saved);
    }

    @Override
    public List<JobDTO> getAllJobs() {
        return jobRepository.findAll().stream()
                .map(JobMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public JobDTO getJobById(Long id) {
        return jobRepository.findById(id)
                .map(JobMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
    }

    @Override
    public List<JobDTO> getJobsByTenant(Long tenantId) {
        return jobRepository.findByTenantId(tenantId).stream()
                .map(JobMapper::toDTO)
                .collect(Collectors.toList());
    }
}
