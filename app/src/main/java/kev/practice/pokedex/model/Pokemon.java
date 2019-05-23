package kev.practice.pokedex.model;

public class Pokemon {

    private int number;
    private String name;
    private String url;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        url = url;
    }

    public int getNumber() {
        String[] urlPartes = url.split("/");
        return  Integer.parseInt(urlPartes[urlPartes.length - 1]);
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
