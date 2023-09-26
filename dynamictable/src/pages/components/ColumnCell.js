function ColumnCell(props) {
    var isInput = props.isInput;
    var column = props.column;

    return (
        <div
            className="column-cell"
            style={{ backgroundColor: isInput ? "#a5c0e3" : "#dfb085" }}
        >
            <div className="column-cell-header">
                <h5
                    style={{
                        textAlign: "center",
                    }}
                >
                    {column}
                </h5>
            </div>
        </div>
    );
}
export default ColumnCell;
