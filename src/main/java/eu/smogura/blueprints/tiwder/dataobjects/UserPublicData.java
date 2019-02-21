package eu.smogura.blueprints.tiwder.dataobjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Basic information of user which can be presented to public. Those are information
 * like user name, image URL.
 *
 * <b>
 * Implementation note: This class should not contain any private or
 * sensitive information about user (i.e. e-mail) as it's used in public catalog
 * </b>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPublicData {
  /** The id of user (system id). */
  private String userId;

  /** User's name. */
  private String userName;

  /** User's image URL. */
  private String userImage;
}
