import izumi.functional.bio.{BIOMonadAsk, SyncSafe2, SyncSafe3}
import izumi.logstage.api.logger.AbstractLogger
import izumi.logstage.{api, sink}
import logstage.strict.LogIOStrict

package object logstage extends LogStage {
  override type IzLogger = api.IzLogger
  override final val IzLogger: api.IzLogger.type = api.IzLogger

  override type ConsoleSink = sink.ConsoleSink
  override final val ConsoleSink: sink.ConsoleSink.type = sink.ConsoleSink

  override type QueueingSink = sink.QueueingSink
  override final val QueueingSink: sink.QueueingSink.type = sink.QueueingSink

  override type ConfigurableLogRouter = api.routing.ConfigurableLogRouter
  override final val ConfigurableLogRouter: api.routing.ConfigurableLogRouter.type = api.routing.ConfigurableLogRouter

  override type LogRouter = api.logger.LogRouter
  override val LogRouter: api.logger.LogRouter.type = api.logger.LogRouter

  override type StaticLogRouter = api.routing.StaticLogRouter
  override val StaticLogRouter: api.routing.StaticLogRouter.type = api.routing.StaticLogRouter

  override type Log = api.Log.type
  override final val Log: api.Log.type = api.Log

  override type Level = api.Log.Level
  override final val Level: api.Log.Level.type = api.Log.Level

  override final val Trace: api.Log.Level.Trace.type = api.Log.Level.Trace
  override final val Debug: api.Log.Level.Debug.type = api.Log.Level.Debug
  override final val Info: api.Log.Level.Info.type = api.Log.Level.Info
  override final val Warn: api.Log.Level.Warn.type = api.Log.Level.Warn
  override final val Error: api.Log.Level.Error.type = api.Log.Level.Error
  override final val Crit: api.Log.Level.Crit.type = api.Log.Level.Crit

  type LogBIO[F[_, _]] = LogIO[F[Nothing, ?]]
  object LogBIO {
    def apply[F[_, _]: LogBIO]: LogBIO[F] = implicitly

    def fromLogger[F[_, _]: SyncSafe2](logger: AbstractLogger): LogBIO[F] = {
      LogIO.fromLogger(logger)
    }
  }
  type LogBIOStrict[F[_, _]] = LogIOStrict[F[Nothing, ?]]

  type LogCreateBIO[F[_, _]] = LogCreateIO[F[Nothing, ?]]
  object LogCreateBIO {
    def apply[F[_, _]: LogCreateBIO]: LogCreateBIO[F] = implicitly
  }

  type UnsafeLogBIO[F[_, _]] = UnsafeLogIO[F[Nothing, ?]]
  object UnsafeLogBIO {
    def apply[F[_, _]: UnsafeLogBIO]: UnsafeLogBIO[F] = implicitly

    def fromLogger[F[_, _]: SyncSafe2](logger: AbstractLogger): UnsafeLogBIO[F] = {
      UnsafeLogIO.fromLogger(logger)
    }
  }

  type LogBIO3[F[_, _, _]] = LogIO[F[Any, Nothing, ?]]
  object LogBIO3 {
    def apply[F[_, _, _]: LogBIO3]: LogBIO3[F] = implicitly

    def fromLogger[F[_, _, _]: SyncSafe3](logger: AbstractLogger): LogBIO3[F] = {
      LogIO.fromLogger(logger)
    }

    /** Lets you carry LogBIO capability in environment */
    def log[F[-_, +_, +_]: BIOMonadAsk: zio.TaggedF3] = new LogBIO3EnvInstance[F](_.get)
  }
  type LogBIOStrict3[F[_, _, _]] = LogIOStrict[F[Any, Nothing, ?]]

  type LogCreateBIO3[F[_, _, _]] = LogCreateIO[F[Any, Nothing, ?]]
  object LogCreateBIO3 {
    def apply[F[_, _, _]: LogCreateBIO3]: LogCreateBIO3[F] = implicitly
  }

  type UnsafeLogBIO3[F[_, _, _]] = UnsafeLogIO[F[Any, Nothing, ?]]
  object UnsafeLogBIO3 {
    def apply[F[_, _, _]: UnsafeLogBIO3]: UnsafeLogBIO3[F] = implicitly

    def fromLogger[F[_, _, _]: SyncSafe3](logger: AbstractLogger): UnsafeLogBIO3[F] = {
      UnsafeLogIO.fromLogger(logger)
    }
  }
}
