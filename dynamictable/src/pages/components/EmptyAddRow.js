import AddIcon from "@mui/icons-material/Add";

function EmptyAddRow(props) {
    return (
        <div className="empty-add-row"  >
            <div className="empty-add-row-header" onClick={props.addRow}>
                <h5>
                    <AddIcon/>
                </h5>
            </div>
        </div>
    );
}
export default EmptyAddRow;
