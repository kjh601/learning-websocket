let stompClient = null;

function submitEnter(event, roomId, userId) {
    if(event.keyCode === 13){
        sendMessage(roomId, userId)
    }
}

function connect(roomId) {
    let socket = new WebSocket('ws://localhost:8080/ws'); // SockJS 클라이언트 초기화
    stompClient = Stomp.over(socket);
    console.log(stompClient)
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/sub/rooms/' + roomId, function (messageOutput) {
            showMessageOutput(JSON.parse(messageOutput.body));
        });
    });

    let chatList = document.querySelector('.chat-history');
    chatList.scrollTop = chatList.scrollHeight;
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function sendMessage(roomId, userId) {
    let input = document.getElementById("messageContent");
    let userName = document.body.getAttribute("data-user-name");
    let messageContent = input.value; // 메시지 내용을 가져옵니다.

    if (messageContent) {
        stompClient.send("/pub/rooms/" + roomId, {}, JSON.stringify({
            'messageType': 'TALK',
            'senderId': userId,
            'senderName' : userName,
            'message': messageContent
        }));
    }
    input.value = null;
}

function showMessageOutput(messageOutput) {
    const chatHistory = document.querySelector('.chat-history ul');
    const msgElement = document.createElement('li');
    const userId = Number(document.body.getAttribute("data-user-id"));
    const messageUserId = messageOutput.senderId;

    let isMine = userId === messageUserId;
    msgElement.classList.add('clearfix');

    let htmlString = null;
    switch (messageOutput.messageType) {
        case 'TALK':
            if (isMine) {
                htmlString = `<div class="message-data text-right">${messageOutput.senderName}</div>
                              <div class="message my-message float-right">${messageOutput.message}</div>`;
            } else {
                htmlString = `<div class="message-data">${messageOutput.senderName}</div>
                              <div class="message other-message">${messageOutput.message}</div>`;
            }
            break;
        case 'ENTER':
            htmlString = `<div class="message alarm-message text-center">${messageOutput.senderName}씨가 입장하였습니다</div>`;
            break;
        case 'LEAVE':
            htmlString = `<div class="message alarm-message text-center">${messageOutput.senderName}씨가 퇴장하였습니다</div>`;
            break;
    }

    if (htmlString) {
        msgElement.innerHTML = htmlString;
        chatHistory.appendChild(msgElement);
    }

    let chatList = document.querySelector('.chat-history');
    chatList.scrollTop = chatList.scrollHeight;
}