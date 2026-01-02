package com.quest.badminton.util;

import com.quest.badminton.entity.UserInfoDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static String getCurrentUserEmail() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) return null;
        UserInfoDetails user =  (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null) return null;
        return user.getUsername();
    }

    public static Long getCurrentUserId() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) return null;
        UserInfoDetails user =  (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null) return null;
        return user.getId();
    }
}
