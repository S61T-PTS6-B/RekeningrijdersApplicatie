var wsURINAW = "ws://145.93.104.202:8080/RekeningAdministratieOverheid/NAWSocket";
var wsURICarTracker = "ws://145.93.104.202:8080/RekeningAdministratieOverheid/CarTrackerSocket";
var wsURITransfer = "ws://145.93.104.202:8080/RekeningAdministratieOverheid/TransferSocket";
window.addEventListener("load", onLoad, false);
var websocketnaw = null;
var websocketcartracker = null;
var websockettransfer = null;
var messagecar;
var confirmmessage;
var confirmcode;

function connect() {
    websocketnaw = new WebSocket(wsURINAW);
    websocketcartracker = new WebSocket(wsURICarTracker);
    websockettransfer = new WebSocket(wsURITransfer);
    websocketcartracker.onerror = function (evt) {
        document.getElementById("profileconnectionmessage").innerHTML = "Kan geen verbinding maken met het administratiesysteem van de overheid. Sommige gegevens kunnen niet worden weergegeven.";
    };
    websocketnaw.onerror = function (evt) {
        document.getElementById("profileconnectionmessage").innerHTML = "Kan geen verbinding maken met het administratiesysteem van de overheid. Sommige gegevens kunnen niet worden weergegeven.";
    };
    websockettransfer.onerror = function (evt) {
        document.getElementById("profileconnectionmessage").innerHTML = "Kan geen verbinding maken met het administratiesysteem van de overheid. Sommige gegevens kunnen niet worden weergegeven.";
    };
    websocketcartracker.onopen = function () {
        console.log("cartracker")
        document.getElementById("profileconnectionmessage").innerHTML = "";
    };
    websockettransfer.onopen = function () {
        console.log("transfer")
        document.getElementById("profileconnectionmessage").innerHTML = "";
    };
    websocketnaw.onopen = function () {
        console.log("naw")
        document.getElementById("profileconnectionmessage").innerHTML = "";
    };
    websocketnaw.onclose = function () {
        window.alert("Geen verbinding met NAW server van de overheid.");
    };
    websockettransfer.onclose = function () {
        window.alert("Geen verbinding met Transfer server van de overheid.");
    };
    websocketcartracker.onclose = function () {
        window.alert("Geen verbinding met CarTracker server van de overheid.");
    };
    websocketnaw.onmessage = function (evt) {
        var messagejson = evt.data;
        var message = JSON.parse(messagejson);
        console.log("Received from NAWSocket: " + messagejson);
        FillProfileFields(message);
    };
    websockettransfer.onmessage = function (evt) {
        var messagejson = evt.data;
        messagecar = JSON.parse(messagejson);
        console.log(messagecar);
        if (messagecar.brandCar != "") {
            if (window.confirm("is this the car you want to transfer: " + messagecar.brandCar + " " + messagecar.modelCar + " (" + messagecar.licensePlate + ")") === true) {
                var bsn = document.getElementById("bsncontainer").innerHTML;
                console.log(messagecar.carid);
                var newmessage = JSON.stringify({'bsn': bsn, 'code': confirmcode, 'carid': messagecar.carid, 'type': 'confirm'});
                confirmmessage =messagecar;
                confirmcode = null;
                messagecar = null;
                sendMessage(newmessage, websockettransfer);
            } else {

            }
        } else {
            var li = document.createElement("li");
            var inhoud = confirmmessage.brandCar + " " + confirmmessage.modelCar + " (" + confirmmessage.licensePlate + ")";
            li.innerHTML = inhoud;
            var sellcar = document.createElement("input");
            sellcar.type = "button";
            sellcar.value = "Verkoop deze auto";
            sellcar.className = "buttons";
            sellcar.style.marginLeft = "5px";
            sellcar.onclick = function () {
                OnSellCarClick(inhoud, confirmmessage.id);
            };
            li.appendChild(sellcar);
            document.getElementById("carslist").appendChild(li);
        }
    };
    websocketcartracker.onmessage = function (evt) {
        var messagejson = evt.data;
        var message = JSON.parse(messagejson);
        console.log("Received from CarTrackSocket: " + messagejson);
        FillCarTrackerFields(message);
    };
}

