const ws = new WebSocket('ws://localhost:61614', 'stomp');
ws.onopen = () => {
    ws.send('CONNECT\n\n\0');
    ws.send('SUBSCRIBE\ndestination:/topic/Notifications\n\nack:auto\n\n\0');
};
ws.onmessage = (e) => {
    if(e.data.startsWith('MESSAGE')) {
        var data = e.data.split('\n\n');
        var message = "";

        for(var i = 1; i < data.length; i++) {
            message += data[i];
        }

        $.notify({
            title: "<strong>Оповещение: </strong>",
            message: message
        });
    }
};