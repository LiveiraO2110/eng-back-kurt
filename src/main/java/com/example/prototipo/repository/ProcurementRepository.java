package com.example.prototipo.repository;

import com.example.prototipo.models.Procurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcurementRepository extends JpaRepository<Procurement, Long> {
    List<Procurement> findByCustomer_Id(Long id);

    boolean existsByCustomer_IdAndPncpId(Long customer_id, String pncpid);

    List<Procurement> findByPncpId(String pncpId);
}
