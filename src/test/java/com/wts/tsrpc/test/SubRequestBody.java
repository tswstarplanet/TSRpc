package com.wts.tsrpc.test;

import java.util.List;

public class SubRequestBody {
    private String subCode;

    private List<Integer> subList;

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public List<Integer> getSubList() {
        return subList;
    }

    public void setSubList(List<Integer> subList) {
        this.subList = subList;
    }
}
