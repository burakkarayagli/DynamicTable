package com.example.dynamictablebackend;

import java.util.List;

public class TableInfo {
    private String tableName;
    private List<String> columnNames;

    public TableInfo(String tableName, List<String> columnNames) {
        this.tableName = tableName;
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
        return "TableInfo{" +
                "tableName='" + tableName + '\'' +
                ", columnNames=" + columnNames +
                '}';
    }
}

