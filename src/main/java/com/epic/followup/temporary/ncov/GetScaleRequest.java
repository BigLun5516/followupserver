package com.epic.followup.temporary.ncov;

import com.epic.followup.temporary.NormalUserRequest;

/**
 * @author : zx
 * @version V1.0
 */
public class GetScaleRequest extends NormalUserRequest {
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
