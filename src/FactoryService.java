package com.example.cnweb_nhom5.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.cnweb_nhom5.domain.Factory;
import com.example.cnweb_nhom5.repository.FactoryRepository;

@Service
public class FactoryService {

    private final FactoryRepository factoryRepository;

    public FactoryService(FactoryRepository factoryRepository) {
        this.factoryRepository = factoryRepository;
    }

    public List<Factory> fetchAllFactories() {
        return factoryRepository.findAll();
    }

    public Factory createFactory(Factory factory) {
        return factoryRepository.save(factory);
    }

    public Optional<Factory> fetchFactoryById(long id) {
        return factoryRepository.findById(id);
    }

    public void deleteFactory(long id) {
        factoryRepository.deleteById(id);
    }

    public Factory getFactoryByName(String name) {
        return factoryRepository.findByName(name);
    }
}
