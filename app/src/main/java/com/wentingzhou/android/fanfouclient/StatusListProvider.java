package com.wentingzhou.android.fanfouclient;

import com.wentingzhou.android.fanfouclient.model.FanfouStatus;

import java.util.List;

/**
 * Created by wenting on 3/19/18.
 */

public class StatusListProvider {
    private List<FanfouStatus> statusList;


    StatusListProvider(List<FanfouStatus> statusList) {
        this.statusList = statusList;
    }

    public void setStatusList(List<FanfouStatus> statusList) {
        this.statusList = statusList;
    }

    public FanfouStatus get(int pos) {
        return statusList.get(pos);
    }

    public List<FanfouStatus> getList() {
        return statusList;
    }

    public void appendList(List<FanfouStatus> newList) {
        statusList.addAll(newList);
    }
    public int size() {
        return statusList.size();
    }
}
