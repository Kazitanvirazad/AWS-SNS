package publisher.sns.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import lombok.extern.slf4j.Slf4j;
import publisher.sns.model.Message;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Slf4j
public class OrderSNSPublisherHandler implements RequestHandler<APIGatewayProxyRequestEvent, String> {
    private static final Region region = Region.of(System.getenv("REGION"));
    private static String topicARN = System.getenv("TOPIC_ARN");

    @Override
    public String handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
        String requestBody = apiGatewayProxyRequestEvent.getBody();
        String response = null;
        try (SnsClient client = SnsClient.builder()
                .region(region).build()) {
            Message message = new Message("Order-Details", requestBody);
            publishMessage(client, topicARN, message);
            response = "Message published successfully!";
        } catch (UnsupportedOperationException exception) {
            log.error(exception.getMessage());
            response = "Error occurred, " + exception.getMessage();
        }
        return response;
    }

    private void publishMessage(SnsClient client, String topicARN, Message message) {
        PublishRequest request = PublishRequest.builder()
                .subject(message.getSubject())
                .message(message.getMessage())
                .topicArn(topicARN)
                .build();
        client.publish(request);
    }
}
