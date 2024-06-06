package com.sorinbratosin.licenta.Service;
import com.sorinbratosin.licenta.Database.DateSenzoriDAO;
import com.sorinbratosin.licenta.Database.IstoricIrigareDAO;
import com.sorinbratosin.licenta.Database.UserDAO;
import com.sorinbratosin.licenta.POJO.DateSenzori;
import com.sorinbratosin.licenta.POJO.HomeDataDTO;
import com.sorinbratosin.licenta.POJO.IstoricIrigare;
import com.sorinbratosin.licenta.POJO.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HomeDataService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private DateSenzoriDAO dateSenzoriDAO;

    @Autowired
    private IstoricIrigareDAO istoricIrigareDAO;

    public HomeDataDTO getHomeData(Long userId) {
        User user = userDAO.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        DateSenzori dateSenzori = dateSenzoriDAO.findFirstByUserIdOrderByDataAdaugariiDesc(userId);
        IstoricIrigare istoricIrigare = istoricIrigareDAO.findFirstByUserIdOrderByDataIrigareDesc(userId);

        HomeDataDTO dto = new HomeDataDTO();
        dto.setLastName(user.getLastName());
        dto.setDataIrigare(istoricIrigare.getDataIrigare());
        dto.setUmiditate(dateSenzori.getUmiditate());
        dto.setDataAdaugarii(dateSenzori.getDataAdaugarii());

        return dto;
    }
}
