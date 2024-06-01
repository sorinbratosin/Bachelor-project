package com.sorinbratosin.licenta.Database;

import com.sorinbratosin.licenta.POJO.DateSenzori;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DateSenzoriDAO extends JpaRepository<DateSenzori, Long> {

    @Query(value = "SELECT * FROM date_senzori ORDER BY id DESC LIMIT 1", nativeQuery = true)
    DateSenzori theMostRecentDateSenzori();

    DateSenzori findFirstByUserIdOrderByDataAdaugariiDesc(Long userId);
}
