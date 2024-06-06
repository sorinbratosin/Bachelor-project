package com.sorinbratosin.licenta.Service;
import com.sorinbratosin.licenta.Database.DeviceDAO;
import com.sorinbratosin.licenta.POJO.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {

    @Autowired
    DeviceDAO deviceDAO;

    public Device findBySerialNumber(String serialNumber) {
        return deviceDAO.findBySerialNumber(serialNumber);
    }

    public void save(Device device) {
        deviceDAO.save(device);
    }
}
