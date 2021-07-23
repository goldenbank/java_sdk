package com.lola.digiccy.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * description
 * </p>
 *
 * @author Micheal
 * @since 2021/3/17
 */
@Data
public class KeyPairDto {

    @ApiModelProperty(value = "私钥信息")
    private String privateKey;
    @ApiModelProperty(value = "公钥信息")
    private String publicKey;
}
