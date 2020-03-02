package izumi.distage.docker

import java.util.concurrent.TimeUnit

import scala.concurrent.duration.FiniteDuration

object Docker {
  final case class AvailablePort(hostV4: String, port: Int)
  object AvailablePort {
    def local(port: Int): AvailablePort = hostPort("127.0.0.1", port)
    def hostPort(host: String, port: Int): AvailablePort = AvailablePort(host, port)
  }

  final case class ServicePort(containerAddressesV4: Seq[String], listenOnV4: String, port: Int)

  final case class ContainerId(name: String) extends AnyVal

  trait DockerPort {
    def number: Int
    def protocol: String
    override def toString: String = s"$protocol:$number"
  }
  object DockerPort {
    final case class TCP(number: Int) extends DockerPort {
      override def protocol: String = "tcp"
    }
    final case class UDP(number: Int) extends DockerPort {
      override def protocol: String = "udp"
    }
  }

  /**
    * @param reuse    If true and [[ClientConfig#allowReuse]] is also true, keeps container alive after tests.
    *                 If false, the container will be shut down.
    *                 default: true
    */
  final case class ContainerConfig[T](
                                       image: String,
                                       ports: Seq[DockerPort],
                                       name: Option[String] = None,
                                       env: Map[String, String] = Map.empty,
                                       cmd: Seq[String] = Seq.empty,
                                       entrypoint: Seq[String] = Seq.empty,
                                       cwd: Option[String] = None,
                                       user: Option[String] = None,
                                       mounts: Seq[Mount] = Seq.empty,
                                       networks: Seq[ContainerNetwork] = Seq.empty,
                                       reuse: Boolean = true,
                                       healthCheckInterval: FiniteDuration = FiniteDuration(1, TimeUnit.SECONDS),
                                       pullTimeout: FiniteDuration = FiniteDuration(120, TimeUnit.SECONDS),
                                       healthCheck: ContainerHealthCheck[T] = ContainerHealthCheck.checkAllPorts[T],
                                       portProbeTimeout: FiniteDuration = FiniteDuration(200, TimeUnit.MILLISECONDS)
                                     )

  /**
    * @param allowReuse   If true and container's [[ContainerConfig#reuse]] is also true, keeps container alive after tests.
    *                     If false, the container will be shut down.
    */
  final case class ClientConfig(
                                 readTimeoutMs: Int,
                                 connectTimeoutMs: Int,
                                 allowReuse: Boolean,
                                 useRemote: Boolean,
                                 useRegistry: Boolean,
                                 remote: Option[RemoteDockerConfig],
                                 registry: Option[DockerRegistryConfig],
                               )

  final case class RemoteDockerConfig(host: String, tlsVerify: Boolean, certPath: String, config: String)

  final case class DockerRegistryConfig(url: String, username: String, password: String, email: String)

  sealed trait HealthCheckResult
  object HealthCheckResult {
    case object Unknown extends HealthCheckResult

    final case class Failed(t: Throwable) extends HealthCheckResult

    sealed trait Running extends HealthCheckResult
    case object JustRunning extends Running
    final case class WithPorts(availablePorts: Map[DockerPort, Seq[AvailablePort]]) extends Running
  }

  final case class Mount(host: String, container: String, noCopy: Boolean = false)

  final case class ContainerNetwork(name: String)

}
