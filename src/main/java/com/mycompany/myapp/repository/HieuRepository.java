package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Hieu;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Hieu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HieuRepository extends JpaRepository<Hieu, Long> {}
