package com.github.pshirshov.izumi.distage.model.exceptions

import com.github.pshirshov.izumi.fundamentals.reflection._

class TraitInitializationFailedException(message: String, val tpe: TypeFull, cause: Throwable) extends DIException(message, cause)
