function addIngredientField() {
    const divIngredients = document.getElementById('ingredientsReference');
    let htmlForm = divIngredients.cloneNode(true);
    document.getElementById('ingredientsContainer').appendChild(htmlForm);
}

function removeIngredientField(button) {
    const formContainer = document.getElementById('ingredientsContainer');
    //if(!(formContainer.childElementCount == 1)){
    if(formContainer.childElementCount > 1){
        formContainer.removeChild(button.parentElement);
    }
    else{
        alert("You must have at least one ingredient.");
    }
}


function addNewIngredient(select){
    if(select.value === 'add'){
        const input = document.createElement('input');
        input.type = 'text';
        input.name = 'ingredientsName';
        //input.id = 'unitInput';
        input.placeholder = 'Inserisci nuovo ingrediente';

        // Replace the select with the input
        //select.parentElement.replaceChild(input, select);

        //crea bottone per tornare alla lista a cascata
        const backButton = document.createElement('button');
        backButton.type = 'button';
        backButton.id = 'backButton';
        backButton.textContent = 'Indietro';
        backButton.onclick = function() {
            changeToSelect(input, select);
        };
        //input.parentElement.appendChild(backButton);
        const parent = select.parentElement;
        parent.replaceChild(input, select);
        parent.appendChild(backButton);
    }
}



function addNewUnit(select){
    if(select.value === 'add'){
        const input = document.createElement('input');
        input.type = 'text';
        input.name = 'unitsName';
        //input.id = 'unitInput';
        input.placeholder = 'Inserisci nuova unità';

        const backButton = document.createElement('button');
        backButton.type = 'button';
        backButton.id = 'backButton';
        backButton.textContent = 'Indietro';
        backButton.onclick = function() {
            changeToSelect(input, select);
        };
        //input.parentElement.appendChild(backButton);
        const parent = select.parentElement;
        parent.replaceChild(input, select);
        parent.appendChild(backButton);

        

        // Replace the select with the input
        //select.parentElement.replaceChild(input, select);
    }
}


function changeToSelect(input, select) {
    const parent = input.parentElement;
    parent.replaceChild(select, input);
    const backButton = document.getElementById('backButton');
    if (backButton) {
        parent.removeChild(backButton);
    }
    select.value = ''; // Reset the select value
}

function updateRegistrationForm(radioInput){
    var listClass = document.getElementsByClassName("inputRegistration");
    if(radioInput.value === 'CUSTOMER'){
        for(i=0; i< listClass.length;i++){
            listClass[i].required = false;
        }
    }
    else if(radioInput.value === 'CHEF'){
        for(i=0; i< listClass.length;i++){
            listClass[i].required = true;
        }
    }
    /*else if(radioInput.value === 'ADMINISTRATOR'){
        for(i=0; i< listClass.length;i++){
            listClass[i].required = false;
        }
    }*/
}

function removeImageFromChef(divImage){

    while (divImage.firstChild) {
        divImage.removeChild(divImage.firstChild);
    }

    const inputHidden = document.createElement('input');
    inputHidden.type = 'hidden';
    inputHidden.name = 'removeImage';
    inputHidden.value = 'true';

    const inputIMG = document.createElement('input');
    inputIMG.type = 'file';
    inputIMG.name = 'image';
    inputIMG.required = true;

    divImage.appendChild(inputHidden);
    divImage.appendChild(inputIMG);

}

