import React, { useState } from "react";

function DataRow(props) {
    var cellCount = props.cellCount;

    const [rowData, setRowData] = useState(new Array(cellCount).fill(""));
    
    function handleCellChange(e, index) {
        const newRowData = [...rowData];
        newRowData[index] = e.target.value;
        setRowData(newRowData);
    }

    const createCells = () => {
        return rowData.map((value, index) => (
            <input 
                key={index}
                type="text"
                className="data-cell-input"
                value={value}
                onChange={(e) => handleCellChange(e, index)}
            />
        ));
    };

    return <div className="data-row">{createCells()}</div>;
}
export default DataRow;
