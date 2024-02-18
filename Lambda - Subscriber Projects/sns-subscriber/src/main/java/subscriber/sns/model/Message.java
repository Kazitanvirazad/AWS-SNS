package subscriber.sns.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamoDbBean
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 2022330617247297792L;

    private String id;
    private String subject;
    private String message;
    private String topicARN;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }
}
