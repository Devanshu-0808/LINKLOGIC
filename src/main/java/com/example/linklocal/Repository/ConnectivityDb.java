package com.example.linklocal.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.linklocal.Pojo.GrpId; // Import the GrpId class

@Repository
public interface ConnectivityDb extends JpaRepository<GrpId, String> {
   List<GrpId> findByGroupId(String groupId);
     GrpId findByUsername(String username);
   int deleteBySocketId(String socketId);
   GrpId findByUsernameAndGroupId(String username, String groupId); // Update method to find by username and groupId
   List<GrpId> findAllByGroupId(String groupId);
}
