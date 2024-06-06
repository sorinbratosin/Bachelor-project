package com.sorinbratosin.licenta.Service;
import com.sorinbratosin.licenta.Database.DateSenzoriDAO;
import com.sorinbratosin.licenta.POJO.DateSenzori;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DateSenzoriService {

    @Autowired
    DateSenzoriDAO dateSenzoriDAO;

    public void saveDateSenzori(DateSenzori dateSenzori) {
        dateSenzoriDAO.save(dateSenzori);
    }

    public DateSenzori findByUserId(long userId) {
        return dateSenzoriDAO.findByUserId(userId);
    }
}
