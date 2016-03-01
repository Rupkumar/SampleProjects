package com.aladdin.entities.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.aladdin.entities.ColumnType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AladdinColumn
{
	String columnName() default "";

	ColumnType columnType() default ColumnType.AUTO;

	int length() default 0;

	boolean primaryKey() default false;
}