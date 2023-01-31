package com.contextlab.authservice.config;

import com.contextlab.authservice.model.User;
import java.util.Map;

public interface JwtGeneratorInterface {

    Map<String, String> generateToken(User user);
}
