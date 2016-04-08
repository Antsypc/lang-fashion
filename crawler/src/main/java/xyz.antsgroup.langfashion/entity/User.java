package xyz.antsgroup.langfashion.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import xyz.antsgroup.langfashion.json.DateTimeDeser;
import xyz.antsgroup.langfashion.json.UserSiteAdminDeser;

/**
 * A user on github.com map to a table in database.
 *
 * @author ants_ypc
 * @version 1.0 4/7/16
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class User {
    private int id;
    private String login;       // username for login
    private String type;

    @JsonProperty("site_admin")
    @JsonDeserialize(using = UserSiteAdminDeser.class)
    private int siteAdmin;

    private String name;        // user named themself, not for login
    private String company;
    private String location;
    private String email;

    @JsonProperty("public_repos")
    private int publicRepos;

    @JsonProperty("public_gists")
    private int publicGists;

    private int followers;
    private int following;

    @JsonProperty("created_at")
    @JsonDeserialize(using = DateTimeDeser.class)
    private int createdAt;

    @JsonProperty("updated_at")
    @JsonDeserialize(using = DateTimeDeser.class)
    private int updatedAt;

    public User() {
    }

    public User(int id, String login, String type, int siteAdmin, String name, String company, String location, String email, int publicRepos, int publicGists, int followers, int following, int createdAt, int updatedAt) {
        this.id = id;
        this.login = login;
        this.type = type;
        this.siteAdmin = siteAdmin;
        this.name = name;
        this.company = company;
        this.location = location;
        this.email = email;
        this.publicRepos = publicRepos;
        this.publicGists = publicGists;
        this.followers = followers;
        this.following = following;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSiteAdmin() {
        return siteAdmin;
    }

    public void setSiteAdmin(int siteAdmin) {
        this.siteAdmin = siteAdmin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPublicRepos() {
        return publicRepos;
    }

    public void setPublicRepos(int publicRepos) {
        this.publicRepos = publicRepos;
    }

    public int getPublicGists() {
        return publicGists;
    }

    public void setPublicGists(int publicGists) {
        this.publicGists = publicGists;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(int createdAt) {
        this.createdAt = createdAt;
    }

    public int getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(int updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", type='" + type + '\'' +
                ", siteAdmin=" + siteAdmin +
                ", name='" + name + '\'' +
                ", company='" + company + '\'' +
                ", location='" + location + '\'' +
                ", email='" + email + '\'' +
                ", publicRepos=" + publicRepos +
                ", publicGists=" + publicGists +
                ", followers=" + followers +
                ", following=" + following +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
