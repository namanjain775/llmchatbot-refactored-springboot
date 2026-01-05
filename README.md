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
