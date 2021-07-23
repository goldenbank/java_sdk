package com.lola.digiccy.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author MECHREVO
 */
@Data
public class Order {
    private Integer mainType;
    private String subType;
    private String outerId;
}
