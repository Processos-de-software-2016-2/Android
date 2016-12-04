package br.ufrn.imd.projeto.dominio;

/**
 * Created by ronaldo on 02/12/2016.
 */

public class User {
    public String email;
    public int age;
    public String password;
    public int id;
    public String name;

    public User(String email, int age, String password, int id, String name) {

        this.email = email;
        this.age = age;
        this.password = password;
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return (email + " " + age + " " + password + " " + id);
    }
}
