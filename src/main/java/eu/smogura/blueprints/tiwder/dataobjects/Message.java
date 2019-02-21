package eu.smogura.blueprints.tiwder.dataobjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Represents message created by user and which can be posted and seen by other
 * users.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
  /** Id of message. */
  private String messageId;

  /** The time message has been posted. */
  private long postDate;

  /** The body of message in text format. */
  private String messageBody;

  /** Id of user who posted message. */
  private String postingUserId;
}
