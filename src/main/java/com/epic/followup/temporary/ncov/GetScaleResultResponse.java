package com.epic.followup.temporary.ncov;

import com.epic.followup.temporary.DealMessageResponse;

/**
 * @author : zx
 * @version V1.0
 */
public class GetScaleResultResponse extends DealMessageResponse {

    private ScaleResult[] scaleResults;

    public ScaleResult[] getScaleResults() {
        return scaleResults;
    }

    public void setScaleResults(ScaleResult[] scaleResults) {
        this.scaleResults = scaleResults;
    }
}
