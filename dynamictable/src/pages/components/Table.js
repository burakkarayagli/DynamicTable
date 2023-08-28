import React, { useEffect, useState } from "react";
import { Divider } from "@mui/material";
import ColumnCell from "./ColumnCell";
import EmptyAddRow from "./EmptyAddRow";
import DataRow from "./DataRow";
import axios from "axios";
import { EndpointConstants } from "@/constants/EndpointConstants";
import { StringConstants } from "@/constants/StringConstants";


function Table(props) {
    const { name, columns, value, index, id } = props;

    const handletableDataChange = (tableData) => {
        setRows((prevRows) =>
            prevRows.map((prevRow, index) => {
                if (index === prevRows.length - 1) {
                    return (
                        <DataRow
                            key={index}
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
        const { name, deleteTableCallback } = props;

        axios
            .delete(EndpointConstants.DELETE_TABLE_BY_NAME + name)
            .then((response) => {
                console.log(response.data);
                // Call the callback function to update the tables state in the parent component
                if (deleteTableCallback) {
                    deleteTableCallback(name);
                }
            });
    };

    function fetchTableData() {
        axios.get(EndpointConstants.GET_TABLE_BY_NAME + name).then((response) => {
            var data = response.data.tableData;
            //update rows with data without previous rows
            if (data.length > 0) {
                setRows(
                    data.map((row, index) => (
                        <DataRow
                            key={index}
                            cellCount={columns.length}
                            onSavetableData={handletableDataChange}
                            //remove the first element of the row because it is the row id
                            data={row}
                        />
                    ))
                );
            } else {
                setRows([
                    <DataRow
                        key={0}
                        cellCount={columns.length}
                        onSavetableData={handletableDataChange}
                        data={Array(columns.length).fill("")}
                    />,
                ]);
            }
        });
    }

    useEffect(() => {
        console.log("Before fetch table:", name, rows);
        fetchTableData();
        console.log("After fetch table:", name, rows);
    }, []);

    function getTableData() {
        var tableName = name;
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
        axios.post(EndpointConstants.SAVE_TABLE_DATA, data).then((response) => {
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
                {rows}
                <div className="grid-row">
                    <EmptyAddRow addRow={addRow} />
                </div>
            </div>
            <button onClick={savetableData} className="save-button">
                    {StringConstants.SAVE_BUTTON}
            </button>
            <Divider />
            <button onClick={handleDeleteClick}> Delete Table</button>
            <Divider />
            
        </div>
    );
}

export default Table;
