var wsURINAW = "ws://192.168.32.102:8080/RekeningAdministratieOverheid/NAWSocket";
window.addEventListener("load",onLoad,false);
var websocket = null;

function onLoad() {
    websocket = new WebSocket(wsURINAW);
    websocket.onmessage = function (evt) {
        var messagejson = evt.data;
        var message = JSON.parse(messagejson);
        console.log("Received from Socket: " + messagejson);
        document.getElementById("registerform:emailfromsocket").value = message.email;
    };
    websocket.onopen = function () {
        console.log("Opened connection Socket: " + wsURINAW);
    };
}

function getEmailFromBsn() {
    var bsn = document.getElementById("registerform:bsn").value;
    var message = JSON.stringify({'bsn' : bsn , 'newphone' : '' , 'newmail' : '',register:'true' });
    console.log(message);
    sendMessage(message, websocket);
}

function sendMessage(msg, socket) {
    waitForSocketConnection(socket, function () {
        console.log("Message sent: " + msg);
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