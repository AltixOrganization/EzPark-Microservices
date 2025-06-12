package com.altix.ezpark.iam.interfaces.rest.transform;

import com.altix.ezpark.iam.domain.model.aggregates.User;
import com.altix.ezpark.iam.domain.model.entities.Role;
import com.altix.ezpark.iam.interfaces.rest.resources.AuthenticatedUserResource;

public class AuthenticatedUserResourceFromEntityAssembler {

  public static AuthenticatedUserResource toResourceFromEntity(User user, String token) {
    var roles = user.getRoles().stream()
            .map(Role::getStringName)
            .toList();
    return new AuthenticatedUserResource(user.getId(), user.getEmail(), token, roles);
  }
}