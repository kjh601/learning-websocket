package learning.websocket.controller;

import learning.websocket.config.PrincipalDetails;
import learning.websocket.dto.*;
import learning.websocket.entity.User;
import learning.websocket.enums.MessageType;
import learning.websocket.service.MessageService;
import learning.websocket.service.RoomService;
import learning.websocket.service.UserRoomService;
import learning.websocket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    private final UserRoomService userRoomService;

    private final MessageService messageService;

    private final UserService userService;

    @GetMapping("/{roomId}")
    public String roomForm(@PathVariable Long roomId, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long userId = principalDetails.getUser().getId();

        RoomWithParticipantStatusDto room = roomService.findRoom(roomId, userId);
        model.addAttribute("room", room);

        model.addAttribute("isAdmin", userRoomService.checkAdmin(userId, roomId));
        return "room";
    }

    @GetMapping("")
    public String roomsForm(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        Long userId = principalDetails.getUser().getId();
        List<RoomWithParticipantStatusDto> rooms = roomService.findAll(userId);
        model.addAttribute("rooms", rooms);
        return "rooms";
    }

    @PostMapping("/add")
    public String addRoom(@ModelAttribute("room") RoomDto.createReq request, RedirectAttributes redirectAttributes, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        User admin = principalDetails.getUser();
        RoomDto.roomRes room = roomService.createRoom(request, admin);

        redirectAttributes.addAttribute("roomId", room.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/rooms/{roomId}";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("room", RoomDto.createReq.builder().build());
        return "addForm";
    }

    @GetMapping("/{roomId}/edit")
    public String editForm(@PathVariable Long roomId, Model model) {

        RoomDto.roomRes room = roomService.findRoom(roomId);
        model.addAttribute("room", room);

        return "editForm";
    }

    @PostMapping("/{roomId}/edit")
    public String editRoom(@PathVariable Long roomId, @ModelAttribute("room") RoomDto.editReq request, RedirectAttributes redirectAttributes) {
        System.out.println("request = " + request);
        RoomDto.roomRes room = roomService.updateRoom(request, roomId);

        redirectAttributes.addAttribute("roomId", room.getId());
        return "redirect:/rooms/{roomId}";
    }

    @GetMapping("/{roomId}/delete")
    public String removeRoom(@PathVariable Long roomId) {
        roomService.removeRoom(roomId);

        return "redirect:/rooms";
    }

    @GetMapping("/{roomId}/chat")
    public String chatForm(@PathVariable Long roomId, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {


        Long userId = principalDetails.getUser().getId();
        if(!userRoomService.checkEnter(userId, roomId)) {
            messageService.sendMessage(userId, roomId, MessageType.ENTER);
            roomService.joinRoom(userId, roomId);
        }

        UserDto.userRes admin = userService.findAdmin(roomId);
        UserDto.userRes userRes = userService.findUser(userId);
        RoomDto.roomRes roomRes = roomService.findRoom(roomId);
        List<UserRoomDto.userRes> participants = userRoomService.findAllParticipants(roomId);
        List<MessageDto.logRes> messages = messageService.getMessages(roomId, userId);


        model.addAttribute("admin", admin);
        model.addAttribute("user", userRes);
        model.addAttribute("room", roomRes);
        model.addAttribute("participants", participants);
        model.addAttribute("messages", messages);
        System.out.println("messages = " + messages);

        return "chatForm";
    }

    @GetMapping("/{roomId}/leave")
    public String leaveRoom(@PathVariable Long roomId, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Long userId = principalDetails.getUser().getId();

        messageService.sendMessage(userId, roomId, MessageType.LEAVE);
        roomService.leaveRoom(userId, roomId);

        return "redirect:/rooms";
    }
}
