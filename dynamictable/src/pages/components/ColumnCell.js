function ColumnCell(props) {
    var isInput = props.isInput;
    var column = props.column;

    return (
        <div
            className="column-cell"
            style={{ backgroundColor: isInput ? "#10B981" : "#EF4444" }}
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
