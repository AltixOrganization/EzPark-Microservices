package com.altix.ezpark.iam.interfaces.rest.transform;

import com.altix.ezpark.iam.domain.model.aggregates.User;
import com.altix.ezpark.iam.domain.model.entities.Role;
import com.altix.ezpark.iam.interfaces.rest.resources.UserResource;

public class UserResourceFromEntityAssembler {

  public static UserResource toResourceFromEntity(User user) {
    var roles = user.getRoles().stream()
            .map(Role::getStringName)
            .toList();
    return new UserResource(user.getId(), user.getEmail(), roles);
  }
}
