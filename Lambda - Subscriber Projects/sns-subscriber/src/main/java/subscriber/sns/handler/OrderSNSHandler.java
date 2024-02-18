package subscriber.sns.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import subscriber.sns.model.Message;

import java.util.List;
import java.util.UUID;

public class OrderSNSHandler implements RequestHandler<SNSEvent, Boolean> {
    private static final Region region = Region.of(System.getenv("REGION"));
    private static final String dynamoDbTableName = System.getenv("SNSSUBSCRIBEDMESSAGE_TABLE");
    private static final DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(region)
            .build();

    private static final DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();

    @Override
    public Boolean handleRequest(SNSEvent snsEvent, Context context) {
        List<SNSEvent.SNSRecord> records = snsEvent.getRecords();
        for (SNSEvent.SNSRecord record : records) {
            processRecords(record);
        }
        return Boolean.TRUE;
    }

    public void processRecords(SNSEvent.SNSRecord record) {
        SNSEvent.SNS sns = record.getSNS();
        String publishedMessage = sns.getMessage();
        String subject = sns.getSubject();
        String topicARN = sns.getTopicArn();
        Message message = new Message(UUID.randomUUID().toString(), subject, publishedMessage, topicARN);
        DynamoDbTable<Message> table = enhancedClient.table(dynamoDbTableName, TableSchema.fromBean(Message.class));
        try {
            table.putItem(message);
        } catch (UnsupportedOperationException exception) {
            return;
        }
    }
}
