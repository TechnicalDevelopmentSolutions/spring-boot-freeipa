package com.techdevsolutions.beans.auditable;

import com.techdevsolutions.beans.ValidationResponse;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class User extends Auditable implements Serializable {
    private String username = "";

    public static ValidationResponse Validate(User i) {
        return User.Validate(i, false);
    }

    public static ValidationResponse Validate(User i, Boolean isNew) {
        if (!Auditable.Validate(i, isNew).getValid()) {
            return Auditable.Validate(i, isNew);
        }

        if (StringUtils.isEmpty(i.getUsername())) {
            return new ValidationResponse(false, "username", "Auditable username is empty");
        }

        return new ValidationResponse(true, "", "");
    }

    public User() {
        super();
    }

    public static Integer GetUIdFromPrincipal(Map<String, Object> principal) {
        Map<String, Object> result = (Map<String, Object>) principal.get("result");
        List<String> uidNumber = (List<String>) result.get("uidnumber");
        return Integer.valueOf(uidNumber.get(0));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), username);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                "} " + super.toString();
    }
}