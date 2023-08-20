import React, { useState, useEffect} from "react";

function DataRow(props) {
    const {data} = props;
    
    const [tableData, settableData] = useState(data);

    function handleCellChange(e, index) {
        const newtableData = [...tableData];
        newtableData[index] = e.target.value;
        settableData(newtableData);
    }

    const createCells = () => {
        return tableData.map((value, index) => (
            <input 
                type="text"
                className="data-cell-input"
                value={value ? value:" "}
                onChange={(e) => handleCellChange(e, index)}
            />
        ));
    };

    return <div className="data-row">{createCells()}</div>;
}
export default DataRow;
