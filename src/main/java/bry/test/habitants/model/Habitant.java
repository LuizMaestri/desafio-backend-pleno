package bry.test.habitants.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;
import org.springframework.data.annotation.Id;

@Value
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Habitant {

    @Id
    @Wither
    Long id;
    String code;
    String name;
}
