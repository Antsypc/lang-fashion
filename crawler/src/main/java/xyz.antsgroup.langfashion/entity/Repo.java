package xyz.antsgroup.langfashion.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A repository on github.com map to a table in database.
 *
 * @author ants_ypc
 * @version 1.0 4/7/16
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Repo {
    private int id;
    private String name;

    private int ownerId;
    private String ownerLogin;

    private String language;
    private boolean fork;

    @JsonProperty("created_at")
    private int createdAt;

    @JsonProperty("updated_at")
    private int updatedAt;

    @JsonProperty("pushed_at")
    private int pushedAt;

    private int size;

    @JsonProperty("stargazers_count")
    private int stargazersCount;

    @JsonProperty("watchers_count")
    private int watchersCount;

    @JsonProperty("forks_count")
    private int forksCount;

    public Repo() {
    }

    public Repo(int id, String name, int ownerId, String ownerLogin, String language, boolean fork, int createdAt, int updatedAt, int pushedAt, int size, int stargazersCount, int watchersCount, int forksCount) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.ownerLogin = ownerLogin;
        this.language = language;
        this.fork = fork;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.pushedAt = pushedAt;
        this.size = size;
        this.stargazersCount = stargazersCount;
        this.watchersCount = watchersCount;
        this.forksCount = forksCount;
    }

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

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean getFork() {
        return fork;
    }

    public void setFork(boolean fork) {
        this.fork = fork;
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

    public int getPushedAt() {
        return pushedAt;
    }

    public void setPushedAt(int pushedAt) {
        this.pushedAt = pushedAt;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStargazersCount() {
        return stargazersCount;
    }

    public void setStargazersCount(int stargazersCount) {
        this.stargazersCount = stargazersCount;
    }

    public int getWatchersCount() {
        return watchersCount;
    }

    public void setWatchersCount(int watchersCount) {
        this.watchersCount = watchersCount;
    }

    public int getForksCount() {
        return forksCount;
    }

    public void setForksCount(int forksCount) {
        this.forksCount = forksCount;
    }

    @Override
    public String toString() {
        return "Repo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ownerId=" + ownerId +
                ", ownerLogin='" + ownerLogin + '\'' +
                ", language='" + language + '\'' +
                ", fork=" + fork +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", pushedAt=" + pushedAt +
                ", size=" + size +
                ", stargazersCount=" + stargazersCount +
                ", watchersCount=" + watchersCount +
                ", forksCount=" + forksCount +
                '}';
    }
}
