LLM Chatbot – Spring Boot (Java)

A Spring Boot–based LLM Chatbot that integrates with the OpenAI Chat Completion API to simulate an IT Customer Support Assistant, built with production-oriented backend design principles.

📌 Project Overview

This project demonstrates how to integrate Large Language Models (LLMs) into a Java backend application using Spring Boot, focusing on:

1. Clean architecture

2. Secure configuration management

3. Predictable LLM behavior

4. Handling real-world latency and timeouts

5. IDE-only execution using Spring Tool Suite (STS)

Rather than a quick demo, this project is designed as a foundation for enterprise-grade GenAI systems.

🎯 What This Chatbot Does

1. Accepts a user query

2. Sends the query to the OpenAI Chat Completion API

3. Applies system instructions so the model behaves like an IT support executive

4. Controls response randomness and length

5. Returns a concise, deterministic response

🧠 Key Features

--> OpenAI Chat API integration

--> Java + Spring Boot Backend

--> Secure API key handling (no hardcoding)

--> Configurable max_tokens

--> Configurable temperature

--> Custom HTTP timeouts

--> Clean separation of concerns

--> IDE-only execution (Spring Tool Suite)

🏗️ Technology Stack


| Layer           | Technology                  |
| --------------- | --------------------------- |
| Language        | Java 17                     |
| Framework       | Spring Boot                 |
| Build Tool      | Maven                       |
| HTTP Client     | OkHttp                      |
| JSON Parsing    | Gson                        |
| LLM Provider    | OpenAI                      |
| IDE             | Spring Tool Suite (Eclipse) |
| Version Control | Git + GitHub                |



📁 Project Structure

llm-chatbot-springboot
├── src/main/java
│   ├── IntentClassifier.java
│   ├── Main.java
│   └── OpenAIClient.java
│
├── src/main/resources
├── pom.xml
├── .gitignore
└── README.md


🧩 Class Responsibilities
Main.java

 --> Application entry point

 --> Bootstraps the Spring context

OpenAIClient.java

 --> Core integration with OpenAI API

 --> Builds request payload (model, messages, tokens, temperature)

 --> Sends HTTP requests using OkHttp

 --> Parses and returns responses

 --> Applies timeout configuration

IntentClassifier.java

 --> Foundation for intent classification

 --> Designed for future routing and escalation logic

🧩 High-Level Architecture

Client --> Spring Boot Application --> OpenAIClient --> OpenAI Chat Completion API



🔄 Request Flow

1. User sends a message

2. Spring Boot receives the request

3. OpenAIClient builds the prompt and configuration

4. Request is sent to OpenAI API

5. Response is parsed and returned

🔐 API Key Management (IMPORTANT)

This project does not store secrets in code or configuration files.

How to configure the API key in STS

1. Open Run → Run Configurations

2. Select your Spring Boot Application

3. Go to the Environment tab

4. Add:
OPENAI_API_KEY=sk-xxxxxxxxxxxxxxxx


5. Click Apply → Run

This ensures:

--> No secrets in GitHub

--> Safe collaboration

--> Production-aligned configuration

🚀 Running the Application (STS Only)

No terminal required.

1. Open the project in Spring Tool Suite

2. Ensure Java 17 is selected

3. Configure OPENAI_API_KEY

4. Click Run

⚙️ OpenAI Configuration
| Parameter   | Value           | Reason                               |
| ----------- | --------------- | ------------------------------------ |
| Model       | `gpt-3.5-turbo` | Stable baseline                      |
| max_tokens  | `200`           | Controls response length & cost      |
| temperature | `0.2`           | Deterministic, support-style replies |

⏱️ Timeout & Reliability Handling

LLM APIs can have variable latency.
This project configures custom HTTP timeouts to avoid hanging requests and improve reliability.

🛡️ Design Decisions

1. Environment variables for secrets: prevents leaks

2. Low temperature: reduces hallucinations

3. Stateless design: easier scaling

4. Explicit timeouts: production readiness

🎤 Interview Talking Points

--> Production-style LLM integration in Java

--> No hardcoded secrets

--> Controlled randomness and output size

--> Timeout handling for external AI APIs

--> Extensible design for future AI workflows

📈 Planned Enhancements

--> Confidence scoring & escalation

--> Intent-based routing

--> Conversation memory

--> Retry & circuit-breaker logic

--> Streaming responses

--> Retrieval-Augmented Generation (RAG)

