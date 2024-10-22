package com.example.cnweb_nhom5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cnweb_nhom5.domain.Factory;

import java.util.List;

@Repository
public interface FactoryRepository extends JpaRepository<Factory, Long> {

    List<Factory> findAll();

    Factory findByName(String name);
}