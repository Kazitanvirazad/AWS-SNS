package springbootpublisher.sns.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 5649986498314666774L;
    private String subject;
    private String message;
}
