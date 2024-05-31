package com.sorinbratosin.licenta.Database;

import com.sorinbratosin.licenta.POJO.IstoricIrigare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IstoricIrigareDAO extends JpaRepository<IstoricIrigare, Long> {

    @Query(value = "SELECT * FROM istoric_irigare ORDER BY id DESC LIMIT 1", nativeQuery = true)
    IstoricIrigare theMostRecentIstoricIrigare();
}