🧪 Who This Project Is For

--> Java Backend Developers

--> Engineers learning GenAI integration

--> System design interview preparation

--> Developers moving beyond simple API demos

🧑‍💻 Author

Naman Jain
Java | Backend | GenAI

GitHub: https://github.com/namanjain775

📄 License

For learning and demonstration purposes.

⭐ Final Note

This project focuses on engineering discipline, not just calling an API.
It reflects how LLMs are integrated into real-world backend systems.



Below is logs for one such sample conversation
========== OpenAIClient.ask() ==========
Prompt received: You are an IT customer support executive for a SaaS product.
You have deep knowledge of the United States Telecom domain,
including federal regulations and company-specific policies.

Your goals:
- Be polite, concise, and professional
- Provide step-by-step troubleshooting guidance
- Do NOT make assumptions or hallucinate
- If the issue cannot be resolved confidently, recommend escalation

IMPORTANT OUTPUT RULES:
- Respond ONLY in valid JSON
- Do NOT include explanations outside JSON
- The confidence value MUST be a number between 0.0 and 1.0

Output format (STRICT):
{
  "reply": "<your response to the user>",
  "confidence": <number between 0.0 and 1.0>
}

If confidence is below 0.7, the reply MUST mention that escalation
to a human agent is recommended.

User issue:
I am not able to activate my verizon eSim on the iphone 17 that I received yesterday.

✅ OPENAI_API_KEY detected (length=164)
========== buildRequestBody() ==========
🟡 Final Request JSON:
{"model":"gpt-4.1-mini","input":"You are an IT customer support executive for a SaaS product.\nYou have deep knowledge of the United States Telecom domain,\nincluding federal regulations and company-specific policies.\n\nYour goals:\n- Be polite, concise, and professional\n- Provide step-by-step troubleshooting guidance\n- Do NOT make assumptions or hallucinate\n- If the issue cannot be resolved confidently, recommend escalation\n\nIMPORTANT OUTPUT RULES:\n- Respond ONLY in valid JSON\n- Do NOT include explanations outside JSON\n- The confidence value MUST be a number between 0.0 and 1.0\n\nOutput format (STRICT):\n{\n  \"reply\": \"<your response to the user>\",\n  \"confidence\": <number between 0.0 and 1.0>\n}\n\nIf confidence is below 0.7, the reply MUST mention that escalation\nto a human agent is recommended.\n\nUser issue:\nI am not able to activate my verizon eSim on the iphone 17 that I received yesterday.\n","temperature":0.2,"max_output_tokens":200}
➡️ Attempt 1 calling OpenAI
========== callOpenAI() ==========
➡️ OpenAI URL: https://api.openai.com/v1/responses
➡️ HTTP Method: POST
➡️ Headers: Authorization=Bearer *****, Content-Type=application/json
➡️ Request Body:
{"model":"gpt-4.1-mini","input":"You are an IT customer support executive for a SaaS product.\nYou have deep knowledge of the United States Telecom domain,\nincluding federal regulations and company-specific policies.\n\nYour goals:\n- Be polite, concise, and professional\n- Provide step-by-step troubleshooting guidance\n- Do NOT make assumptions or hallucinate\n- If the issue cannot be resolved confidently, recommend escalation\n\nIMPORTANT OUTPUT RULES:\n- Respond ONLY in valid JSON\n- Do NOT include explanations outside JSON\n- The confidence value MUST be a number between 0.0 and 1.0\n\nOutput format (STRICT):\n{\n  \"reply\": \"<your response to the user>\",\n  \"confidence\": <number between 0.0 and 1.0>\n}\n\nIf confidence is below 0.7, the reply MUST mention that escalation\nto a human agent is recommended.\n\nUser issue:\nI am not able to activate my verizon eSim on the iphone 17 that I received yesterday.\n","temperature":0.2,"max_output_tokens":200}
⬅️ HTTP Status Code: 200
⬅️ HTTP Status Message: 
⬅️ Response Headers:
date: Mon, 05 Jan 2026 14:11:35 GMT
content-type: application/json
x-ratelimit-limit-requests: 500
x-ratelimit-limit-tokens: 200000
x-ratelimit-remaining-requests: 499
x-ratelimit-remaining-tokens: 199780
x-ratelimit-reset-requests: 120ms
x-ratelimit-reset-tokens: 66ms
openai-version: 2020-10-01
openai-organization: user-xocmczabytlce1hecnm2r7nc
openai-project: proj_xuUlzjhm8sRmA26o2KYSeeu4
x-request-id: req_1495f3f0dc7a4f71abadc40f5b9c6717
openai-processing-ms: 4101
x-envoy-upstream-service-time: 4106
cf-cache-status: DYNAMIC
set-cookie: ██
strict-transport-security: max-age=31536000; includeSubDomains; preload
x-content-type-options: nosniff
set-cookie: ██
server: cloudflare
cf-ray: 9b9390aa0d78a7b0-DEL
alt-svc: h3=":443"; ma=86400

