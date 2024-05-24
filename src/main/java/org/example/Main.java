package org.example;

import java.net.InetSocketAddress;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;
import com.dtsx.astra.sdk.db.AstraDBOpsClient;


public class Main {
    public static void main(String[] args) {
        String token = System.getProperty("astraToken");
        String dbId = System.getProperty("dbId");
        new AstraDBOpsClient(token)
                .database(dbId)
                .downloadDefaultSecureConnectBundle("/tmp/secure-connect-dbId.zip");
        try(CqlSession session = CqlSession.builder()
                .withCloudSecureConnectBundle(Paths.get("/tmp/secure-connect-dbId.zip"))
                .withAuthCredentials("token", token)
                .withKeyspace("test_space")
                .build()) {
            ResultSet rs = session.execute(SimpleStatement.newInstance("select * from airport limit 1"));
            System.out.println(rs.one().getString("airport"));
        }
    }
}