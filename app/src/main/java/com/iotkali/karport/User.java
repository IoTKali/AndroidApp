package com.iotkali.karport;

import android.content.SharedPreferences;

public class User {

    private String gender, birthDate, condition, name, email, password;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    public User(SharedPreferences sharedPreferences){
        this.sharedPref = sharedPreferences;
        this.editor = this.sharedPref.edit();
        this.email = sharedPref.getString("email", "N/A");
        this.name = sharedPref.getString("name","N/A");
        this.birthDate = sharedPref.getString("birthDate","N/A");
        this.gender = sharedPref.getString("gender","N/A");
        this.password = sharedPref.getString("password","N/A");
        this.condition = sharedPref.getString("condition","N/A");
    }
    public User(String name, String birthDate, String email, String password, String gender, String condition, SharedPreferences sharedPreferences) {
        this.gender = gender;
        this.birthDate = birthDate;
        this.condition = condition;
        this.name = name;
        this.email = email;
        this.password = password;
        this.sharedPref = sharedPreferences;
        this.editor = this.sharedPref.edit();
        this.editor.putString("email",email);
        this.editor.putString("name",name);
        this.editor.putString("birthDate",birthDate);
        this.editor.putString("gender",gender);
        this.editor.putString("password",password);
        this.editor.putString("condition",condition);
        this.editor.commit();
    }
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
        this.editor.putString("gender",gender);
        this.editor.commit();
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthdDate(String birthDate) {
        this.birthDate = birthDate;
        this.editor.putString("birthDate",birthDate);
        this.editor.commit();
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
        this.editor.putString("condition",condition);
        this.editor.commit();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.editor.putString("name",name);
        this.editor.commit();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        this.editor.putString("email",email);
        this.editor.commit();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.editor.putString("password",password);
        this.editor.commit();
    }
    public boolean isOnline(){
        if (this.gender.equals("N/A") || this.condition.equals("N/A") || this.name.equals("N/A") || this.email.equals("N/A")){
            return false;
        }else{
            return true;
        }
    }
    public void logOut(){
        editor.clear();
        editor.commit();
    }
    public String toString(){
        return this.name + "\n" + this.email + "\n" + this.password + "\n" + this.birthDate + "\n" + this.gender + "\n" + this.condition;
    }
}