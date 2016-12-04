package br.ufrn.imd.projeto.dominio;

/**
 * Created by ronaldo on 02/12/2016.
 */

public class User_Login {
    public String email;
    public String password;

    public User_Login(String user, String password) {
        this.email = user;
        this.password = password;
    }

    @Override
    public String toString() {
        return (email +  " " + password );
    }
}
