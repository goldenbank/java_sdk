package com.lola.digiccy.entity;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author MECHREVO
 */
@NoArgsConstructor
@Data
public class Token implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long merchantId;
    private String  walletNo;
    private String  apiKey;
    private  String gatewayUrl;

}
