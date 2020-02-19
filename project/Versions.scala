import sbt.librarymanagement.CrossVersion
object V {
  // foundation
  val collection_compat = "2.1.4"

  val kind_projector = "0.11.0"
  val silencer = "1.4.4"

  val scalatest = "3.1.0"

  val cats = "2.1.0"
  val cats_effect = "2.1.1"
  val zio = "1.0.0-RC16"
  val zio_interop_cats = "2.0.0.0-RC7"

  val circe = "0.13.0"
  val circe_generic_extras = "0.13.0"
  val circe_derivation = "0.12.0-M7"
  val pureconfig = "0.12.2"
  val jawn = "1.0.0"

  val http4s = "0.21.1"

  // java-only dependencies below
  val classgraph = "4.8.64"
  val slf4j = "1.7.30"
  val typesafe_config = "1.4.0"

  // good to drop - java
  val cglib_nodep = "3.3.0"
  val scala_java_time = "2.0.0-RC3"
  val docker_java = "3.2.0-rc4"
}
