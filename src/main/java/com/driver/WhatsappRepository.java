package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    // my HashMaps
    private Map<String, User> userMap;

    private HashMap<String, List<User>> groupMap;
    private Map<String, List<User>> personalChatGroup;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();

        this.groupUserMap = new HashMap<Group, List<User>>(); //

        this.senderMap = new HashMap<Message, User>(); // use in send message function

        this.adminMap = new HashMap<Group, User>();  // group mapped with its admin

        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;

        this.userMap = new HashMap<String, User>();   // mob no is unique key
        this.groupMap = new HashMap<String, List<User>>();

    }

    // create a user
    // @PostMapping
    public String createUser(String name, String mobNo) throws Exception {
        if(!userMap.containsKey(mobNo)) {
            userMap.put(mobNo, new User(name, mobNo));
        }else {
            throw new Exception("User already exist");
        }
        return "User Created Successfully";
    }


   // find admin -- no need we have adminMap
//    public boolean findAdmin(List<User> users, User user) {
//        if(users.get(0).equals(user)) {
//            return true;
//        }
//        return false;
//    }


    // creation of personalChat
    public Group personalChatGroup(List<User> users) {
        Group personalChat = new Group(users.get(1).getName(), 2);
        groupUserMap.put(personalChat, users); // personal chat is also a group, hence putted into dataBase
        return personalChat;
    }



    // create a group
    public Group createGroup(List<User> users) {
        if(users.size() == 2) {
            return personalChatGroup(users);
        }

        this.customGroupCount++;
        // If there are 2 or more users, the name of the group will be "Group count".
        Group group = new Group("Group " + this.customGroupCount, users.size());
        groupUserMap.put(group, users);
        adminMap.put(group, users.get(0));
        return group;
    }


    // create a message
    public int createMessage(String messageContent) {
        Message message = new Message(this.messageId++, messageContent, new Date());
        return this.messageId; // from which id message was created
    }



    // Send a message by providing the message, sender, and group.
    // means i have to send message in some group
    public int sendMessage(Message message, User sender, Group group) throws Exception{
        String senderName = sender.getName();
        String messageContent = message.getContent();
        String groupName = group.getName();

        if(!groupUserMap.containsKey(group.getName())) throw new Exception("Group does not exist");


        if(!checkSender(group,sender)) throw new Exception("User does not exist in group");

        List<Message> messageList = groupMessageMap.get(group);
        messageList.add(message);
        groupMessageMap.put(group, messageList);  // also updated in DB

        int messageListSize = groupMessageMap.get(group).size();

        // return messageList.size();
        return messageListSize;
    }


    // check if sender is present in group or not
    public boolean checkSender(Group group, User sender) {
        List<User> userList = groupUserMap.get(group);
//        if(groupUserMap.containsKey(group)) {
//            userList = groupUserMap.get(group);
//        }

        for (User user : userList) {
            if(userList.contains(sender))
                return true;
        }
        return false;
    }


    // change admin
    public String changeAdmin(User approver, User user, Group group) throws Exception {
        if(!groupUserMap.containsKey(group)) throw new Exception("Group does not exist");

        if(!adminMap.get(approver).equals(approver)) throw new Exception("Approver is not the admin");

        List<User> userList = groupUserMap.get(group);
        if(!userList.contains(user)) throw new Exception("User is not part of the Group");

        adminMap.put(group, user);
        return "Admin Changed Successfully";
    }
}
