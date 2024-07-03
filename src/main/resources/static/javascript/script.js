function addIngredientField() {
    const divIngredients = document.getElementById('ingredientsReference');
    let htmlForm = divIngredients.cloneNode(true);
    document.getElementById('ingredientsContainer').appendChild(htmlForm);
}

function removeIngredientField(button) {
    const formContainer = document.getElementById('ingredientsContainer');
    if(formContainer.childElementCount > 1){
        formContainer.removeChild(button.parentElement);
    }
    else{
        alert("È necessario avere almeno un ingrediente.");
    }
}


function addNewIngredient(select){
    if(select.value === 'add'){
        const input = document.createElement('input');
        input.type = 'text';
        input.name = 'ingredientsName';
        //input.id = 'unitInput';
        input.className = 'form-control d-inline-block';
        input.style = 'width: auto;';
        input.placeholder = 'Inserisci nuovo ingrediente';

        //crea bottone per tornare alla lista a cascata
        const backButton = document.createElement('button');
        backButton.type = 'button';
        backButton.id = 'backButtonIngredient';
        backButton.className = 'btn btn-secondary btn-sm';
        backButton.textContent = 'Indietro';
        backButton.onclick = function() {
            changeToSelect(input, select, 'ingredient');
        };
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
        input.className = 'form-control d-inline-block';
        input.style = 'width: auto;';
        input.placeholder = 'Inserisci nuova unità';

        const backButton = document.createElement('button');
        backButton.type = 'button';
        backButton.id = 'backButtonUnit';
        backButton.className = 'btn btn-secondary btn-sm';
        backButton.textContent = 'Indietro';
        backButton.onclick = function() {
            changeToSelect(input, select,'unit');
        };

        const parent = select.parentElement;
        parent.replaceChild(input, select);
        parent.appendChild(backButton);

    }
}


function changeToSelect(input, select, field) {
    const parent = input.parentElement;
    parent.replaceChild(select, input);
    var backButton;
    if(field == 'ingredient'){
        backButton = document.getElementById('backButtonIngredient');
    }else if(field == 'unit'){
        backButton = document.getElementById('backButtonUnit');
    }

    if (backButton) {
        parent.removeChild(backButton);
    }
    select.value = ''; // Resetta il valore del select
}

function updateRegistrationForm(radioInput){
    
    var listClass = document.getElementsByClassName("inputRegistration");
    
    var registrationButton = document.getElementById('registrationButton');

    var chooseRoleRegistration = document.getElementById('chooseRoleRegistration');

    const form = radioInput.parentElement.parentElement.parentElement;
    var loginLink = document.getElementById('loginLink');

    if(radioInput.value === 'CUSTOMER'){
        for(i=0; i< listClass.length;i++){
            listClass[i].required = false;
        }
        form.removeChild(document.getElementById('inputImageRegistration'));
        form.removeChild(chooseRoleRegistration);
        form.removeChild(registrationButton);
        form.removeChild(loginLink);

        form.appendChild(chooseRoleRegistration);
        form.appendChild(registrationButton);
        form.appendChild(loginLink);
    }
    else if(radioInput.value === 'CHEF'){
        for(i=0; i< listClass.length;i++){
            listClass[i].required = true;
        }
        if(document.getElementById('inputImageRegistration') == null){
            form.removeChild(chooseRoleRegistration);
            form.removeChild(registrationButton);
            form.removeChild(loginLink);

            const divImage = document.createElement('div');

            divImage.id = 'inputImageRegistration';
            divImage.className = 'mb-3';
            
            const labelImage = document.createElement('label');
            labelImage.for = 'image';
            labelImage.className = 'text-white';
            labelImage.textContent = 'Foto:';

            const inputImage = document.createElement('input');
            inputImage.type = 'file';
            inputImage.id = 'image';
            inputImage.name = 'image';
            //inputImage.className = 'form-control-file';
            inputImage.required = true;

            divImage.appendChild(labelImage);
            divImage.appendChild(inputImage);

            
            form.enctype = 'multipart/form-data';

            form.appendChild(divImage);
            form.appendChild(chooseRoleRegistration);
            form.appendChild(registrationButton);
            form.appendChild(loginLink);
        }
    }
    /*
    else if(radioInput.value === 'ADMINISTRATOR'){
        for(i=0; i< listClass.length;i++){
            listClass[i].required = false;
        }
    }
    */
}

