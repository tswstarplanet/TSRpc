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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ConfigurationProperties(prefix = "tsrpc.server")
public class ServerProperties {

    private String protocol;

    private Integer port = 6688;

    private Integer bossNum = 1;

    private Integer workerNum = 10;

    private String serviceInvoker;

    private String transformType;

    private List<String> invokerFilters = new ArrayList<>(Arrays.asList("com.wts.tsrpc.server.filter.LogServerInvokerFilter", "com.wts.tsrpc.server.filter.CheckServerInvokerFilter"));

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getBossNum() {
        return bossNum;
    }

    public void setBossNum(Integer bossNum) {
        this.bossNum = bossNum;
    }

    public Integer getWorkerNum() {
        return workerNum;
    }

    public void setWorkerNum(Integer workerNum) {
        this.workerNum = workerNum;
    }

    public String getServiceInvoker() {
        return serviceInvoker;
    }

    public void setServiceInvoker(String serviceInvoker) {
        this.serviceInvoker = serviceInvoker;
    }

    public String getTransformType() {
        return transformType;
    }

    public void setTransformType(String transformType) {
        this.transformType = transformType;
    }

    public List<String> getInvokerFilters() {
        return invokerFilters;
    }

    public void setInvokerFilters(List<String> invokerFilters) {
        this.invokerFilters = invokerFilters;
    }
}
