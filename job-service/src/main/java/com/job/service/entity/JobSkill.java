package com.job.service.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "job_skill")
public class JobSkill {

    @EmbeddedId
    private JobSkillId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("jobId")
    @JoinColumn(name = "job_id")
    private Job job;

    @Column(nullable = false)
    private Boolean mandatory = true;

    public JobSkill() {
    }

    public JobSkill(Job job, Long skillId, Boolean mandatory) {
        this.job = job;
        this.mandatory = mandatory;
        this.id = new JobSkillId(job.getId(), skillId);
    }

    public JobSkillId getId() {
        return id;
    }

    public void setId(JobSkillId id) {
        this.id = id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Long getSkillId() {
        return id != null ? id.getSkillId() : null;
    }

    public void setSkillId(Long skillId) {
        if (this.id == null)
            this.id = new JobSkillId();
        this.id.setSkillId(skillId);
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    @Embeddable
    public static class JobSkillId implements Serializable {
        private Long jobId;
        private Long skillId;

        public JobSkillId() {
        }

        public JobSkillId(Long jobId, Long skillId) {
            this.jobId = jobId;
            this.skillId = skillId;
        }

        public Long getJobId() {
            return jobId;
        }

        public void setJobId(Long jobId) {
            this.jobId = jobId;
        }

        public Long getSkillId() {
            return skillId;
        }

        public void setSkillId(Long skillId) {
            this.skillId = skillId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            JobSkillId that = (JobSkillId) o;
            return Objects.equals(jobId, that.jobId) && Objects.equals(skillId, that.skillId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(jobId, skillId);
        }
    }
}
