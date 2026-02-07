package com.candidate.service.service;

import com.candidate.service.record.CandidateDTO;
import java.util.Optional;

public interface CandidateService {
    CandidateDTO getProfileByEmail(String email);

    CandidateDTO updateProfile(CandidateDTO candidateDTO);
}
