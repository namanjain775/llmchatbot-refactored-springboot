package com.namanjain.llmchatbot.dto;

public class ChatResponse {

    private String reply;
    private double confidence;
    private TokensUsageDTO tokenUsage;

    public ChatResponse() {}

    public ChatResponse(String reply, double confidence) {
        this.reply = reply;
        this.confidence = confidence;
    }

    public ChatResponse(String reply, double confidence, TokensUsageDTO tokenUsage) {
        this.reply = reply;
        this.confidence = confidence;
        this.tokenUsage = tokenUsage;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    // ✅ THIS IS WHAT WAS MISSING
    public TokensUsageDTO getTokenUsage() {
        return tokenUsage;
    }

    public void setTokenUsage(TokensUsageDTO tokenUsage) {
        this.tokenUsage = tokenUsage;
    }
}
