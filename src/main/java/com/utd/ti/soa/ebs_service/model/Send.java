package com.utd.ti.soa.ebs_service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Send {
    private String destination;
    private String origin;
    private String address;
    private Boolean fragile;
    private String extraInformation;
    private String costumerName;
    private Float weight_num;
    private String weight_type;
    private Float packageDimensions_width;
    private Float packageDimensions_height;
    private Float packageDimensions_depth;
    private String packageDimensions_type;
}
