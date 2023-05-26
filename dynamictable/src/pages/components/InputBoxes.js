import RemoveIcon from '@mui/icons-material/Remove';
import PlusIcon from '@mui/icons-material/Add';
import * as ReactDOMServer from 'react-dom/server';
import Button from '@mui/material/Button';

function InputBoxes(props) {

    function addNewInput(e) {
        console.log(props);
        const columns = document.getElementById(props.type);
      
        // Create container for input and remove button
        const inputContainer = document.createElement('div');
        inputContainer.className = 'input-container flex';
      
        // Create input element
        const newInput = document.createElement('input');
        newInput.type = 'text';
        newInput.className = 'bg-transparent border border-gray-400 text-gray-900 rounded focus:ring-blue-500 focus:border-blue-500 block mt-2';
        inputContainer.appendChild(newInput);
        
        // Create remove button
        const removeButton = document.createElement('button');
        removeButton.innerHTML = ReactDOMServer.renderToString(<RemoveIcon/>);
        removeButton.className = 'text-red-500 mr-2 mt-2';
        removeButton.addEventListener('click', () => removeInput(inputContainer));
        //add remove button before the input element
        inputContainer.appendChild(removeButton);


      
        // Insert the input container before the last child
        columns.insertBefore(inputContainer, columns.lastChild);
      }
      
    function removeInput(inputContainer) {
    inputContainer.parentNode.removeChild(inputContainer);
    }

    return (
        <>
            <div id="input-container" className="flex">
                <input
                    type="text"
                    className="bg-transparent border border-gray-400 text-gray-900 rounded focus:ring-blue-500 focus:border-blue-500 block mt-2"
                />
            </div>
            <button onClick={addNewInput} className=' text-center flex bg-green-500 mt-2 ml-auto mr-auto rounded-full'>
                <PlusIcon className=' text-white'/>
            </button>
        </>
    );
}
export default InputBoxes;
