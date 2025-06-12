package com.altix.ezpark.iam.interfaces.rest.transform;

import com.altix.ezpark.iam.domain.model.commands.SignInCommand;
import com.altix.ezpark.iam.interfaces.rest.resources.SignInResource;

public class SignInCommandFromResourceAssembler {

  public static SignInCommand toCommandFromResource(SignInResource signInResource) {
    return new SignInCommand(signInResource.email(), signInResource.password());
  }
}
