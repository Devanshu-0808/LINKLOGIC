package com.example.linklocal.Controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.linklocal.Pojo.ChatMessage;
import com.example.linklocal.Pojo.CodeMessage;
import com.example.linklocal.Pojo.GrpId;
import com.example.linklocal.Pojo.sideMessage;
import com.example.linklocal.Repository.ConnectivityDb;
import com.example.linklocal.Service.GroupIdCreation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller

public class Conto {

    @Autowired
    GroupIdCreation groupIdCreation;
   
    @Autowired
    ConnectivityDb connectionDb;

    @Autowired
    GroupIdCreation GroupIdCreation;
   
   

    String  groupId1;
   String username;
   Boolean created = false;
  

    @GetMapping("/")
    public String requestMethodName(HttpServletRequest req, Model model) {
        HttpSession session = req.getSession();
        session.setAttribute("groupId", groupIdCreation.createGroupId());
        return "index";
    }

    @GetMapping("/session/groupId")
    @ResponseBody
    public Map<String, String> getGroupId(HttpServletRequest req , HttpServletResponse res) {
        HttpSession session = req.getSession();
        String groupId = GroupIdCreation.createGroupId();
       created = true;
        Map<String, String> response = new HashMap<>();
        response.put("groupId", groupId);
        return response;
    }

    @RequestMapping("/page1")
    public String page1() {
        return "page1";
    }

    @GetMapping("/login")
    public String requestMethodName1(HttpServletRequest req, Model model) {
         groupId1 = req.getParameter("groupId");
          username = req.getParameter("username");
          HttpSession session = req.getSession();
          if(connectionDb.findByUsernameAndGroupId(username, groupId1)!=null) return"index";
          else if(!connectionDb.findByGroupId(groupId1).isEmpty() ){
            session.setAttribute("username", username);
            session.setAttribute("groupId1", groupId1);
            return "page1";
          }
          else if(created){
            session.setAttribute("username", username);
            session.setAttribute("groupId1", groupId1);
            created=false;
            return "page1";
          }  
          return "index";
     
    }
    @MessageMapping("/sendMessage/{groupId}")
    @SendTo("/topic/messages/{groupId}")
    public ChatMessage sendMessage(ChatMessage message) {
        return message;
    }
    @GetMapping("/getusername")
    @ResponseBody
    public String getUsername() {
        return username;
    }
    
    @MessageMapping("/sendCodeMessage/{groupId}")
    @SendTo("/topic/Codemessages/{groupId}")
    public CodeMessage sendMessage(CodeMessage message) {
        return message;
    } 

    @MessageMapping("/sendSidebar/{groupId}")
    @SendTo("/topic/Sidebar/{groupId}")
    public sideMessage sendsidebar(sideMessage message) {
        return message;
    } 
    
    @MessageMapping("/disconnectUser/{groupId}")
    @SendTo("/topic/Disconnect/{groupId}")
    public sideMessage disconnectUser(sideMessage message) {
        return message;
    }

    @GetMapping("/allPrevUsers")
    @ResponseBody
 public List<String> allPrevUsers(@RequestParam String groupId) {
        List<GrpId> allUsers = connectionDb.findAllByGroupId(groupId);
        List<String> allUsernames = new ArrayList<>();
        for (GrpId user : allUsers) {
            allUsernames.add(user.getUsername());      
    }
    return allUsernames;
    }

 
}
