package com.dom.adv.api.repository;

import com.dom.adv.api.entity.Advertise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertiseRepository extends JpaRepository<Advertise, Long> {

    Page<Advertise> findAllByActive(boolean active, Pageable pageable);
    Page<Advertise> findAllByActiveAndCategoryDescriptionIgnoreCase(Boolean active, String category, Pageable pageable);
    Page<Advertise> findAllByCategoryDescriptionIgnoreCase(String category, Pageable pageable);

}
