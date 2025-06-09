package com.example.notification_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "AUTH-SERVICE", dismiss404 = true)
public interface AuthClient {

    @GetMapping("/api/v1/users/me/email")
    String getUserEmail(@RequestHeader("X-User-Id") Integer userId);

}
