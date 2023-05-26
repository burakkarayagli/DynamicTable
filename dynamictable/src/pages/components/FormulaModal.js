import CloseIcon from "@mui/icons-material/Close";
import InputBoxes from "./InputBoxes";
import Button from "@mui/material/Button";

function FormulaModal(props) {
    function readInputs() {
        const inputColumns = document.getElementById("input-columns");
        const inputs = inputColumns.childNodes;
        var input_columns = [];
        inputs.forEach((input) => {
            input_columns.push(input.value);
        });
        console.log(input_columns);
    }

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
                    <div class="w-72">
                        <div class="relative h-10 w-full min-w-[200px]">
                            <input
                                class="peer h-full w-full rounded-[7px] border border-black border-t-transparent bg-transparent px-3 py-2.5 font-sans text-sm font-normal text-blue-gray-700 outline outline-0 transition-all placeholder-shown:border placeholder-shown:border-black placeholder-shown:border-t-black focus:border-2 focus:border-gray-500 focus:border-t-transparent focus:outline-0 disabled:border-0 disabled:bg-blue-gray-50"
                                placeholder=" "
                            />
                            <label class="before:content[' '] after:content[' '] pointer-events-none absolute left-0 -top-1.5 flex h-full w-full select-none text-[11px] font-normal leading-tight text-blue-gray-400 transition-all before:pointer-events-none before:mt-[6.5px] before:mr-1 before:box-border before:block before:h-1.5 before:w-2.5 before:rounded-tl-md before:border-t before:border-l before:border-blue-gray-200 before:transition-all after:pointer-events-none after:mt-[6.5px] after:ml-1 after:box-border after:block after:h-1.5 after:w-2.5 after:flex-grow after:rounded-tr-md after:border-t after:border-r after:border-blue-gray-200 after:transition-all peer-placeholder-shown:text-sm peer-placeholder-shown:leading-[3.75] peer-placeholder-shown:text-blue-gray-500 peer-placeholder-shown:before:border-transparent peer-placeholder-shown:after:border-transparent peer-focus:text-[11px] peer-focus:leading-tight peer-focus:text-gray-500 peer-focus:before:border-t-2 peer-focus:before:border-l-2 peer-focus:before:border-gray-500 peer-focus:after:border-t-2 peer-focus:after:border-r-2 peer-focus:after:border-gray-500 peer-disabled:text-transparent peer-disabled:before:border-transparent peer-disabled:after:border-transparent peer-disabled:peer-placeholder-shown:text-blue-gray-500">
                                BFM Name
                            </label>
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
                        <h5>INPUT COLUMNS</h5>
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
                        <h5>OUTPUT COLUMNS</h5>
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
                    >
                        Create
                    </Button>
                    <Button
                        variant="contained"
                        color="error"
                        className="bg-red-600"
                    >
                        Cancel
                    </Button>
                </div>
            </div>
        </div>
    );
}

export default FormulaModal;
