package com.example.todolist.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ControllerUtil {
    public static String toJsonString(final Object obj) throws RuntimeException {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public  static String createStringWithLength(int length) {
        return "a".repeat(Math.max(0, length));
    }
}
