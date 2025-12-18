package com.tnh.baseware.core.mains;

import com.tnh.baseware.core.components.ExternalApiClient;
import com.tnh.baseware.core.exceptions.BasewareCoreException;
import com.tnh.baseware.core.properties.ExternalApiProperties;
import com.tnh.baseware.core.services.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class InitCameraDevice {

    private static final String BASE_URL = "https://ivms-api.vienthongtayninh.vn/api/v1/camera-devices";
    private static final String TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdGFyY29tIiwidCI6Iml2bXMtYXBpLnZpZW50aG9uZ3RheW5pbmgudm4iLCJpc3MiOiJzdGFyY29tIiwiZXhwIjoxNzUwMzg2MTk2LCJpYXQiOjE3NTAzODI1OTYsImp0aSI6IjBjNDlhZDIyLTJkYTEtNGU4OS04MzU2LTliNzEyZWIxYzg2ZCIsInNpZCI6IjMxYTI0YTAyLTA0MGYtNDczNi04ZmQyLWI0ZGIwNzYyMDFmMyJ9.4q6a0pLtSFF92gFIxM2-zlpl9vx1-9JWPbp72vYmLkSMaRDho32X99297PwFnynKUJ52WkZdQe-k4bwGuHxRfw";

    private final ExternalApiClient externalApiClient;

    public InitCameraDevice(ExternalApiClient externalApiClient) {
        this.externalApiClient = externalApiClient;
    }

    public static void main(String[] args) {
        WebClient.Builder builder = WebClient.builder();

        MessageService messageService = new MessageService(null) {
            @Override
            public String getMessage(String code, Object... args) {
                return "[Mock message] " + code;
            }
        };

        ExternalApiProperties properties = new ExternalApiProperties();
        properties.setRequestTimeoutSeconds(10);
        properties.setCircuitBreakerEnabled(false);

        ExternalApiClient externalApiClient = new ExternalApiClient(builder, messageService, properties);
        InitCameraDevice runner = new InitCameraDevice(externalApiClient);
        runner.run();
    }

    public void run() {
        Map<String, String> headers = Map.of(
                "Authorization", "Bearer " + TOKEN,
                "Content-Type", "application/json",
                "Accept-Language", "en"
        );

        for (int i = 1; i <= 32; i++) {
            var channel = String.format("%d01", i);
            var name = "NVR42-Cam" + channel;
            var streamUrl = "rtsp://admin:nVr@22262@10.10.0.42:554/Streaming/Channels/" + channel;
            var playbackUrl = "rtsp://admin:nVr@22262@10.10.0.42:554/Streaming/Tracks/" + channel;

            log.info("name: {}", name);
            log.info("streamUrl: {}", streamUrl);
            log.info("playbackUrl: {}", playbackUrl);

            Map<String, Object> device = new HashMap<>();
            device.put("name", name);
            device.put("channel", 1);
            device.put("port", "8000");
            device.put("username", "admin");
            device.put("password", "nVr@22262");
            device.put("stream_url", streamUrl);
            device.put("playback_url", playbackUrl);
            device.put("type", "PTZ");
            device.put("status", "OFFLINE");
            device.put("vendor", "HIKVISION");

            try {
                externalApiClient.callApiSync(BASE_URL, HttpMethod.POST, headers, device, Void.class);
                log.info("✅ Created camera: {}", device.get("name"));
            } catch (BasewareCoreException ex) {
                log.error("❌ Failed to create camera {}: {}", device.get("name"), ex.getMessage());
            }
        }
    }
}
