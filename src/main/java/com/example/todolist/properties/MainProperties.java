package com.example.todolist.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "main")
@Getter
@Setter
@Validated
public class MainProperties {
  @NotBlank(
      message =
          "Main property \"exceptionDateFormat\" cannot be null or empty: check application.yml")
  String exceptionDateFormat;

  @NotBlank(
      message = "Main property \"errorInvalidName\" cannot be null or empty: check application.yml")
  String errorInvalidName;
}
