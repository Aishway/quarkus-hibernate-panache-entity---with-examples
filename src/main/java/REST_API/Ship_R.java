package REST_API;


public class Ship_R {

    public String name;
    public String direction;
    public  String size;


    public Ship_R(String name, String direction, String size) {
        this.name = name;
        this.direction = direction;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Ship name='" + name + '\'' +
                ", direction='" + direction + '\'' +
                ", size='" + size;
    }
}
