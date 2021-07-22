package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Ha;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Ha entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HaRepository extends JpaRepository<Ha, Long> {}
