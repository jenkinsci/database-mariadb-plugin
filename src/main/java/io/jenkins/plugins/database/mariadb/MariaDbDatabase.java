package io.jenkins.plugins.database.mariadb;

import hudson.Extension;
import hudson.util.Secret;
import org.jenkinsci.plugins.database.AbstractRemoteDatabase;
import org.jenkinsci.plugins.database.AbstractRemoteDatabaseDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

public class MariaDbDatabase extends AbstractRemoteDatabase {
    @DataBoundConstructor
    public MariaDbDatabase(String hostname, String database, String username, Secret password, String properties) {
        super(hostname, database, username, password, properties);
    }

    @Override
    protected Class<org.mariadb.jdbc.Driver> getDriverClass() {
        return org.mariadb.jdbc.Driver.class;
    }

    @Override
    protected String getJdbcUrl() {
        return "jdbc:mariadb://" + hostname + '/' + database;
    }

    @Extension
    public static class DescriptorImpl extends AbstractRemoteDatabaseDescriptor {

        @Override
        public String getDisplayName() {
            return "MariaDB";
        }
    }
}
