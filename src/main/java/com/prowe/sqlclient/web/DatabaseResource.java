package com.prowe.sqlclient.web;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.actuate.health.DataSourceHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.prowe.sqlclient.domain.DBConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@RestController
@RequestMapping(value = "/api/db", produces = MediaType.APPLICATION_JSON_VALUE)
public class DatabaseResource {
	
	@RequestMapping(value="/supported",
			method = RequestMethod.GET)
	public Map<String,String> getSupportedDatabases() {
		Map<String,String> supportedDatabases = new HashMap<String,String>();
		supportedDatabases.put("H2","org.h2.jdbcx.JdbcDataSource");
		supportedDatabases.put("MYSQL", "com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
		return supportedDatabases;
	}
	
	@RequestMapping(value="/test",
			method = RequestMethod.POST)
	public Health testDB(@RequestBody DBConfig dbconfig) {
		HikariConfig config = new HikariConfig();
        config.setDataSourceClassName(dbconfig.getClassName());
        config.addDataSourceProperty("url", dbconfig.getUrl());
        config.addDataSourceProperty("user", dbconfig.getUser());
        config.addDataSourceProperty("password", dbconfig.getPassword());
		DataSource ds = new HikariDataSource(config);
		DataSourceHealthIndicator databaseHealthIndicator = new DataSourceHealthIndicator(ds);
		return databaseHealthIndicator.health();
	}

}
