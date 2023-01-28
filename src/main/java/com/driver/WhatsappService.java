package com.driver;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WhatsappService {

    // common Object of repository class
    WhatsappRepository whatsappRepository = new WhatsappRepository();

    public boolean checkNewUser(String mobNo) {
        return whatsappRepository.checkNewUser(mobNo);
    }


    public String createUser(String name, String mobNo) throws Exception {
        return whatsappRepository.createUser(name, mobNo);
    }


    public Group createGroup(List<User> user) {
        return whatsappRepository.createGroup(user);
    }


    public int createMessage(String messageContent){
        return whatsappRepository.createMessage(messageContent);
    }


    public int sendMessage(Message message, User sender, Group group) throws Exception{
        return whatsappRepository.sendMessage(message, sender, group);
    }



    public String changeAdmin(User approver, User user, Group group) throws Exception {
        return whatsappRepository.changeAdmin(approver, user, group);
    }




}
