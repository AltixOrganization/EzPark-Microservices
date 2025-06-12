package com.altix.ezpark.iam.interfaces.rest.transform;

import com.altix.ezpark.iam.domain.model.entities.Role;
import com.altix.ezpark.iam.interfaces.rest.resources.RoleResource;

public class RoleResourceFromEntityAssembler {

  public static RoleResource toResourceFromEntity(Role role) {
    return new RoleResource(role.getId(), role.getStringName());
  }
}
