package com.wentingzhou.android.fanfouclient;

import com.wentingzhou.android.fanfouclient.model.FanfouStatus;

import java.util.List;

/**
 * Created by wenting on 3/20/18.
 */

public interface OnLoadMoreTimelineTaskCompleted {
    void onTaskCompleted(String response);

    void updateReturnedNewList(List<FanfouStatus> list);
}
