package io.jenkins.plugins.database.mariadb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import hudson.util.Secret;
import java.io.IOException;
import org.jenkinsci.plugins.database.GlobalDatabaseConfiguration;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@WithJenkins
@Testcontainers(disabledWithoutDocker = true)
public class MariaDbDatabaseTest {

    public static final String TEST_IMAGE = "mariadb:11.2.2";

    @Container
    private static final MariaDBContainer<?> mariadb = new MariaDBContainer<>(TEST_IMAGE);

    public void setConfiguration() throws IOException {
        MariaDbDatabase database = new MariaDbDatabase(
                mariadb.getHost() + ":" + mariadb.getMappedPort(3306),
                mariadb.getDatabaseName(),
                mariadb.getUsername(),
                Secret.fromString(mariadb.getPassword()),
                null);
        database.setValidationQuery("SELECT 1");
        GlobalDatabaseConfiguration.get().setDatabase(database);
    }

    @Test
    public void shouldSetConfiguration(JenkinsRule j) throws IOException {
        setConfiguration();
        assertThat(GlobalDatabaseConfiguration.get().getDatabase(), instanceOf(MariaDbDatabase.class));
    }

    @Test
    public void shouldConstructDatabase(JenkinsRule j) throws IOException {
        MariaDbDatabase database = new MariaDbDatabase(
                mariadb.getHost() + ":" + mariadb.getMappedPort(3306),
                mariadb.getDatabaseName(),
                mariadb.getUsername(),
                Secret.fromString(mariadb.getPassword()),
                null);
        assertThat(database.getDescriptor().getDisplayName(), is("MariaDB"));
        assertThat(
                database.getJdbcUrl(),
                is("jdbc:mariadb://" + mariadb.getHost() + ":" + mariadb.getMappedPort(3306) + "/"
                        + mariadb.getDatabaseName() + ""));
        assertThat(database.getDriverClass(), is(org.mariadb.jdbc.Driver.class));
    }
}
