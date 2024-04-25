package com.example.communityapplication.dao;

import com.example.communityapplication.model.Community;
import com.example.communityapplication.model.Content;
import com.example.communityapplication.model.ContentTemplate;

import java.util.List;

public interface ContentTemplateDao {
    ContentTemplate findByNameAndCommunityId(String name, Community community);
    List<ContentTemplate> findByCommunityId(Community community);
    ContentTemplate findById(Long id);
    void save(ContentTemplate theContentTemplate);

}
