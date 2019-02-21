package eu.smogura.blueprints.tiwder.dataobjects;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the full details about user.
 *
 * <br/>
 *
 * This class is composed from {@link UserPublicData} and {@link UserPrivateData}
 * to provide better separation between public and private information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
  /** The public information about user. */
  private UserPublicData publicData;

  /** The private (sensitive) information about user. */
  private UserPrivateData privateData;
}
