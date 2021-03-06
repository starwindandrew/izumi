package izumi.distage.model.reflection

import izumi.distage.model.definition.ImplDef
import izumi.reflect.Tag

sealed trait DIKey {
  def tpe: SafeType
  override lazy val hashCode: Int = scala.util.hashing.MurmurHash3.productHash(this.asInstanceOf[Product])
}

object DIKey {
  def apply[T: Tag]: DIKey = TypeKey(SafeType.get[T])
  def apply[T: Tag](id: String): DIKey = DIKey.get[T].named(id)

  def get[T: Tag]: DIKey.TypeKey = TypeKey(SafeType.get[T])

  sealed trait BasicKey extends DIKey {
    def withTpe(tpe: SafeType): DIKey.BasicKey
  }

  final case class TypeKey(tpe: SafeType) extends BasicKey {
    final def named[I: IdContract](id: I): IdKey[I] = IdKey(tpe, id)

    override final def withTpe(tpe: SafeType): DIKey.TypeKey = copy(tpe = tpe)
    override final def toString: String = s"{type.${tpe.toString}}"
  }

  final case class IdKey[I: IdContract](tpe: SafeType, id: I) extends BasicKey {
    final val idContract: IdContract[I] = implicitly

    override final def withTpe(tpe: SafeType): DIKey.IdKey[I] = copy(tpe = tpe)
    override final def toString: String = s"{type.${tpe.toString}@${idContract.repr(id)}}"
  }

  final case class ProxyElementKey(proxied: DIKey, tpe: SafeType) extends DIKey {
    override final def toString: String = s"{proxy.${proxied.toString}}"
  }

  final case class ResourceKey(key: DIKey, tpe: SafeType) extends DIKey {
    override final def toString: String = s"{resource.${key.toString}/$tpe}"
  }

  final case class EffectKey(key: DIKey, tpe: SafeType) extends DIKey {
    override final def toString: String = s"{effect.${key.toString}/$tpe}"
  }

  /**
    * @param set       Key of the parent Set. `set.tpe` must be of type `Set[T]`
    * @param reference Key of `this` individual element. `reference.tpe` must be a subtype of `T`
    */
  final case class SetElementKey(set: DIKey, reference: DIKey, disambiguator: Option[ImplDef]) extends DIKey {
    override final def tpe: SafeType = reference.tpe

    override final def toString: String = s"{set.$set/${reference.toString}#${disambiguator.fold("0")(_.hashCode.toString)}"
  }

  final case class MultiSetImplId(set: DIKey, impl: ImplDef)
  object MultiSetImplId {
    implicit object SetImplIdContract extends IdContract[MultiSetImplId] {
      override def repr(v: MultiSetImplId): String = s"set/${v.set}#${v.impl.hashCode}"
    }
  }
}
