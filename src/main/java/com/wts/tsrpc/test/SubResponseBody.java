package com.wts.tsrpc.test;

import java.util.List;

public class SubResponseBody {
    private String subResCode;

    private List<Integer> subResList;

    public String getSubResCode() {
        return subResCode;
    }

    public void setSubResCode(String subResCode) {
        this.subResCode = subResCode;
    }

    public List<Integer> getSubResList() {
        return subResList;
    }

    public void setSubResList(List<Integer> subResList) {
        this.subResList = subResList;
    }
}
