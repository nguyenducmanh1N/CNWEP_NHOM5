package com.example.cnweb_nhom5.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.cnweb_nhom5.domain.Target;
import com.example.cnweb_nhom5.repository.TargetRepository;

@Service
public class TargetService {

    private final TargetRepository targetRepository;

    public TargetService(TargetRepository targetRepository) {
        this.targetRepository = targetRepository;
    }

    public List<Target> fetchAllTargets() {
        return targetRepository.findAll();
    }

    public Target createTarget(Target target) {
        return targetRepository.save(target);
    }

    public Optional<Target> fetchTargetById(long id) {
        return targetRepository.findById(id);
    }

    public void deleteTarget(long id) {
        targetRepository.deleteById(id);
    }

    public Target getTargetByName(String name) {
        return targetRepository.findByName(name);
    }
}