function removeImageFromRecipe(button, index){

    const inputHidden = document.createElement('input');
    inputHidden.type = 'hidden';
    inputHidden.name = 'removeImageIndexes';
    inputHidden.value = index;
    button.parentElement.parentElement.appendChild(inputHidden);

    button.parentElement.remove();
}
    
            /*
            //ascolta solo la prima riga perché hanno tutti lo stesso nome della classe
            document.addEventListener('DOMContentLoaded', (event) => {
                const unitSelect = document.getElementById('unitSelect');
                const addUnitOption = document.getElementById('addUnitOption');
    
                unitSelect.addEventListener('change', function() {
                    if (this.value === 'add') {
                        const input = document.createElement('input');
                        input.type = 'text';
                        input.name = 'unitsName';
                        //input.id = 'unitInput';
                        input.placeholder = 'Inserisci nuova unità';
    
                        // Replace the select with the input
                        this.parentNode.replaceChild(input, this);
    
                        // Optionally, focus the new input field
                        //input.focus();
                    }
                });
    
                const ingredientSelect = document.getElementById('ingredientSelect');
                const addIngredientOption = document.getElementById('addIngredientOption');
    
                ingredientSelect.addEventListener('change', function() {
                    if (this.value === 'add') {
                        const input = document.createElement('input');
                        input.type = 'text';
                        input.name = 'ingredientsName';
                        //input.id = 'ingredientInput';
                        input.placeholder = 'Inserisci nuovo ingrediente';
                        //input.required = true;
                        
                        this.parentNode.replaceChild(input, this);
                    }
                });
    
    
            });
            */
    
            /*
            function newUnit(newUnit){
                //const unitSelect = document.getElementById('unitSelect');
                const input = document.createElement('input');
                input.type = 'text';
                input.name = 'unit';
                input.id = 'unitInput';
                input.placeholder = 'Inserisci nuova unità';
    
                newUnit.parentNode.replaceChild(input, newUnit);
    
                input.focus();
            }
            */
    



/*
document.addEventListener('DOMContentLoaded', (event) => {
    const unitSelect = document.getElementById('unitSelect');
    const addUnitOption = document.getElementById('addUnitOption');

    unitSelect.addEventListener('change', function() {
        if (this.value === 'aggiungi') {
            const input = document.createElement('input');
            input.type = 'text';
            input.name = 'unit';
            input.id = 'unitInput';
            input.placeholder = 'Inserisci nuova unità';

            // Replace the select with the input
            this.parentNode.replaceChild(input, this);

            // Optionally, focus the new input field
            input.focus();
        }
    });
});



var ingredientCount = 1;

        function addIngredient() {
            var container = document.getElementById("ingredients");

            var newDiv = document.createElement("div");
            newDiv.className = "ingredient";

            var nameLabel = document.createElement("label");
            nameLabel.setAttribute("for", "ingredient" + ingredientCount + "Name");
            nameLabel.textContent = "Ingredient Name:";
            newDiv.appendChild(nameLabel);

            var nameInput = document.createElement("input");
            nameInput.type = "text";
            nameInput.id = "ingredient" + ingredientCount + "Name";
            nameInput.name = "ingredient" + ingredientCount + "Name";
            newDiv.appendChild(nameInput);

            var amountLabel = document.createElement("label");
            amountLabel.setAttribute("for", "ingredient" + ingredientCount + "Amount");
            amountLabel.textContent = "Amount:";
            newDiv.appendChild(amountLabel);

            var amountInput = document.createElement("input");
            amountInput.type = "number";
            amountInput.id = "ingredient" + ingredientCount + "Amount";
            amountInput.name = "ingredient" + ingredientCount + "Amount";
            newDiv.appendChild(amountInput);

            /*
            var unitLabel = document.createElement("label");
            unitLabel.setAttribute("for", "ingredient" + ingredientCount + "Unit");
            unitLabel.textContent = "Unit:";
            newDiv.appendChild(unitLabel);

            var unitSelect = document.createElement("select");
            unitSelect.id = "ingredient" + ingredientCount + "Unit";
            unitSelect.name = "ingredient" + ingredientCount + "Unit";
            */
/*
            var unitSelect = document.createElement("select");
            unitSelect.setAttribute("for", "ingredient" + ingredientCount + "Unit");
            unitSelect.textContent = "Unit:";
            newDiv.appendChild(unitSelect);

            var unitOption = document.createElement("option");
            unitOption.id = "ingredient" + ingredientCount + "Unit";
            unitOption.name = "ingredient" + ingredientCount + "Unit";

            /* Assuming units are passed to the view as a list of strings (unit names) */
            /* This part may vary depending on how the units are passed to the view */
 //           var units = /*[[${units}]];*/ [];
 /*           units.forEach(function(unit) {
                var option = document.createElement("option");
                option.value = unit;
                option.textContent = unit;
                unitSelect.appendChild(option);
            });

            newDiv.appendChild(unitSelect);

            container.appendChild(newDiv);

            ingredientCount++;
        }

        function addUnit() {
            var UnitInput = document.createElement("input");
            UnitInput.type = "text";
            newDiv.appendChild(UnitInput);
        }
        */