function updateCreationForm(radioInput){
    var listClass = document.getElementsByClassName('inputCreation');

    var creationButton = document.getElementById('creationButton');

    var chooseRoleCreation = document.getElementById('chooseRoleCreation');

    const form = radioInput.parentElement.parentElement.parentElement;

    if(radioInput.value === 'CUSTOMER'){
        for(i=0; i< listClass.length;i++){
            listClass[i].required = false;
        }
        form.removeChild(document.getElementById('inputImageCreation'));
        form.removeChild(chooseRoleCreation);
        form.removeChild(creationButton);
        form.appendChild(chooseRoleCreation);
        form.appendChild(creationButton);
    }
    else if(radioInput.value === 'CHEF'){
        for(i=0; i< listClass.length;i++){
            listClass[i].required = true;
        }
        if(document.getElementById('inputImageCreation') == null){
            form.removeChild(creationButton);
            form.removeChild(chooseRoleCreation);

            const divImage = document.createElement('div');

            divImage.id = 'inputImageCreation';
            
            const labelImage = document.createElement('label');
            labelImage.for = 'image';
            labelImage.textContent = 'Select Image:';

            const inputImage = document.createElement('input');
            inputImage.type = 'file';
            inputImage.id = 'image';
            inputImage.name = 'image';
            inputImage.required = true;

            divImage.appendChild(labelImage);
            divImage.appendChild(inputImage);

            
            form.enctype = 'multipart/form-data';

            form.appendChild(divImage);
            form.appendChild(chooseRoleCreation);
            form.appendChild(creationButton);
        }
    }
    else if(radioInput.value === 'ADMINISTRATOR'){
        for(i=0; i< listClass.length;i++){
            listClass[i].required = false;
        }
        
        form.removeChild(document.getElementById('inputImageCreation'));
        form.removeChild(chooseRoleCreation);
        form.removeChild(creationButton);
        form.appendChild(chooseRoleCreation);
        form.appendChild(creationButton);
    }
}

function removeImageFromChef(divImage){

    const form = divImage.parentElement;
    form.enctype = 'multipart/form-data';

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

    button.parentElement.parentElement.parentElement.parentElement.parentElement.parentElement.appendChild(inputHidden);

    const carouselItem = button.closest('.carousel-item' + index);
    carouselItem.remove();

    // Rimuovi il corrispondente indicatore
    const indicator = document.querySelector(`.carousel-indicators [data-slide-to="${index}"]`);
    if (indicator) {
        indicator.remove();
    }

    // Trova il carosello e i suoi elementi
    const carousel = document.querySelector('#imageCarousel .carousel-inner');
    const items = carousel.querySelectorAll('.carousel-item');

    // Se ci sono elementi rimasti nel carosello
    if (items.length > 0) {
        // Se l'elemento rimosso era l'elemento attivo
        if (carouselItem.classList.contains('active')) {
            // Rendi attivo il primo elemento rimasto
            items[0].classList.add('active');
        }
    } else {
        // Se non ci sono elementi rimasti, disabilita i controlli del carosello
        document.querySelector('.carousel-control-prev').style.display = 'none';
        document.querySelector('.carousel-control-next').style.display = 'none';
    }
}

function changePasswordClick(divInputPassword){ 
    const divPassword = divInputPassword.parentElement;
    divPassword.lastElementChild.value = 'true';
    let changePasswordBox = document.getElementById('password');
    changePasswordBox.type = 'password';
    changePasswordBox.className = 'form-control';
    divPassword.removeChild(divInputPassword);
}

function changeUsernameClick(divInputUsername){
    alert("sarai disconnesso");
    const divUsername = divInputUsername.parentElement;
    divUsername.lastElementChild.value = 'true';
    let changeUsernameBox = document.getElementById('username');
    changeUsernameBox.type = 'text';
    changeUsernameBox.className = 'form-control';
    divUsername.removeChild(divInputUsername);
}