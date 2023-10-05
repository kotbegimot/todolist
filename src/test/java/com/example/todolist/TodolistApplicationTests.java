package com.example.todolist;

import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

class TodolistApplicationTests {
  @Test
  void runTest() {
    String[] args = new String[] {""};
    assertAll(() -> TodolistApplication.main(args));
  }
}
