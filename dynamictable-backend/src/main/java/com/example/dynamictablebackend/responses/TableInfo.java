package com.example.dynamictablebackend.responses;

import java.util.List;

public class TableInfo {
    private String tableName;
    private List<String> columnNames;

    public TableInfo(String tableName, List<String> columnNames) {
        this.tableName = tableName;
        //Remove the first column name, which is the id
        this.columnNames = columnNames;
    }

    // Getters and setters
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    @Override
    public String toString() {
        return "{" +
                "tableName='" + tableName + '\'' +
                ", columnNames=" + columnNames +
                '}';
    }
}

