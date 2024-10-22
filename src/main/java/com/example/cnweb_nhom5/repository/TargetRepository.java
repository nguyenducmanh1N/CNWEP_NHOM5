package com.example.cnweb_nhom5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cnweb_nhom5.domain.Target;

import java.util.List;

@Repository
public interface TargetRepository extends JpaRepository<Target, Long> {

    List<Target> findAll();

    Target findByName(String name);
}
