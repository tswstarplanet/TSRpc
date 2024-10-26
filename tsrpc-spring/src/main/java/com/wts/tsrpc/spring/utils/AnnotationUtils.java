/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wts.tsrpc.spring.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

@SuppressWarnings("unchecked")
public class AnnotationUtils {

    /**
     * Check if an annotation of the specified {@code annotationClass} is
     * @param element AnnotatedElement
     * @param annotationClass annotation class
     * @return true if the annotation is present, false otherwise
     */
    public static boolean isAnnotatedWith(AnnotatedElement element, Class<?> annotationClass) {
        return element.isAnnotationPresent(annotationClass.asSubclass(java.lang.annotation.Annotation.class));
    }

    /**
     * Find a single {@link Annotation} of {@code annotationType} on the specified {@link AnnotatedElement}.
     * @param annotatedClass AnnotatedElement
     * @param annotationClass annotation class
     * @param <T> annotation type
     * @return the annotation, or {@code null} if not found
     */
    public static <T extends Annotation> T getAnnotationInfo(Class<?> annotatedClass, Class<T> annotationClass) {
        if (annotatedClass.isAnnotationPresent(annotationClass)) {
            return annotatedClass.getAnnotation(annotationClass);
        }
        return null;
    }

}
