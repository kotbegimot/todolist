package com.example.todolist;

import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = "dev")
class TodolistApplicationTests {
  @Test
  void runTest() {
    String[] args = new String[] {""};
    assertAll(() -> TodolistApplication.main(args));
  }
}
