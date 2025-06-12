package com.altix.ezpark.iam.domain.services;

import com.altix.ezpark.iam.domain.model.commands.SeedRolesCommand;

public interface RoleCommandService {
  void handle(SeedRolesCommand command);
}
