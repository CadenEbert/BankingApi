package com.java.bankapp;

import com.bank.app.AppMainApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

@SpringBootTest(classes = AppMainApplication.class)
public class H2DatabaseStructureTest {

    private final DataSource dataSource;

    @Autowired
    public H2DatabaseStructureTest(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Test
    void printCustomerTableStructure() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});

            boolean customerTableFound = false;

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                if ("CUSTOMER".equalsIgnoreCase(tableName)) {
                    customerTableFound = true;
                    System.out.println("\n===== CUSTOMER Table Structure =====");

                    ResultSet columns = metaData.getColumns(null, null, tableName, "%");
                    while (columns.next()) {
                        String columnName = columns.getString("COLUMN_NAME");
                        String columnType = columns.getString("TYPE_NAME");
                        String nullable = columns.getInt("NULLABLE") == 0 ? "NO" : "YES";
                        System.out.println("Column: " + columnName + ", Type: " + columnType + ", Nullable: " + nullable);
                    }

                    ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableName);
                    System.out.print("Primary Key(s): ");
                    while (primaryKeys.next()) {
                        System.out.print(primaryKeys.getString("COLUMN_NAME") + " ");
                    }
                    System.out.println("\n===== End of CUSTOMER Table Structure =====\n");
                    break;
                }
            }

            if (!customerTableFound) {
                System.out.println("CUSTOMER table not found in the database.");
            }
        }
    }
}
