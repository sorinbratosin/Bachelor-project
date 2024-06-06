package com.sorinbratosin.licenta.Database;
import com.sorinbratosin.licenta.POJO.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceDAO extends JpaRepository<Device, Long> {

    Device findBySerialNumber(String serialNumber);
}
