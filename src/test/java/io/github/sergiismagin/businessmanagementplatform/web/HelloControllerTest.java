package io.github.sergiismagin.businessmanagementplatform.web;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloControllerTest {

    @Test
    void helloReturnsGreetingForProvidedName() {
        HelloController helloController = new HelloController();
        HelloResponse helloResponse = helloController.hello("Spring");
        assertEquals(new HelloResponse("Hello from Spring"), helloResponse);
    }
}
