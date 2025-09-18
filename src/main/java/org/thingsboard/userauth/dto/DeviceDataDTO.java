package org.thingsboard.userauth.dto;

import lombok.Getter;

@Getter
public class DeviceDataDTO {
    private Long deviceId;   // add deviceId
    private Double temperature;
    private Double humidity;
}
