/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esa.mo.nmf.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.ccsds.moims.mo.mc.structures.ConditionalConversionList;

/**
 *
 * @author Kevin Otto <Kevin@KevinOtto.de>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ActionParameter
{

  String name(); // sadly, the compiler removes argument names so it is not possible to automaticaly parse them

  String description() default "";

  byte rawType() default 0;

  String rawUnit() default "";
  String conditionalConversionFieldName() default "";
  byte convertedType() default -1;
  String convertedUnit() default "";
}
