package com.example.prototipo.repository;

import com.example.prototipo.models.SearchTerms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SearchTermsRepository extends JpaRepository<SearchTerms, Long> {
    Optional<SearchTerms> findByTermAndCustomer_Id(String term, Long customer_id);
}
