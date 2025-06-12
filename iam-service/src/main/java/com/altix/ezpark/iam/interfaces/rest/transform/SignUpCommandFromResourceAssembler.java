package com.altix.ezpark.iam.interfaces.rest.transform;

import com.altix.ezpark.iam.domain.model.commands.SignUpCommand;
import com.altix.ezpark.iam.domain.model.entities.Role;
import com.altix.ezpark.iam.interfaces.rest.resources.SignUpResource;

import java.util.ArrayList;

public class SignUpCommandFromResourceAssembler {

  public static SignUpCommand toCommandFromResource(SignUpResource resource) {
    var roles = resource.roles() != null
            ? resource.roles().stream().map(Role::toRoleFromName).toList()
            : new ArrayList<Role>();
    return new SignUpCommand(
            resource.firstName(),
            resource.lastName(),
            resource.birthDate(),
            resource.email(),
            resource.password(),
            roles
    );
  }
}
