import com.google.cloud.secretmanager.v1.AccessSecretVersionResponse;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretVersionName;

public class GCPPlainSecretReader {

    public static void main(String[] args) throws Exception {
        // 🔁 Replace with your actual project ID, secret ID, and version
        String projectId = "your-gcp-project-id";
        String secretId = "my-plain-secret";
        String versionId = "latest";

        SecretVersionName secretVersionName = SecretVersionName.of(projectId, secretId, versionId);

        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            AccessSecretVersionResponse response = client.accessSecretVersion(secretVersionName);

            // 👇 Directly read the plain string payload
            String secretPayload = response.getPayload().getData().toStringUtf8();
            System.out.println("Plain Secret Value: " + secretPayload);
        }
    }
}
