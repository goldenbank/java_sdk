package com.lola.digiccy.constant;

/**
 * 币种类型枚举
 * @Author: shrimp
 * @Date: 2020/10/10 13:43
 */
public enum CoinType {
    BTC("0", "BTC"),
    ETH("60", "ETH"),
    TRX("195", "TRX"),
    HT("2509", "HT"),
    DOGE("3","DOGE"),
    ETES("5001","ETES"),
    OKT("5000","OKT"),
    DOT("6000","DOT"),
    KSM("6001","KSM");
    /**
     * 编码
     */
    private String code;
    /**
     * 单位
     */
    private String unit;

    CoinType(String code, String unit) {
        this.code = code;
        this.unit = unit;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * 根据币种编码获取币种类型
     * @param code
     * @return
     */
    public static CoinType codeOf(Integer code) {
        switch(code) {
            case 0:
                return BTC;
            case 3:
                return DOGE;
            case 60:
                return ETH;
            case 195:
                return TRX;
            case 2509:
                return HT;
            case 5000:
                return OKT;
            case 5001:
                return ETES;
            case 6000:
                return DOT;
            case 6001:
                return KSM;
            default:
                return null;
        }
    }

}
