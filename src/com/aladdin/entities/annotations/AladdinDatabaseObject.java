package com.aladdin.entities.annotations;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AladdinDatabaseObject
{
	Class<? extends Serializable> keyClass();

	String getTbl();
}