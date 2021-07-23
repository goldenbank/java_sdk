package com.lola.digiccy.entity;

import lombok.Data;

/**
 * @author MECHREVO
 */
@Data
public class Address {
    private static final long serialVersionUID = 2725214089541876602L;
    private Integer coinType;
    private String callUrl;
    private String alias;
}
