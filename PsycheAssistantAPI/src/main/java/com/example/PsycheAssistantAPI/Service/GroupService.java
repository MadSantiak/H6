package com.example.PsycheAssistantAPI.Service;

import com.example.PsycheAssistantAPI.Model.Group;
import com.example.PsycheAssistantAPI.Repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }
}
