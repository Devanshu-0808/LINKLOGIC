package com.example.linklocal.Service;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class GroupIdCreation {
       public String createGroupId(){
        return UUID.randomUUID().toString();
       }
}
