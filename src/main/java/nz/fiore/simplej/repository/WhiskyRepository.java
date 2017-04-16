package nz.fiore.simplej.repository;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;

/**
 * Created by fiorenzo on 16/04/17.
 */
public class WhiskyRepository {

    static String TABLE_NAME = "whiskies";
    public JDBCClient jdbcClient;
    protected Logger logger = LoggerFactory.getLogger(getClass());


    public WhiskyRepository() {
    }

    public WhiskyRepository(Vertx vertx, JsonObject config) {
        this.jdbcClient = JDBCClient.createShared(vertx, new JsonObject()
                .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
                .put("driver_class", "org.hsqldb.jdbcDriver")
                .put("max_pool_size", 30));
    }


    public void init(Future<Void> future) {
        System.out.println("init");

        jdbcClient.getConnection(connection -> {
            connection.result()
                    .execute("create table IF NOT EXISTS whiskies" +
                                    " (uuid varchar(255), " +
                                    " name varchar(255)," +
                                    " date datetime NULL," +
                                    " amount decimal(19,4))",
                            future.completer());
        });
    }


    public void persist(JsonObject params, Handler<AsyncResult<UpdateResult>> handler) {
        jdbcClient.getConnection(connection -> {
            if (connection.succeeded()) {
                persist(TABLE_NAME, params, connection.result(), handler);
            } else {
                handler.handle(Future.failedFuture(connection.cause()));
            }
            connection.result().close();
        });
    }

    public void merge(JsonObject params, JsonObject key, Handler<AsyncResult<UpdateResult>> handler) {
        jdbcClient.getConnection(connection -> {
            if (connection.succeeded()) {
                merge(TABLE_NAME, params, key, connection.result(), handler);
            } else {
                handler.handle(Future.failedFuture(connection.cause()));
            }
            connection.result().close();
        });
    }

    public void delete(JsonObject key, Handler<AsyncResult<UpdateResult>> handler) {
        jdbcClient.getConnection(connection -> {
            if (connection.succeeded()) {
                delete(TABLE_NAME, key, connection.result(), handler);
            } else {
                handler.handle(Future.failedFuture(connection.cause()));
            }
            connection.result().close();
        });
    }

    public void list(Handler<AsyncResult<ResultSet>> handler) {
        jdbcClient.getConnection(connection -> {
            if (connection.succeeded()) {
                connection.result().query("select * from " + TABLE_NAME, handler);
            } else {
                handler.handle(Future.failedFuture(connection.cause()));
            }
            connection.result().close();
        });
    }

    public void persist(String tableName, JsonObject params, SQLConnection
            sqlConnection, Handler<AsyncResult<UpdateResult>> handler) {
        logger.info("persist " + tableName + ", params: " + params.toString());
        sqlConnection.persist(tableName, params, handler);
    }

    public void merge(String table, JsonObject params, JsonObject key, SQLConnection
            sqlConnection, Handler<AsyncResult<UpdateResult>> handler) {
        logger.info("merge " + table + ", params: " + params.toString() + ", key: " + key);
        sqlConnection.merge(table, params, key, handler);
    }

    public void delete(String table, JsonObject key, SQLConnection
            sqlConnection, Handler<AsyncResult<UpdateResult>> handler) {
        logger.info("delete " + table + ", key: " + key);
        sqlConnection.delete(table, key, handler);
    }


}
