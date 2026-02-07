package com.candidate.service.controller;

import com.candidate.service.record.CandidateDTO;
import com.candidate.service.service.CandidateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/candidate")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping("/profile")
    public ResponseEntity<CandidateDTO> getProfile(@RequestParam String email) {
        return ResponseEntity.ok(candidateService.getProfileByEmail(email));
    }

    @PutMapping("/profile")
    public ResponseEntity<CandidateDTO> updateProfile(@RequestBody CandidateDTO candidateDTO) {
        return ResponseEntity.ok(candidateService.updateProfile(candidateDTO));
    }
}
