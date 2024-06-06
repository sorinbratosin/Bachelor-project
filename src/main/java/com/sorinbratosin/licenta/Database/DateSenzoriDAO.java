package com.sorinbratosin.licenta.Database;
import com.sorinbratosin.licenta.POJO.DateSenzori;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DateSenzoriDAO extends JpaRepository<DateSenzori, Long> {

    DateSenzori findFirstByUserIdOrderByDataAdaugariiDesc(Long userId);

    DateSenzori findByUserId(Long userId);
}
