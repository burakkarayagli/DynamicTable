import CloseIcon from "@mui/icons-material/Close";
import InputBoxes from "./InputBoxes";
import Button from "@mui/material/Button";
import { TextField } from "@mui/material";
import { StringConstants } from "@/constants/StringConstants";

function FormulaModal(props) {
    return (
        <div
            className="fixed inset-0 bg-black bg-opacity-25 backdrop-opacity-10 flex justify-center items-center"
            id="formula-modal"
            onClick={props.closeModal}
        >
            <div
                className="w-[960px] h-[540px] bg-gray-200 p-2 rounded flex flex-col justify-between"
                id="modal-window"
            >
                <div className="p-4 flex justify-between">
                    <div className="w-72">
                        <div className="relative h-10 w-full min-w-[200px]">
                            <TextField id="bfm-name" label={StringConstants.TABLE_NAME} variant= "outlined"/>
                        </div>
                    </div>
                    <button
                        id="close-button"
                        className="text-gray place-self-end justify-end inline"
                        onClick={props.closeModal}
                    >
                        <CloseIcon />
                    </button>
                </div>
                <div className="flex justify-around" id="modal-columns">
                    <div
                        id="input-columns-header"
                        className="h-[355px] text-center bg-gray-100 shadow-md shadow-gray-500 rounded-xl p-3"
                    >
                        <h5>{StringConstants.INPUT_COLUMNS}</h5>
                        <div
                            id="input-columns"
                            className="flex-col flex justify-start"
                        >
                            <InputBoxes type={"input-columns"} />
                        </div>
                    </div>
                    <div
                        id="output-columns-header"
                        className="h-[355px] text-center bg-gray-100 shadow-md shadow-gray-500 rounded-xl p-3"
                    >
                        <h5>{StringConstants.OUTPUT_COLUMNS}</h5>
                        <div
                            id="output-columns"
                            className="flex-col flex justify-start"
                        >
                            <InputBoxes type={"output-columns"} />
                        </div>
                    </div>
                </div>
                <div className="flex justify-end">
                    <Button
                        variant="contained"
                        color="success"
                        className="bg-green-600 mr-2"
                        onClick={props.createButton}
                    >
                        {StringConstants.FORMULA_MODAL_CREATE_BUTTON}
                    </Button>
                    <Button
                        id = "cancel-button"
                        variant="contained"
                        color="error"
                        className="bg-red-600"
                    >
                        {StringConstants.FORMULA_MODAL_CANCEL_BUTTON}
                    </Button>
                </div>
            </div>
        </div>
    );
}

export default FormulaModal;
