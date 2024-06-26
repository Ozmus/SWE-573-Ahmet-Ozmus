package com.example.communityapplication.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "content_templates")
public class ContentTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @ManyToOne
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @OneToMany(mappedBy = "contentTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Content> contents;

    public ContentTemplate() {
    }

    public ContentTemplate(String name, Community community) {
        this.name = name;
        this.community = community;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }
}
