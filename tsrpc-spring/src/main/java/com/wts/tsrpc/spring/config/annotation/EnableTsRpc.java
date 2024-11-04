///*
// * Copyright 2024 wts
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.wts.tsrpc.spring.config.annotation;
//
//
//import org.springframework.context.annotation.Import;
//
//import java.lang.annotation.Documented;
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Inherited;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
///**
// * Enable TsRpc bean as spring beans
// */
//
//@Inherited
//@Documented
//@Target({ElementType.TYPE})
//@Retention(RetentionPolicy.RUNTIME)
//@Import(TsRpcScanRegistrar.class)
//public @interface EnableTsRpc {
//
//    /**
//     * The application id.
//     */
//    String applicationId();
//
//    /**
//     * The packages to scan TsRpc beans.
//     */
//    String[] basePackages() default {};
//}
