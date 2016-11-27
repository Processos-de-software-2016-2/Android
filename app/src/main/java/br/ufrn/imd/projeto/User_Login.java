package br.ufrn.imd.projeto;

/**
 * Created by ronaldo on 26/11/2016.
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
