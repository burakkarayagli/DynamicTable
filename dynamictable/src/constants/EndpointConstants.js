export const baseURL = "http://localhost:8080";
const api = "/api";

const endpoint_base = baseURL + api;

export const EndpointConstants = {
    GET_ALL_TABLES: endpoint_base + "/getAllTables",
    GET_TABLE_BY_NAME: endpoint_base + "/getTableByName/",
    CREATE_TABLE: endpoint_base + "/createTable",
    DELETE_TABLE_BY_NAME: endpoint_base + "/deleteTableByName/",
    SAVE_TABLE_DATA: endpoint_base + "/saveTable",
    IMPORT_FROM_EXCEL: endpoint_base + "/importFromExcel",
}
