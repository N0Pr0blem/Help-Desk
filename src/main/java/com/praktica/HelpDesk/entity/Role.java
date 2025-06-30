package com.praktica.HelpDesk.entity;

public enum Role {
    USER,ADMIN,SYSADMIN;

    public static Role fromString(String role) {
        for(Role r : Role.values()){
            if(r.name().equals(role.toUpperCase())) return r;
        }
        return Role.USER;
    }
}
