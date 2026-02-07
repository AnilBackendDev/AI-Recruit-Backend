package com.candidate.service.mapper;

import com.candidate.service.entity.Candidate;
import com.candidate.service.entity.CandidateEducation;
import com.candidate.service.entity.CandidateExperience;
import com.candidate.service.record.CandidateDTO;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class CandidateMapper {

        public static CandidateDTO toDTO(Candidate candidate) {
                if (candidate == null)
                        return null;

                return new CandidateDTO(
                                candidate.getId(),
                                candidate.getFirstName(),
                                candidate.getLastName(),
                                candidate.getEmail(),
                                candidate.getMobile(),
                                candidate.getSummary(),
                                candidate.getLinkedin(),
                                candidate.getWebsite(),
                                candidate.getTotalExperience(),
                                candidate.getCurrentCtc(),
                                candidate.getExpectedCtc(),
                                candidate.getLocation(),
                                candidate.getEducations() != null ? candidate.getEducations().stream()
                                                .map(edu -> new CandidateDTO.EducationDTO(edu.getId(), edu.getDegree(),
                                                                edu.getInstitution(), edu.getGraduationYear()))
                                                .collect(Collectors.toList()) : null,
                                candidate.getExperiences() != null ? candidate.getExperiences().stream()
                                                .map(exp -> new CandidateDTO.ExperienceDTO(exp.getId(),
                                                                exp.getCompanyName(), exp.getDesignation(),
                                                                exp.getFromDate() != null ? exp.getFromDate().toString()
                                                                                : null,
                                                                exp.getToDate() != null ? exp.getToDate().toString()
                                                                                : null))
                                                .collect(Collectors.toList()) : null);
        }

        public static Candidate toEntity(CandidateDTO dto) {
                if (dto == null)
                        return null;

                Candidate candidate = Candidate.builder()
                                .id(dto.id())
                                .firstName(dto.firstName())
                                .lastName(dto.lastName())
                                .email(dto.email())
                                .mobile(dto.mobile())
                                .summary(dto.summary())
                                .linkedin(dto.linkedin())
                                .website(dto.website())
                                .totalExperience(dto.totalExperience())
                                .currentCtc(dto.currentCtc())
                                .expectedCtc(dto.expectedCtc())
                                .location(dto.location())
                                .build();

                if (dto.educations() != null) {
                        candidate.setEducations(dto.educations().stream()
                                        .map(eduDto -> {
                                                CandidateEducation edu = CandidateEducation.builder()
                                                                .id(eduDto.id())
                                                                .degree(eduDto.degree())
                                                                .institution(eduDto.institution())
                                                                .graduationYear(eduDto.graduationYear())
                                                                .candidate(candidate)
                                                                .build();
                                                return edu;
                                        }).collect(Collectors.toList()));
                }

                if (dto.experiences() != null) {
                        candidate.setExperiences(dto.experiences().stream()
                                        .map(expDto -> {
                                                CandidateExperience exp = CandidateExperience.builder()
                                                                .id(expDto.id())
                                                                .companyName(expDto.companyName())
                                                                .designation(expDto.designation())
                                                                .fromDate(expDto.fromDate() != null
                                                                                ? LocalDate.parse(expDto.fromDate())
                                                                                : null)
                                                                .toDate(expDto.toDate() != null
                                                                                ? LocalDate.parse(expDto.toDate())
                                                                                : null)
                                                                .candidate(candidate)
                                                                .build();
                                                return exp;
                                        }).collect(Collectors.toList()));
                }

                return candidate;
        }
}
