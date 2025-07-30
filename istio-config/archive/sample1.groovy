import com.google.cloud.secretmanager.v1.AccessSecretVersionResponse;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretVersionName;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GCPSecretJsonExtractor {

    public static void main(String[] args) throws Exception {
        // 🔁 Replace these with your actual values
        String projectId = "your-gcp-project-id";
        String secretId = "my-secret-json";
        String versionId = "latest";

        SecretVersionName secretVersionName = SecretVersionName.of(projectId, secretId, versionId);

        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            AccessSecretVersionResponse response = client.accessSecretVersion(secretVersionName);

            // 👇 Get the secret payload (as UTF-8 string)
            String payload = response.getPayload().getData().toStringUtf8();
            System.out.println("Raw JSON Secret: " + payload);

            // 🧠 Parse it as JSON and print each key-value
            JsonObject json = JsonParser.parseString(payload).getAsJsonObject();
            for (String key : json.keySet()) {
                System.out.println("Key: " + key + " | Value: " + json.get(key).getAsString());
            }
        }
    }
}
