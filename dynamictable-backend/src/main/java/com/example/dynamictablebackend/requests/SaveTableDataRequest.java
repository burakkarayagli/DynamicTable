package com.example.dynamictablebackend.requests;
import java.util.List;

public class SaveTableDataRequest {
    private String tableName;
    private List<List<String>> tableData;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<List<String>> getTableData() {
        return tableData;
    }

    public void setTableData(List<List<String>> tableData) {
        this.tableData = tableData;
    }
}
