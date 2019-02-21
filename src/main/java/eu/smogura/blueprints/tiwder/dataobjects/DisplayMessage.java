package eu.smogura.blueprints.tiwder.dataobjects;

import eu.smogura.blueprints.tiwder.controllers.TimelineController;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents message which is returned from {@link TimelineController} and
 * which is used to be presented to user.
 *
 * <br />
 *
 * This class is composition of other objects (like user) to
 * provide better user experience by returning as much as required in
 * one HTTP call.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisplayMessage {
  /** User who posted message. */
  private UserPublicData postingUser;

  /** The message which has been posted. */
  private Message message;
}
