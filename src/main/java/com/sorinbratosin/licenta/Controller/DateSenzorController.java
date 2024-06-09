package com.sorinbratosin.licenta.Controller;
import com.sorinbratosin.licenta.POJO.DateSenzori;
import com.sorinbratosin.licenta.POJO.Device;
import com.sorinbratosin.licenta.POJO.IstoricIrigare;
import com.sorinbratosin.licenta.POJO.User;
import com.sorinbratosin.licenta.Service.DateSenzoriService;
import com.sorinbratosin.licenta.Service.DeviceService;
import com.sorinbratosin.licenta.Service.IstoricIrigareService;
import com.sorinbratosin.licenta.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
public class DateSenzorController {

    @Autowired
    DateSenzoriService dateSenzoriService;

    @Autowired
    IstoricIrigareService istoricIrigareService;

    @Autowired
    DeviceService deviceService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/postHumidity", method = RequestMethod.POST)
    @ResponseBody
    public String PostSensorData(@RequestBody String requestBody) throws JsonProcessingException {
            String status = null;

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonData = objectMapper.readValue(requestBody, new TypeReference<Map<String, Object>>() {
            });

        Boolean aFostIrigat = (Boolean) jsonData.get("irigat");
        String serialNumber = (String) jsonData.get("device_serial_number");
        Long userId = ((Number) jsonData.get("user_id")).longValue();
        Integer umiditate = (Integer) jsonData.get("humidity");

        if (aFostIrigat == null || serialNumber == null || userId == null || umiditate == null) {
            throw new IllegalArgumentException("Datele primite sunt incomplete sau invalide.");
        }

        //salvare device daca nu exista
        User user = userService.findById(userId);
        if(user == null) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }

        Device device = deviceService.findBySerialNumber(serialNumber);
        if(device == null) {
            device = new Device();
            device.setUser(user);
            device.setSerialNumber(serialNumber);
            deviceService.save(device);
        }

        //adaugare date senzor umiditate
        DateSenzori dateSenzori = new DateSenzori();
        dateSenzori.setUmiditate(umiditate);
        dateSenzori.setDataAdaugarii(LocalDateTime.now());
        dateSenzori.setDevice(device);
        dateSenzori.setUser(user);
        dateSenzoriService.saveDateSenzori(dateSenzori);

        //salvare irigare
        if(aFostIrigat) {
            IstoricIrigare istoricIrigare = new IstoricIrigare();
            istoricIrigare.setDataIrigare(LocalDateTime.now());
            istoricIrigare.setUser(user);
            istoricIrigare.setIdSenzor(dateSenzori);
            istoricIrigareService.saveIstoricIrigare(istoricIrigare);
            status = "S-au salvat datele si istoricul irigarii";
        } else {
            status = "S-au salvat datele";
        }
        return status;
    }
}
