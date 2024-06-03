package REST_API;

public class Pirate_R {

    private String name;
    private String nickname;
    private String function;
    private String status;

    public Pirate_R(String name, String nickname, String function, String status) {
        this.name = name;
        this.nickname = nickname;
        this.function = function;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Pirate with " +
                "name='" + name + '\'' +
                ", function='" + function ;
    }
}
