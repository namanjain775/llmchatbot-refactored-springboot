package com.namanjain.llmchatbot.client;

import com.google.gson.*;
import com.namanjain.llmchatbot.dto.ChatResponse;
import com.namanjain.llmchatbot.dto.TokensUsageDTO;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class OpenAIClient {

    /* ===================== Configuration ===================== */

    private static final String API_URL = "https://api.openai.com/v1/responses";
    private static final String MODEL = "gpt-4.1-mini";
    private static final double TEMPERATURE = 0.2;
    private static final int MAX_OUTPUT_TOKENS = 200;

    private static final int MAX_RETRIES = 3;
    private static final long BASE_RETRY_DELAY_MS = 800;

    /* ===================== HTTP Client ===================== */

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .build();

    /* ===================== Public API ===================== */

    public ChatResponse ask(String prompt) {

        System.out.println("========== OpenAIClient.ask() ==========");
        System.out.println("Prompt received: " + prompt);

        String apiKey = System.getenv("OPENAI_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            System.out.println("❌ OPENAI_API_KEY NOT FOUND in environment");
            throw new IllegalStateException("OPENAI_API_KEY not set");
        }

        System.out.println("✅ OPENAI_API_KEY detected (length=" + apiKey.length() + ")");

        JsonObject requestBody = buildRequestBody(prompt);
        System.out.println("🟡 Final Request JSON:\n" + requestBody.toString());

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                System.out.println("➡️ Attempt " + attempt + " calling OpenAI");
                return callOpenAI(requestBody, apiKey);
            } catch (IOException e) {

                System.out.println("⚠️ IOException on attempt " + attempt);
                e.printStackTrace(System.out);

                if (attempt >= MAX_RETRIES) {
                    System.out.println("❌ Max retries reached. Failing.");
                    throw new RuntimeException("OpenAI call failed after retries", e);
                }

                sleepWithBackoff(attempt);
            }
        }

        throw new IllegalStateException("Unreachable code");
    }

    /* ===================== Core API Call ===================== */

    private ChatResponse callOpenAI(JsonObject requestBody, String apiKey) throws IOException {

        System.out.println("========== callOpenAI() ==========");

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(
                        requestBody.toString(),
                        MediaType.parse("application/json")
                ))
                .build();

        System.out.println("➡️ OpenAI URL: " + API_URL);
        System.out.println("➡️ HTTP Method: POST");
        System.out.println("➡️ Headers: Authorization=Bearer *****, Content-Type=application/json");
        System.out.println("➡️ Request Body:\n" + requestBody.toString());

        try (Response response = httpClient.newCall(request).execute()) {

            System.out.println("⬅️ HTTP Status Code: " + response.code());
            System.out.println("⬅️ HTTP Status Message: " + response.message());
            System.out.println("⬅️ Response Headers:\n" + response.headers());

            if (!response.isSuccessful()) {

                String errorBody = response.body() != null ? response.body().string() : "<empty>";
                System.out.println("❌ OpenAI Error Response Body:\n" + errorBody);

                int code = response.code();

                // Retry only for transient errors
                if (code == 429 || code >= 500) {
                    System.out.println("🔁 Retriable error detected (code=" + code + ")");
                    throw new IOException("Transient OpenAI error: HTTP " + code);
                }

                throw new RuntimeException("OpenAI API error: HTTP " + code + "\nBody:\n" + errorBody);
            }

            String rawResponse = response.body().string();
            System.out.println("✅ OpenAI Raw Success Response:\n" + rawResponse);

            return parseResponse(rawResponse);
        }
    }

    /* ===================== Response Parsing ===================== */

    private ChatResponse parseResponse(String rawJson) {

        System.out.println("========== parseResponse() ==========");
        System.out.println("Raw JSON length: " + rawJson.length());

        JsonObject root = JsonParser.parseString(rawJson).getAsJsonObject();

        JsonArray outputArray = root.getAsJsonArray("output");
        if (outputArray == null || outputArray.isEmpty()) {
            System.out.println("❌ Missing or empty 'output' array");
            throw new RuntimeException("Missing output from OpenAI");
        }

        JsonObject message = outputArray.get(0).getAsJsonObject();
        JsonArray content = message.getAsJsonArray("content");

        if (content == null || content.isEmpty()) {
            System.out.println("❌ Empty 'content' array in output");
            throw new RuntimeException("Empty model response");
        }

        String modelText = content.get(0).getAsJsonObject()
                .get("text")
                .getAsString();

        System.out.println("🟢 Model text payload:\n" + modelText);

        JsonObject modelJson = JsonParser.parseString(modelText).getAsJsonObject();

        String reply = modelJson.get("reply").getAsString();
        double confidence = modelJson.get("confidence").getAsDouble();

        JsonObject usage = root.getAsJsonObject("usage");

        TokensUsageDTO tokens = new TokensUsageDTO(
                usage.get("input_tokens").getAsInt(),
                usage.get("output_tokens").getAsInt(),
                usage.get("total_tokens").getAsInt()
        );

        System.out.println("✅ Parsed reply: " + reply);
        System.out.println("✅ Confidence: " + confidence);
        System.out.println("✅ Token usage: Total Tokens / Prompt Tokens / Completions Tokens - " + tokens.getTotalTokens() + " / " + tokens.getPromptTokens() + " / " + tokens.getCompletionTokens());

        return new ChatResponse(reply, confidence, tokens);
    }

    /* ===================== Retry Backoff ===================== */

    private void sleepWithBackoff(int attempt) {

        long delay = BASE_RETRY_DELAY_MS * attempt;
        System.out.println("⏳ Backoff sleep: " + delay + " ms");

        try {
            Thread.sleep(delay);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    /* ===================== Request Builder ===================== */

    private JsonObject buildRequestBody(String prompt) {

        System.out.println("========== buildRequestBody() ==========");

        JsonObject body = new JsonObject();
        body.addProperty("model", MODEL);
        body.addProperty("input", prompt);
        body.addProperty("temperature", TEMPERATURE);
        body.addProperty("max_output_tokens", MAX_OUTPUT_TOKENS);

        return body;
    }
}
