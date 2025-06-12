package com.altix.ezpark.iam.application.internal.outboundservices.tokens;

/**
 * TokenService interface
 * This interface is used to generate and validate tokens
 */
public interface TokenService {

  /**
   * Generate a token for a given userId
   * @param userId the userId
   * @return String the token
   */
  String generateToken(Long userId);

  /**
   * Extract the username from a token
   * @param token the token
   * @return String the username
   */
  Long getUserIdFromToken(String token);

  /**
   * Validate a token
   * @param token the token
   * @return boolean true if the token is valid, false otherwise
   */
  boolean validateToken(String token);
}
