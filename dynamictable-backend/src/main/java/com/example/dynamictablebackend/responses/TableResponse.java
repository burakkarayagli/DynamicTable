package com.example.dynamictablebackend.responses;

import java.util.List;

public class TableResponse {
    private String tableName;
    private List<String> columnNames;
    private List<List<String>> tableData;

    public TableResponse(String tableName, List<String> columnNames, List<List<String>> tableData) {
        this.tableName = tableName;
        this.columnNames = columnNames;
        this.tableData = tableData;
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

    public List<List<String>> getTableData() {
        return tableData;
    }

    public void setTableData(List<List<String>> tableData) {
        this.tableData = tableData;
    }
}
