import React, { useEffect, useState } from "react";
import { Divider } from "@mui/material";
import ColumnCell from "./ColumnCell";
import EmptyAddRow from "./EmptyAddRow";
import DataRow from "./DataRow";
import axios from "axios";

const baseURL = "http://localhost:8080";

function BfmTable(props) {
    const { bfm_name, columns, value, index, id } = props;
    console.log("this is table columns:", columns);
    const handletableDataChange = (tableData) => {
        // Assuming that each row has a unique identifier (e.g., ID) that you can use to identify rows in a more sophisticated app
        // For this example, I'm using the index of the row in the `rows` state array as a makeshift ID
        setRows((prevRows) =>
            prevRows.map((prevRow, index) => {
                if (index === prevRows.length - 1) {
                    // If it's the last row (the row with the new changes), return the updated row data
                    return (
                        <DataRow
                            cellCount={columns.length}
                            onSavetableData={handletableDataChange}
                        />
                    );
                } else {
                    // If it's not the last row, return the previous row
                    return prevRow;
                }
            })
        );
        // You can also do any additional logic here to save the row data to a database or perform other actions.
        // For now, I'm just printing the updated data to the console as an example.
    };

    const [rows, setRows] = useState([]);

    function addRow() {
        setRows((prevRows) => [
            ...prevRows,
            <DataRow
                cellCount={columns.length}
                onSavetableData={handletableDataChange}
                data={Array(columns.length).fill("")}
            />,
        ]);
    }

    const handleDeleteClick = () => {
        const { bfm_name, deleteTableCallback } = props;

        axios
            .delete(baseURL + "/api/deleteTableByName/" + bfm_name)
            .then((response) => {
                console.log(response.data);
                // Call the callback function to update the tables state in the parent component
                if (deleteTableCallback) {
                    deleteTableCallback(bfm_name);
                }
            });
    };

    function fetchTableData() {
        axios
            .get(baseURL + "/api/getTableByName/" + bfm_name)
            .then((response) => {
                var data = response.data.tableData;
                //update rows with data without previous rows
                setRows(
                    data.map((row, index) => (

                        <DataRow
                            cellCount={columns.length}
                            onSavetableData={handletableDataChange}
                            //remove the first element of the row because it is the row id
                            data={row.slice(1)}
                        />
                    ))
                );
            });
    }

    useEffect(() => {
        fetchTableData();
    }, []);

    function getTableData() {
        var tableName = bfm_name;
        var tableData = [];
        var rowContainer = document.getElementById(id);
        var rows = rowContainer.getElementsByClassName("data-row");
        for (var i = 0; i < rows.length; i++) {
            var row = rows[i];
            var cells = row.getElementsByClassName("data-cell-input");
            var cellData = [];
            for (var j = 0; j < cells.length; j++) {
                var cell = cells[j];
                cellData.push(columns[j] + ": " + cell.value);
            }
            tableData.push(cellData);
        }

        return {
            tableName: tableName,
            tableData: tableData,
        };
    }

    function savetableData() {
        var data = getTableData();
        console.log(data);
        axios.post(baseURL + "/api/saveTable", data).then((response) => {
            console.log(response.data);
        });
    }

    return (
        <div hidden={value !== index}>
            <div className="row-container" id={id}>
                <div className="grid-row">
                    {
                        //If column starts with in_ then it is an input column
                        //If column starts with out_ then it is an output column
                        columns.map((column, index) =>
                        //column.startswith but ignore case

                            column.toLowerCase().startsWith("in_") ? (
                                <ColumnCell
                                    key={index}
                                    column={(column = column.substring(3))}
                                    isInput={true}
                                />
                            ) : (
                                <ColumnCell
                                    key={index}
                                    column={(column = column.substring(4))}
                                />
                            )
                        )
                    }
                </div>
                {rows.length === 0 ? (
                        <DataRow
                            cellCount={columns.length}
                            onSavetableData={handletableDataChange}
                            data={Array(columns.length).fill("")}
                        />
                ) : (
                    rows
                )}
                <div className="grid-row">
                    <EmptyAddRow addRow={addRow} />
                </div>
            </div>
            <button onClick={savetableData}>Get Row Data</button>
            <Divider />
            <button
                onClick={handleDeleteClick}
                style={{ backgroundColor: "red" }}
            >
                {" "}
                Delete Table
            </button>
        </div>
    );
}

export default BfmTable;
