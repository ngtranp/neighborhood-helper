package org.coreservice.repository;

import org.coreservice.entity.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoreRepository extends JpaRepository<ServiceRequest, Long> {
    List<ServiceRequest> findByStatus(String status);
}
