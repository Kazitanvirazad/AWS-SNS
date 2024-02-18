package springbootpublisher.sns.app.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import springbootpublisher.sns.app.model.Message;

@Component
@Slf4j
public class SNSPublisherHandler implements CommandLineRunner {
    @Value("${REGION}")
    private String region;

    @Value("${TOPIC_ARN}")
    private String topicARN;

    @Value("${ACCESSKEYID}")
    private String accessKey;

    @Value("${SECRETACCESSKEY}")
    private String secretAccessKey;

    @Override
    public void run(String... args) throws Exception {
        try (SnsClient client = getSnsClient()) {
            Message message = new Message("SpringBoot-Order-Details", "{\"orderId\":105,\"customerId\":\"kazi_env\",\"lineItem\":\"Oneplus Earphone\",\"price\":1500}");
            publishMessage(client, topicARN, message);
        } catch (UnsupportedOperationException exception) {
            log.error(exception.getMessage());
        }
    }

    private void publishMessage(SnsClient snsClient, String topicARN, Message message) {
        PublishRequest request = PublishRequest.builder()
                .subject(message.getSubject())
                .message(message.getMessage())
                .topicArn(topicARN)
                .build();
        snsClient.publish(request);
    }

    public SnsClient getSnsClient() {
        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider
                .create(AwsBasicCredentials.create(accessKey, secretAccessKey));
        return SnsClient.builder()
                .region(getRegion())
                .credentialsProvider(credentialsProvider)
                .build();
    }

    private Region getRegion() {
        return Region.of(region);
    }
}
