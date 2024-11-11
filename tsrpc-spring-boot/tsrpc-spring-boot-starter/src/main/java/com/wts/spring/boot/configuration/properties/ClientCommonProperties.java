/*
 * Copyright 2024 wts
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wts.spring.boot.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "tsrpc.client.common")
public class ClientCommonProperties {

    private String balancerType = "random";

    private String basePackage;

    private Long timeout = 6000L;

    private List<String> invokerFilters = List.of("com.wts.tsrpc.client.filter.LogClientInvokerFilter", "com.wts.tsrpc.client.filter.CheckClientInvokerFilter");


    public String getBalancerType() {
        return balancerType;
    }

    public void setBalancerType(String balancerType) {
        this.balancerType = balancerType;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public List<String> getInvokerFilters() {
        return invokerFilters;
    }

    public void setInvokerFilters(List<String> invokerFilters) {
        this.invokerFilters = invokerFilters;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }
}
