package com.epic.followup.temporary;

/**
 * @author : zx
 * @version V1.0
 */
public class UnionIdRequest extends NormalUserRequest {

    private String encryptedData;
    private String iv;
    private String code;

    public String getEncryptedData() {
        return encryptedData;
    }

    public String getCode() {
        return code;
    }

    public String getIv() {
        return iv;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }
}
