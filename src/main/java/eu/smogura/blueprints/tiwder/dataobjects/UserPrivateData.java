package eu.smogura.blueprints.tiwder.dataobjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents private data (like e-mail) about user. Those are data which should not be
 * exposed to public.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPrivateData {
  /** User e-mail. */
  private String email;
}
