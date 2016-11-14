package br.ufrn.imd.projeto;

import android.app.Application;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class BaseAppExtender extends Application {
    private Bitmap picture;
    private String name;
    private String email;
    private List<String> ability = new ArrayList<>();
    private List<String> interest = new ArrayList<>();

    public Bitmap getPicture() {
        return picture;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getAbility() {
        return ability;
    }

    public List<String> getInterest() {
        return interest;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAbility(List<String> ability) {
        this.ability = ability;
    }

    public void setInterest(List<String> interest) {
        this.interest = interest;
    }

    public void addAbility(String ability) {
        this.ability.add(ability);
    }

    public void removeAbility(String ability) {
        int index = this.ability.indexOf(ability);
        this.ability.remove(index);
    }

    public void addInterest(String interest) {

        this.interest.add(interest);
    }

    public void removeInterest(String interest) {
        int index = this.interest.indexOf(interest);
        this.interest.remove(index);
    }
}
