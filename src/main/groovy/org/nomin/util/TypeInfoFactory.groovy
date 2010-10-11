package org.nomin.util

import java.lang.reflect.*
import org.nomin.core.NominException

/**
 * Constructs TypeInfo instances.
 * @author Dmitry Dobrynin
 * Date: 09.10.2010 Time: 22:02:40
 */
class TypeInfoFactory {
  static TypeInfo typeInfo(type) {
    if (Class.isInstance(type))
      return type.isArray() ? new TypeInfo(type, [new TypeInfo(type.componentType)]) : new TypeInfo(type)
    if (ParameterizedType.isInstance(type))
      return new TypeInfo(type.rawType, type.actualTypeArguments.collect { typeInfo(it) })
    if (WildcardType.isInstance(type))
      return typeInfo(type.upperBounds ? type.upperBounds[0] : type.lowerBounds ? typeInfo(type.lowerBounds[0]) : Object)
    if (TypeVariable.isInstance(type))
      return typeInfo(type.bounds ? type.bounds[0] : Object)
    if (Closure.isInstance(type)) return new TypeInfo(type)

    throw new NominException("Could not recognize type ${type}!")
  }

  static TypeInfo typeInfo(type, params) {
    new TypeInfo(type,
            Collection.isInstance(params) ? params.collect { typeInfo(it) } : [TypeInfoFactory.typeInfo(params)])
  }
}