✅ OpenAI Raw Success Response:
{
  "id": "resp_02f43b0976f5c85a00695bc692e7ec8193a2569dbd8b7f43ee",
  "object": "response",
  "created_at": 1767622290,
  "status": "completed",
  "background": false,
  "billing": {
    "payer": "developer"
  },
  "completed_at": 1767622294,
  "error": null,
  "incomplete_details": null,
  "instructions": null,
  "max_output_tokens": 200,
  "max_tool_calls": null,
  "model": "gpt-4.1-mini-2025-04-14",
  "output": [
    {
      "id": "msg_02f43b0976f5c85a00695bc6943a1881939bd6eb74638c78ed",
      "type": "message",
      "status": "completed",
      "content": [
        {
          "type": "output_text",
          "annotations": [],
          "logprobs": [],
          "text": "{\n  \"reply\": \"To activate your Verizon eSIM on your iPhone 17, please follow these steps: 1. Ensure your iPhone is connected to Wi-Fi or cellular data. 2. Go to Settings > Cellular > Add Cellular Plan. 3. Scan the QR code provided by Verizon or enter the activation code manually. 4. Follow the on-screen instructions to complete activation. 5. Restart your iPhone after activation. If you encounter any errors during this process, please verify that your Verizon account is active and supports eSIM activation. If the issue persists, I recommend contacting Verizon support or escalating this issue to a human agent for further assistance.\",\n  \"confidence\": 0.85\n}"
        }
      ],
      "role": "assistant"
    }
  ],
  "parallel_tool_calls": true,
  "previous_response_id": null,
  "prompt_cache_key": null,
  "prompt_cache_retention": null,
  "reasoning": {
    "effort": null,
    "summary": null
  },
  "safety_identifier": null,
  "service_tier": "default",
  "store": true,
  "temperature": 0.2,
  "text": {
    "format": {
      "type": "text"
    },
    "verbosity": "medium"
  },
  "tool_choice": "auto",
  "tools": [],
  "top_logprobs": 0,
  "top_p": 1.0,
  "truncation": "disabled",
  "usage": {
    "input_tokens": 201,
    "input_tokens_details": {
      "cached_tokens": 0
    },
    "output_tokens": 148,
    "output_tokens_details": {
      "reasoning_tokens": 0
    },
    "total_tokens": 349
  },
  "user": null,
  "metadata": {}
}
========== parseResponse() ==========
Raw JSON length: 2149
🟢 Model text payload:
{
  "reply": "To activate your Verizon eSIM on your iPhone 17, please follow these steps: 1. Ensure your iPhone is connected to Wi-Fi or cellular data. 2. Go to Settings > Cellular > Add Cellular Plan. 3. Scan the QR code provided by Verizon or enter the activation code manually. 4. Follow the on-screen instructions to complete activation. 5. Restart your iPhone after activation. If you encounter any errors during this process, please verify that your Verizon account is active and supports eSIM activation. If the issue persists, I recommend contacting Verizon support or escalating this issue to a human agent for further assistance.",
  "confidence": 0.85
}
✅ Parsed reply: To activate your Verizon eSIM on your iPhone 17, please follow these steps: 1. Ensure your iPhone is connected to Wi-Fi or cellular data. 2. Go to Settings > Cellular > Add Cellular Plan. 3. Scan the QR code provided by Verizon or enter the activation code manually. 4. Follow the on-screen instructions to complete activation. 5. Restart your iPhone after activation. If you encounter any errors during this process, please verify that your Verizon account is active and supports eSIM activation. If the issue persists, I recommend contacting Verizon support or escalating this issue to a human agent for further assistance.
✅ Confidence: 0.85
✅ Token usage: Total Tokens / Prompt Tokens / Completions Tokens - 349 / 201 / 148
