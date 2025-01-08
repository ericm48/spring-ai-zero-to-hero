package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebugController {
  @Value("${spring.ai.azure.openai.api-key:notfound}")
  private String apiKey;

  @Value("${spring.ai.azure.openai.endpoint:notfound}")
  private String endpoint;

  @Value("${spring.ai.azure.openai.chat.options.deployment-name:notfound}")
  private String deploymentName;

  @GetMapping("/debug")
  public String getDebug() {
    return "You are running Azure OpenAI Application with: \n"
        + "apiKey="
        + apiKey
        + "\n"
        + "endpoint="
        + endpoint
        + "\n"
        + "deploymentName="
        + deploymentName
        + "\n";
  }
}
