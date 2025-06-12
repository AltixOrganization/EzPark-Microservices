package com.altix.ezpark.iam.application.internal.commandservices;

import com.altix.ezpark.iam.application.dtos.UserSignedUpEvent;
import com.altix.ezpark.iam.application.internal.outboundservices.hashing.HashingService;
import com.altix.ezpark.iam.application.internal.outboundservices.tokens.TokenService;
import com.altix.ezpark.iam.domain.model.aggregates.User;
import com.altix.ezpark.iam.domain.model.commands.SignInCommand;
import com.altix.ezpark.iam.domain.model.commands.SignUpCommand;
import com.altix.ezpark.iam.domain.model.exceptions.EmailAlreadyExistsException;
import com.altix.ezpark.iam.domain.model.exceptions.InvalidCredentialsException;
import com.altix.ezpark.iam.domain.model.exceptions.RoleNotFoundException;
import com.altix.ezpark.iam.domain.model.exceptions.UserNotFoundException;
import com.altix.ezpark.iam.domain.model.valueobjects.Roles;
import com.altix.ezpark.iam.domain.services.UserCommandService;
import com.altix.ezpark.iam.infrastructure.messaging.IamKafkaConfig;
import com.altix.ezpark.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.altix.ezpark.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.altix.ezpark.shared.infrastructure.messaging.KafkaEventPublisher;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import java.util.List;

/**
 * User command service implementation
 * <p>
 *     This class implements the {@link UserCommandService} interface and provides the implementation for the
 *     {@link SignInCommand} and {@link SignUpCommand} commands.
 * </p>
 */
@AllArgsConstructor
@Service
public class UserCommandServiceImpl implements UserCommandService {

  private final UserRepository userRepository;
  private final HashingService hashingService;
  private final TokenService tokenService;

  private final RoleRepository roleRepository;
  private final KafkaEventPublisher kafkaEventPublisher;


  /**
   * Handle the sign-in command
   * <p>
   *     This method handles the {@link SignInCommand} command and returns the user and the token.
   * </p>
   * @param command the sign-in command containing the username and password
   * @return and optional containing the user matching the username and the generated token
   * @throws RuntimeException if the user is not found or the password is invalid
   */
  @Transactional
  @Override
  public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {
    var user = userRepository.findByEmail(command.email());
    if (user.isEmpty())
      throw new UserNotFoundException();
    if (!hashingService.matches(command.password(), user.get().getPassword()))
      throw new InvalidCredentialsException();

    var token = tokenService.generateToken(user.get().getId());
    return Optional.of(ImmutablePair.of(user.get(), token));
  }

  /**
   * Handle the sign-up command
   * <p>
   *     This method handles the {@link SignUpCommand} command and returns the user.
   * </p>
   * @param command the sign-up command containing the username and password
   * @return the created user
   */
  @Transactional
  @Override
  public Optional<User> handle(SignUpCommand command) {
    if (userRepository.existsByEmail(command.email()))
      throw new EmailAlreadyExistsException();
    var roles = command.roles().stream()
            .map(role ->
                    roleRepository.findByName(role.getName())
                            .orElseThrow(RoleNotFoundException::new))
            .toList();
    if (roles.isEmpty()) {
      roles = List.of(roleRepository.findByName(Roles.ROLE_GUEST)
              .orElseThrow(RoleNotFoundException::new));
    }
    var user = new User(command.email(), hashingService.encode(command.password()), roles);
    var savedUser = userRepository.save(user);
    var event = new UserSignedUpEvent(
            user.getId(),
            command.firstName(),
            command.lastName(),
            command.birthDate(),
            command.email()
    );
    kafkaEventPublisher.publish(IamKafkaConfig.TOPIC_USER_SIGNED_UP, event);
    return userRepository.findByEmail(command.email());
  }
}
