package com.candidate.service.serviceImpl;

import com.candidate.service.entity.Candidate;
import com.candidate.service.mapper.CandidateMapper;
import com.candidate.service.record.CandidateDTO;
import com.candidate.service.repository.CandidateRepository;
import com.candidate.service.service.CandidateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;

    public CandidateServiceImpl(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    @Override
    public CandidateDTO getProfileByEmail(String email) {
        Candidate candidate = candidateRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Candidate not found with email: " + email));
        return CandidateMapper.toDTO(candidate);
    }

    @Override
    @Transactional
    public CandidateDTO updateProfile(CandidateDTO candidateDTO) {
        Candidate candidate = CandidateMapper.toEntity(candidateDTO);

        // Ensure email exists or handle update logic
        Candidate existing = candidateRepository.findByEmail(candidate.getEmail())
                .orElseThrow(() -> new RuntimeException("Candidate not found with email: " + candidate.getEmail()));

        // Map fields from candidate to existing or just save candidate if it has the ID
        candidate.setId(existing.getId());

        Candidate saved = candidateRepository.save(candidate);
        return CandidateMapper.toDTO(saved);
    }
}