function onLoad() {
    connect();
    var bsn = document.getElementById("bsncontainer").innerHTML;
    var message = JSON.stringify({'bsn': bsn, 'newphone': '', 'newmail': '', register: 'false'});
    sendMessage(message, websocketnaw);
    sendMessage(message, websocketcartracker);
    //sendMessage("hoi",websockettransfer);
}

function sendMessage(msg, socket) {
    waitForSocketConnection(socket, function () {
        console.log("Message sent: ");
        socket.send(msg);
    });
}

function waitForSocketConnection(socket, callback) {
    setTimeout(
            function () {
                if (socket.readyState === 1) {
                    console.log("Connection is made");
                    if (callback !== null) {
                        callback();
                    }
                    return;

                } else {
                    console.log("wait for connection...");
                    waitForSocketConnection(socket, callback);
                }

            }, 5);
}

function FillProfileFields(message) {
    document.getElementById("namecontainer").innerHTML = message.firstname + " " + message.lastname;
    document.getElementById("streetcontainer").innerHTML = message.address + " " + message.number;
    document.getElementById("postalcodecontainer").innerHTML = message.zipcode;
    document.getElementById("citycontainer").innerHTML = message.city;
    document.getElementById("emailcontainer").innerHTML = message.email;
    document.getElementById("phonecontainer").innerHTML = message.telephone;
}

function FillCarTrackerFields(message) {
    for (var i = 0; i < message.length; i++) {
        var cartracker = message[i];
        var li = document.createElement("li");
        var inhoud = cartracker.brandCar + " " + cartracker.modelCar + " (" + cartracker.licensePlate + ")";
        li.innerHTML = inhoud;
        var sellcar = document.createElement("input");
        sellcar.type = "button";
        sellcar.value = "Verkoop deze auto";
        sellcar.className = "buttons";
        sellcar.onclick = function () {
            OnSellCarClick(inhoud, cartracker.id)
        };
        li.appendChild(sellcar);
        document.getElementById("carslist").appendChild(li);
    }
}

function OnChangePhoneClick() {
    var newphone = window.prompt("Voer je nieuwe telefoonnummer in:");
    if (!validatePhone(newphone)) {
        window.alert("Het door u ingevulde nummer is geen geldig telefoonnummer.");
    } else {
        var bsn = document.getElementById("bsncontainer").innerHTML;
        var message = JSON.stringify({'bsn': bsn, 'newphone': newphone, 'newmail': ""});
        sendMessage(message, websocketnaw);
    }
}

function OnChangeEmailClick() {
    var newmail = window.prompt("Voer je nieuwe e-mailadres in:");
    if (!validateEmail(newmail)) {
        window.alert("Het door u ingevulde adres is geen geldig e-mailadres.");
    } else {
        var bsn = document.getElementById("bsncontainer").innerHTML;
        var message = JSON.stringify({'bsn': bsn, 'newphone': "", 'newmail': newmail});
        sendMessage(message, websocketnaw);
    }
}

function OnSellCarClick(inhoud, id) {
    var person = prompt("Weet je zeker dat je jouw ", + inhoud + " wilt overzetten naar een andere eigenaar? \n Vul hieronder het BSN in van de volgende eigenaar.", "BSN");
    if (person !== null) {
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            console.log(xhttp.status);
            if (xhttp.readyState === 4 && xhttp.status === 200) {
                prompt("Kopieer de volgende code en geef het \n aan de volgende eigenaar:\n", xhttp.responseText);
                var message = JSON.stringify({'bsn': person, 'code': xhttp.responseText, 'carid': id, 'type': 'register'});
                console.log(message);
                sendMessage(message, websockettransfer);
            }
        };
        xhttp.open("GET", "http://localhost:8080/RekeningrijdersApplicatie/rest/encrypt/" + inhoud, true);
        xhttp.send();
    } else {
    }
}

function transferCar() {
    var code = prompt("Vul hier de ontvangen code in om de auto naar uzelf over te zetten:", "Code");
    if (code !== null) {
        var bsn = document.getElementById("bsncontainer").innerHTML;
        var message = JSON.stringify({'bsn': bsn, 'code': code, 'carid': '', 'type': 'transfer'});
        confirmcode = code;
        sendMessage(message, websockettransfer);
    }
}


function validateEmail(email) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
}

function validatePhone(phone) {
    return /^\d+$/.test(phone);
}