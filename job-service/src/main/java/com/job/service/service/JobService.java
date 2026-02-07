package com.job.service.service;

import com.job.service.record.JobDTO;
import java.util.List;

public interface JobService {
    JobDTO createJob(JobDTO jobDTO);

    JobDTO updateJob(Long id, JobDTO jobDTO);

    List<JobDTO> getAllJobs();

    JobDTO getJobById(Long id);

    List<JobDTO> getJobsByTenant(Long tenantId);
}
