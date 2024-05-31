package com.sorinbratosin.licenta.Service;

import com.sorinbratosin.licenta.Database.IstoricIrigareDAO;
import com.sorinbratosin.licenta.POJO.IstoricIrigare;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IstoricIrigareService {

    @Autowired
    IstoricIrigareDAO istoricIrigareDAO;


    public void saveIstoricIrigare(IstoricIrigare istoricIrigare) {
        istoricIrigareDAO.save(istoricIrigare);
    }

    IstoricIrigare latestIstoricIrigare() {
        return istoricIrigareDAO.theMostRecentIstoricIrigare();
    }

}
