package com.utd.ti.soa.ebs_service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    private String name;
    private Float price;
    private Float dimensions_width;
    private Float dimensions_height;
    private Float dimensions_depth;
    private String dimensions_type;
    private Float weight_num;
    private String weight_type;
    private String description;
    private String productMarlk;
    private String material;
    private String photo;
}
