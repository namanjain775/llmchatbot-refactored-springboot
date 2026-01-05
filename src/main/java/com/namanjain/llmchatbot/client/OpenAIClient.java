//package com.namanjain.llmchatbot.client;
//
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.google.gson.JsonParseException;
//import com.namanjain.llmchatbot.dto.ChatResponse;
//import com.namanjain.llmchatbot.dto.TokensUsageDTO;
//import okhttp3.*;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.concurrent.TimeUnit;
//
///**
// * OpenAIClient
// *
// * Responsible for:
// *  - Communicating with OpenAI Responses API
// *  - Sending prompt input to the model
// *  - Parsing structured model output (reply + confidence)
// *  - Extracting authoritative token usage
// *
// * This class intentionally contains NO business logic.
// * It is a pure external API integration layer.
// */
//@Component
//public class OpenAIClient {
//
//    /* ===================== Configuration ===================== */
//
//    private static final String API_URL = "https://api.openai.com/v1/responses";
//    private static final String MODEL = "gpt-4.1-mini";
//    private static final double TEMPERATURE = 0.2;
//    private static final int MAX_OUTPUT_TOKENS = 200;
//
//    /* ===================== HTTP Client ===================== */
//
//    /**
//     * Reusable, thread-safe HTTP client.
//     * Timeouts are intentionally high to accommodate LLM latency.
//     */
//    private final OkHttpClient httpClient = new OkHttpClient.Builder()
//            .connectTimeout(90, TimeUnit.SECONDS)
//            .readTimeout(90, TimeUnit.SECONDS)
//            .build();
//
//    /* ===================== Public API ===================== */
//
//    /**
//     * Sends a prompt to OpenAI and returns a structured ChatResponse.
//     *
//     * @param prompt Fully constructed prompt (from PromptBuilder)
//     * @return ChatResponse containing reply, model confidence, and token usage
//     */
//    public ChatResponse ask(String prompt) {
//
//        /* -------- API Key Validation -------- */
//
//        String apiKey = System.getenv("OPENAI_API_KEY");
//        if (apiKey == null || apiKey.isBlank()) {
//            throw new IllegalStateException("OPENAI_API_KEY not set in environment variables");
//        }
//
//        /* -------- Build Request Body (Responses API) -------- */
//
//        JsonObject requestBody = new JsonObject();
//        requestBody.addProperty("model", MODEL);
//        requestBody.addProperty("input", prompt); // Responses API uses `input`
//        requestBody.addProperty("temperature", TEMPERATURE);
//        requestBody.addProperty("max_output_tokens", MAX_OUTPUT_TOKENS);
//
//        Request request = new Request.Builder()
//                .url(API_URL)
//                .addHeader("Authorization", "Bearer " + apiKey)
//                .addHeader("Content-Type", "application/json")
//                .post(RequestBody.create(
//                        requestBody.toString(),
//                        MediaType.parse("application/json")
//                ))
//                .build();
//
//        /* -------- Execute Request -------- */
//
//        try (Response response = httpClient.newCall(request).execute()) {
//
//            if (!response.isSuccessful()) {
//                throw new RuntimeException("OpenAI API error: HTTP " + response.code());
//            }
//
//            /* -------- Read Raw Response (ONLY ONCE) -------- */
//
//            String rawResponse = response.body().string();
//            // Prefer logger in real systems
//            System.out.println("🔍 OpenAI Raw Response:\n" + rawResponse);
//
//            JsonObject root = JsonParser.parseString(rawResponse).getAsJsonObject();
//
//            /* -------- Extract Model Output -------- */
//
//            JsonArray outputArray = root.getAsJsonArray("output");
//            if (outputArray == null || outputArray.isEmpty()) {
//                throw new RuntimeException("OpenAI response missing output array");
//            }
//
//            JsonObject messageObject = outputArray.get(0).getAsJsonObject();
//            JsonArray contentArray = messageObject.getAsJsonArray("content");
//
//            if (contentArray == null || contentArray.isEmpty()) {
//                throw new RuntimeException("OpenAI response missing content");
//            }
//
//            String modelText = contentArray
//                    .get(0).getAsJsonObject()
//                    .get("text")
//                    .getAsString();
//
//            /* -------- Parse Model-Generated JSON -------- */
//
//            JsonObject modelJson = JsonParser.parseString(modelText).getAsJsonObject();
//
//            String reply = modelJson.get("reply").getAsString();
//            double confidence = modelJson.get("confidence").getAsDouble();
//
//            /* -------- Extract Token Usage -------- */
//
//            JsonObject usageJson = root.getAsJsonObject("usage");
//
//            TokensUsageDTO tokenUsage = new TokensUsageDTO(
//                    usageJson.get("input_tokens").getAsInt(),
//                    usageJson.get("output_tokens").getAsInt(),
//                    usageJson.get("total_tokens").getAsInt()
//            );
//
//            /* -------- Return API Response DTO -------- */
//
//            return new ChatResponse(reply, confidence, tokenUsage);
//
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to call OpenAI API", e);
//        } catch (JsonParseException e) {
//            throw new RuntimeException("Model returned invalid JSON output", e);
//        }
//    }
//}


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

        String apiKey = System.getenv("OPENAI_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("OPENAI_API_KEY not set");
        }

        JsonObject requestBody = buildRequestBody(prompt);

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                return callOpenAI(requestBody, apiKey);
            } catch (IOException e) {

                if (attempt >= MAX_RETRIES) {
                    throw new RuntimeException("OpenAI call failed after retries", e);
                }

                sleepWithBackoff(attempt);
            }
        }

        throw new IllegalStateException("Unreachable code");
    }

    /* ===================== Core API Call ===================== */

    private ChatResponse callOpenAI(JsonObject requestBody, String apiKey) throws IOException {

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(
                        requestBody.toString(),
                        MediaType.parse("application/json")
                ))
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                int code = response.code();

                // Retry only for transient errors
                if (code == 429 || code >= 500) {
                    throw new IOException("Transient OpenAI error: " + code);
                }

                throw new RuntimeException("OpenAI API error: HTTP " + code);
            }

            String rawResponse = response.body().string();
            System.out.println("🔍 OpenAI Raw Response:\n" + rawResponse);

            return parseResponse(rawResponse);
        }
    }

    /* ===================== Response Parsing ===================== */

    private ChatResponse parseResponse(String rawJson) {

        JsonObject root = JsonParser.parseString(rawJson).getAsJsonObject();

        JsonArray outputArray = root.getAsJsonArray("output");
        if (outputArray == null || outputArray.isEmpty()) {
            throw new RuntimeException("Missing output from OpenAI");
        }

        JsonObject message = outputArray.get(0).getAsJsonObject();
        JsonArray content = message.getAsJsonArray("content");

        if (content == null || content.isEmpty()) {
            throw new RuntimeException("Empty model response");
        }

        String modelText = content.get(0).getAsJsonObject()
                .get("text")
                .getAsString();

        JsonObject modelJson = JsonParser.parseString(modelText).getAsJsonObject();

        String reply = modelJson.get("reply").getAsString();
        double confidence = modelJson.get("confidence").getAsDouble();

        JsonObject usage = root.getAsJsonObject("usage");

        TokensUsageDTO tokens = new TokensUsageDTO(
                usage.get("input_tokens").getAsInt(),
                usage.get("output_tokens").getAsInt(),
                usage.get("total_tokens").getAsInt()
        );

        return new ChatResponse(reply, confidence, tokens);
    }

    /* ===================== Retry Backoff ===================== */

    private void sleepWithBackoff(int attempt) {
        try {
            long delay = BASE_RETRY_DELAY_MS * attempt;
            Thread.sleep(delay);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    /* ===================== Request Builder ===================== */

    private JsonObject buildRequestBody(String prompt) {
        JsonObject body = new JsonObject();
        body.addProperty("model", MODEL);
        body.addProperty("input", prompt);
        body.addProperty("temperature", TEMPERATURE);
        body.addProperty("max_output_tokens", MAX_OUTPUT_TOKENS);
        return body;
    }
}
