package de.innovationhub.prox;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

public class RedisContainer extends GenericContainer<RedisContainer> {

  public static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("redis");
  public static final String DEFAULT_TAG = "7.0";
  public static final int REDIS_PORT = 6379;

  @Deprecated
  public RedisContainer() {
    this(DEFAULT_IMAGE_NAME.withTag(DEFAULT_TAG));
  }

  public RedisContainer(final String dockerImageName) {
    this(DockerImageName.parse(dockerImageName));
  }

  public RedisContainer(final DockerImageName dockerImageName) {
    super(dockerImageName);

    withCopyFileToContainer(MountableFile.forClasspathResource("redis.conf"),
        "/data/redis.conf");
    withCommand("redis-server", "/data/redis.conf");

    withExposedPorts(REDIS_PORT);
    waitingFor(Wait.forLogMessage(".*Ready to accept connections.*\\n", 1));
  }

  public int getPort() {
    return getMappedPort(REDIS_PORT);
  }
}
