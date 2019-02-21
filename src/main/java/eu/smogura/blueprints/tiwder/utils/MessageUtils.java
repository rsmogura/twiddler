package eu.smogura.blueprints.tiwder.utils;

import eu.smogura.blueprints.tiwder.dataobjects.Message;
import lombok.NonNull;

public class MessageUtils {
  /**
   * Compares two messages by posting date in reverse chronological order.
   *
   * @param m1 the first message
   * @param m2 the second message
   * @return negative if m1 < m2, 0 if m1 == m2, positive if m1 > m2
   */
  public static int compareByPostingDate(@NonNull Message m1, @NonNull Message m2) {
    return (int) (m2.getPostDate() - m1.getPostDate());
  }
}
