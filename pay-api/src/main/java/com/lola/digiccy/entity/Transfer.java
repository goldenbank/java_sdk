package com.lola.digiccy.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author MECHREVO
 */
@Data
public class Transfer {
    private static final long serialVersionUID = 459330910026929184L;
    private String address;

    private BigDecimal amount;

    private String mainType;

    private String subType;

    private String callUrl;

    private String checkUrl;

    private String memo;

    private String blockchainTag;
}
