package com.sorinbratosin.licenta.Database;
import com.sorinbratosin.licenta.POJO.IstoricIrigare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IstoricIrigareDAO extends JpaRepository<IstoricIrigare, Long> {

    IstoricIrigare findFirstByUserIdOrderByDataIrigareDesc(Long userId);
}
