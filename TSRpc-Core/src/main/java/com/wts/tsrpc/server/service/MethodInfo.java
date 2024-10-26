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

package com.wts.tsrpc.server.service;

public class MethodInfo {

    private String methodName;

    private Class<?>[] argTypes;

    private Class<?> returnType;

    public MethodInfo() {
    }

    public MethodInfo(String methodName) {
        this.methodName = methodName;
    }

    public MethodInfo(String methodName, Class<?>[] argTypes) {
        this.methodName = methodName;
        this.argTypes = argTypes;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public MethodInfo methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public Class<?>[] getArgTypes() {
        return argTypes;
    }

    public void setArgTypes(Class<?>[] argTypes) {
        this.argTypes = argTypes;
    }

    public MethodInfo argTypes(Class<?>[] argTypes) {
        this.argTypes = argTypes;
        return this;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public MethodInfo returnType(Class<?> returnType) {
        this.returnType = returnType;
        return this;
    }
}
