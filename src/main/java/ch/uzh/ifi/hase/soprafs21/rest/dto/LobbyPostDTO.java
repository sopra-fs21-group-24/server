package ch.uzh.ifi.hase.soprafs21.rest.dto;


public class LobbyPostDTO {



    private String creator;
    private Boolean isPublic;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }


    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }





}
