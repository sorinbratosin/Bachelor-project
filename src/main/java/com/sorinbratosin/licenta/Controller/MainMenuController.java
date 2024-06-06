package com.sorinbratosin.licenta.Controller;
import com.sorinbratosin.licenta.POJO.HomeDataDTO;
import com.sorinbratosin.licenta.Service.HomeDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MainMenuController {

    @Autowired
    private HomeDataService homeDataService;

    @GetMapping("/date-home/{userId}")
    public HomeDataDTO getHomeData(@PathVariable Long userId) {
        return homeDataService.getHomeData(userId);
    }
}
