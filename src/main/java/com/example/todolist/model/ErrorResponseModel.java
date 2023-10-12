package com.example.todolist.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponseModel {
    Integer status;
    String error;
    List<String> messages;
    String path;
    String timestamp;
}
