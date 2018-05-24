package com.lostandfoundapp.BE;

public class User
{

    String m_username;
    String m_password;

public User(){

}

    public User(String m_username, String m_password) {
        this.m_username = m_username;
        this.m_password = m_password;
    }

    public String getM_username() {
        return m_username;
    }

    public void setM_username(String m_username) {
        this.m_username = m_username;
    }

    public String getM_password() {
        return m_password;
    }

    public void setM_password(String m_password) {
        this.m_password = m_password;
    }
}
