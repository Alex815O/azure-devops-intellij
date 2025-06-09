package com.microsoft.alm.plugin.external.models.pullRequestThread;

public class IdentityRef {
    private ReferenceLinks _links;
    private String descriptor;
    private String directoryAlias;
    private String displayName;
    private String id;
    private String imageUrl;
    private boolean inactive;
    private boolean isAadIdentity;
    private boolean isContainer;
    private boolean isDeletedInOrigin;
    private String profileUrl;
    private String uniqueName;
    private String url;

    public ReferenceLinks get_links() {
        return _links;
    }

    public void set_links(ReferenceLinks _links) {
        this._links = _links;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public String getDirectoryAlias() {
        return directoryAlias;
    }

    public void setDirectoryAlias(String directoryAlias) {
        this.directoryAlias = directoryAlias;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isInactive() {
        return inactive;
    }

    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }

    public boolean isAadIdentity() {
        return isAadIdentity;
    }

    public void setAadIdentity(boolean aadIdentity) {
        isAadIdentity = aadIdentity;
    }

    public boolean isContainer() {
        return isContainer;
    }

    public void setContainer(boolean container) {
        isContainer = container;
    }

    public boolean isDeletedInOrigin() {
        return isDeletedInOrigin;
    }

    public void setDeletedInOrigin(boolean deletedInOrigin) {
        isDeletedInOrigin = deletedInOrigin;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}