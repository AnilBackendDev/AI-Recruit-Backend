package com.candidate.service.record;

import java.math.BigDecimal;
import java.util.List;

public record CandidateDTO(
                Long id,
                String firstName,
                String lastName,
                String email,
                String mobile,
                String summary,
                String linkedin,
                String website,
                BigDecimal totalExperience,
                BigDecimal currentCtc,
                BigDecimal expectedCtc,
                String location,
                List<EducationDTO> educations,
                List<ExperienceDTO> experiences) {
        public record EducationDTO(
                        Long id,
                        String degree,
                        String institution,
                        Integer graduationYear) {
        }

        public record ExperienceDTO(
                        Long id,
                        String companyName,
                        String designation,
                        String fromDate,
                        String toDate) {
        }
}
