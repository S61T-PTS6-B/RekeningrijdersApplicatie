var wsURINAW = "ws://145.93.105.25:8080/RekeningAdministratieOverheid/NAWSocket";
var wsURICarTracker = "ws://145.93.105.25:8080/RekeningAdministratieOverheid/CarTrackerSocket";
window.addEventListener("load", onLoad, false);
var websocketnaw = null;
var websocketcartracker = null;

function connect() {
    websocketnaw = new WebSocket(wsURINAW);
    websocketcartracker = new WebSocket(wsURICarTracker);
    websocketcartracker.onerror = function (evt) {
        document.getElementById("profileconnectionmessage").innerHTML = "Kan geen verbinding maken met het administratiesysteem van de overheid. Sommige gegevens kunnen niet worden weergegeven.";
    };
    websocketnaw.onerror = function (evt) {
        document.getElementById("profileconnectionmessage").innerHTML = "Kan geen verbinding maken met het administratiesysteem van de overheid. Sommige gegevens kunnen niet worden weergegeven.";
    };
    websocketcartracker.onopen = function () {
        document.getElementById("profileconnectionmessage").innerHTML = "";
    };
    websocketnaw.onopen = function () {
        document.getElementById("profileconnectionmessage").innerHTML = "";
    };
    websocketnaw.onclose = function () {
        window.alert("Geen verbinding met NAW server van de overheid.");
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
    var message = JSON.stringify({'bsn' : bsn , 'newphone' : '' , 'newmail' : ''});
    sendMessage(message, websocketnaw);
    sendMessage(message, websocketcartracker);
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
        li.innerHTML = cartracker.brandCar + " " + cartracker.modelCar + " (" + cartracker.licensePlate + ")";
        document.getElementById("carslist").appendChild(li);
    }
}

function OnChangePhoneClick() {
    var newphone = window.prompt("Voer je nieuwe telefoonnummer in:");
    if (!validatePhone(newphone)) {
        window.alert("Het door u ingevulde nummer is geen geldig telefoonnummer.");
    } else {
        var bsn = document.getElementById("bsncontainer").innerHTML;
        var message = JSON.stringify({'bsn' : bsn , 'newphone' : newphone , 'newmail' : ""});
        sendMessage(message, websocketnaw);
    }
}

function OnChangeEmailClick() {
    var newmail = window.prompt("Voer je nieuwe e-mailadres in:");
    if (!validateEmail(newmail)) {
        window.alert("Het door u ingevulde adres is geen geldig e-mailadres.");
    } else {
        var bsn = document.getElementById("bsncontainer").innerHTML;
        var message = JSON.stringify({'bsn' : bsn , 'newphone' : "" , 'newmail' : newmail});
        sendMessage(message, websocketnaw);
    }
}

function validateEmail(email) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
}

function validatePhone(phone) {
    return /^\d+$/.test(phone);
}