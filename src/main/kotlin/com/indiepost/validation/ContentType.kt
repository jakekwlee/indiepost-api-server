package com.indiepost.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ContentTypeMultipartFileValidator::class])
@Target(AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER,
        AnnotationTarget.FIELD,
        AnnotationTarget.ANNOTATION_CLASS,
        AnnotationTarget.CONSTRUCTOR,
        AnnotationTarget.VALUE_PARAMETER)
annotation class ContentType(
        /**
         * 허용되는 콘텐츠 타입
         *
         *
         * 예:
         *
         *  * application/pdf
         *  * application/msword
         *  * images/png
         *
         *
         * @return 허용되는 콘텐츠 타입
         */
        vararg val value: String,
        val message: String = "Default Error Message",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = [])