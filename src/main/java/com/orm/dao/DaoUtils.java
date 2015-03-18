package com.orm.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import com.raddle.jdbc.JdbcTemplate;
import com.raddle.jdbc.callback.ConnectionCallback;
import com.raddle.jdbc.datasource.DriverManagerDataSource;
import com.raddle.jdbc.meta.table.TableInfo;
import com.raddle.jdbc.meta.table.TableMetaHelper;

public final class DaoUtils {

    private static Connection connection;
    
    private static JdbcTemplate jdbcTemplate;
    
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String DB_URL = "jdbc:oracle:thin:@192.168.140.203:1521:OKSTAGE";
    private static final String USERNAME = "ok_wallet";
    private static final String PASSWORD = "ok_wallet";
    
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        if(connection == null) {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        }
        return connection;
    }
    
    public static JdbcTemplate getJdbcTemplate() {
        if(jdbcTemplate == null) {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(DRIVER);
            dataSource.setUrl(DB_URL);
            dataSource.setUsername(USERNAME);
            dataSource.setPassword(PASSWORD);
            jdbcTemplate = new JdbcTemplate(dataSource);
        }
        return jdbcTemplate;
    }
    
    @SuppressWarnings("unchecked")
    public static List<TableInfo> getTableInfo(final String[] tableNames, final String schema) throws Exception {
        return (List<TableInfo>) getJdbcTemplate().execute(new ConnectionCallback() {
            public Object doInConnection(Connection connection) throws SQLException {
                TableMetaHelper metaHelper = new TableMetaHelper(connection);
                return metaHelper.getTableInfo(tableNames, schema, new String[] {"TABLE"});
            }
        });        
    }
    
//    public static List<TableInfo> getTableInfo(final String[] tableNames, final String schema) throws Exception {
//        DatabaseMetaData metaData = getConnection().getMetaData();
//        for(String tableName : tableNames) {
//            ResultSet rs = metaData.getTables(null, "OK_WALLET", "REFUND", new String[]{"TABLE"});
//        }
//        
//        while(rs.next()) {
//            String tableName = rs.getString("TABLE_NAME");
//            System.out.println(tableName);
//        }
//        return null;  
//    }
    
    
}
