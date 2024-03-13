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

package com.wts.tsrpc.spring.config.annotation;

/**
 * Constants of key of property source
 */
public class PropertySourceConfigConstants {

    public static final String GENERAL_PREFIX = "tsrpc";

    public static final String APPLICATION = "application";

    public static final String APPLICATION_ID = STR."\{GENERAL_PREFIX}.\{APPLICATION}.id";

    public static final String APPLICATION_VERSION = STR."\{GENERAL_PREFIX}.\{APPLICATION}.version";

    public static final String DEFAULT_APPLICATION_VERSION = "1.0";
}
