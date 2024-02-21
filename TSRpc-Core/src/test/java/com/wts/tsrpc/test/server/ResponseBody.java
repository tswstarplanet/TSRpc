package com.wts.tsrpc.test.server;

import java.util.List;

public class ResponseBody<S> {
    private String resCode;

    private String resMsg;

    private List<String> resList;

    private S subResBody;

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public List<String> getResList() {
        return resList;
    }

    public void setResList(List<String> resList) {
        this.resList = resList;
    }

    public S getSubResBody() {
        return subResBody;
    }

    public void setSubResBody(S subResBody) {
        this.subResBody = subResBody;
    }
}
