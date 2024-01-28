package com.wts.tsrpc.client;

public class BalanceAggregate {
    private String applicationId;

    private String serviceId;

    public BalanceAggregate(String applicationId, String serviceId) {
        this.applicationId = applicationId;
        this.serviceId = serviceId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
