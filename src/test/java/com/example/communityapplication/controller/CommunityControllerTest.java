package com.example.communityapplication.controller;

import com.example.communityapplication.model.Community;
import com.example.communityapplication.model.User;
import com.example.communityapplication.model.WebCommunity;
import com.example.communityapplication.service.CommunityService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CommunityControllerTest {

    @Mock
    private CommunityService communityService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @InjectMocks
    private CommunityController communityController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowCommunityPage() {
        // Arrange
        List<Community> communities = new ArrayList<>();
        Community community1 = new Community("Community 1", "Description 1",  false);
        Community community2 = new Community("Community 2", "Description 2",  false);
        communities.add(community1);
        communities.add(community2);
        when(communityService.getAllCommunities()).thenReturn(communities);

        // Act
        String viewName = communityController.showCommunityPage(model);

        // Assert
        assertEquals("community/community-page", viewName);
        verify(model).addAttribute("communities", communities);
    }

    @Test
    void testShowCommunityDetails() {
        // Arrange
        String communityName = "Community 1";
        Community community = new Community(communityName, "Description 1", false);
        when(communityService.getByCommunityName(communityName)).thenReturn(community);

        // Act
        String viewName = communityController.showCommunityDetails(communityName, model, session);

        // Assert
        assertEquals("community/community-details", viewName);
        verify(model).addAttribute("community", community);
    }

    @Test
    void testShowCreateCommunityForm() {
        // Act
        String viewName = communityController.showCreateCommunityForm(model);

        // Assert
        assertEquals("community/community-form", viewName);
        verify(model).addAttribute(eq("newCommunity"), any(Community.class));
    }

    @Test
    void testProcessCreateCommunityForm_WithErrors() {
        // Arrange
        WebCommunity community = new WebCommunity("Community 1", "",  false);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        String viewName = communityController.processCreateCommunityForm(community, bindingResult, session, model);

        // Assert
        assertEquals("community/community-form", viewName);
        verifyNoInteractions(communityService);
    }

    @Test
    void testProcessCreateCommunityForm_ExistingCommunity() {
        // Arrange
        WebCommunity community = new WebCommunity("Community 1", "Description 1", false);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(communityService.isExist(community.getName())).thenReturn(true);

        // Act
        String viewName = communityController.processCreateCommunityForm(community, bindingResult, session, model);

        // Assert
        assertEquals("community/community-form", viewName);
        verify(communityService).isExist(community.getName());
        verify(model).addAttribute(eq("newCommunity"), any(Community.class));
        verify(model).addAttribute("communityCreationError", "Community name already exists.");
        verifyNoMoreInteractions(model, communityService);
    }

    @Test
    void testProcessCreateCommunityForm_Success() {
        // Arrange
        WebCommunity community = new WebCommunity("Community 1", "Description 1", false);
        Community savedCommunity = new Community("Community 1", "Description 1", false);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(communityService.isExist(community.getName())).thenReturn(false);

        // Act
        String viewName = communityController.processCreateCommunityForm(community, bindingResult, session, model);

        // Assert
        assertEquals("community/community-page", viewName);
        verify(communityService).isExist(community.getName());
        verify(communityService).createCommunity(savedCommunity, null);
        verifyNoMoreInteractions(communityService, model);
    }

    @Test
    public void testJoinCommunity_Success() {
        // Arrange
        String communityName = "Test Community";
        Community community = new Community();
        community.setName(communityName);

        User user = new User();
        user.setId(1);

        when(session.getAttribute("user")).thenReturn(user);
        when(communityService.getByCommunityName(communityName)).thenReturn(community);
        when(communityService.isMember(community, user)).thenReturn(true);

        // Act
        String viewName = communityController.joinCommunity(communityName, model, session);

        // Assert
        assertEquals("community/community-details", viewName);
        verify(model).addAttribute("community", community);
        verify(model).addAttribute("isMember", true);
    }

    @Test
    public void testJoinCommunity_CommunityNotFound() {
        // Arrange
        String communityName = "Nonexistent Community";
        when(communityService.getByCommunityName(communityName)).thenReturn(null);
        User user = new User();
        user.setId(1);
        when(session.getAttribute("user")).thenReturn(user);

        // Act
        String viewName = communityController.joinCommunity(communityName, model, session);

        // Assert
        assertEquals("community/community-details", viewName);
        verify(model).addAttribute("community", null);
    }

    @Test
    public void testJoinCommunity_UserNotAuthenticated() {
        // Arrange
        String communityName = "Test Community";
        Community community = new Community();
        community.setName(communityName);

        // Mocking session to return null user
        when(session.getAttribute("user")).thenReturn(null);
        when(communityService.getByCommunityName(communityName)).thenReturn(community);
        when(communityService.isMember(community, null)).thenReturn(false);

        // Act
        String viewName = communityController.joinCommunity(communityName, model, session);

        // Assert
        assertEquals("community/community-details", viewName);
        verify(model).addAttribute("community", community);
        verify(model).addAttribute("isMember", false);
    }
}

