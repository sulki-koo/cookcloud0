package cookcloud.openapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.annotation.PostConstruct;
import okhttp3.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChoiceSearchApi {
    @Value("${openai.api.key}")
    private String API_KEY;
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final int MAX_TOKENS = 200;
    private static final int MAX_RETRIES = 3;  // 최대 3번 재시도
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private final List<JsonNode> messageHistory = new ArrayList<>(); // 대화 기록 저장
    
    // 메시지 기록 초기화
    public void resetMessageHistory() {
        messageHistory.clear(); // 기존 메시지 삭제
        messageHistory.add(objectMapper.createObjectNode()
                .put("role", "system")
                .put("content", "You are a helpful assistant."));
    }

    public String getChatGPTResponse(String prompt) throws IOException {
        
        // 요청 전에 메시지 초기화
        resetMessageHistory();
        
        StringBuilder fullResponse = new StringBuilder();

        int attempt = 0;
        while (attempt < MAX_RETRIES) {
            attempt++; // 요청 횟수 증가

            // 이전 대화 기록 유지
            if (messageHistory.isEmpty()) {
                messageHistory.add(objectMapper.createObjectNode().put("role", "system").put("content", "You are a helpful assistant."));
            }
            
            // 새로운 질문 추가
            messageHistory.add(objectMapper.createObjectNode().put("role", "user").put("content", prompt));

         // JSON 요청 본문 구성
            ObjectNode requestJson = objectMapper.createObjectNode();
            requestJson.put("model", "gpt-4");
            requestJson.set("messages", objectMapper.valueToTree(messageHistory));
            requestJson.put("max_tokens", MAX_TOKENS);

            String json = requestJson.toString();  // 최종 JSON 문자열 변환

            RequestBody body = RequestBody.create(json, MediaType.get("application/json"));

            Request request = new Request.Builder()
                    .url(API_URL)
                    .header("Authorization", "Bearer " + API_KEY)
                    .header("Content-Type", "application/json")
                    .post(body)
                    .build();

            System.out.println("요청 헤더 Authorization: Bearer " + API_KEY);

            try (Response response = client.newCall(request).execute()) {
                System.out.println("응답 코드: " + response.code());

                if (!response.isSuccessful()) {
                    System.out.println("API 요청 실패: " + response.message());
                    continue; // 재시도
                }

                JsonNode jsonResponse = objectMapper.readTree(response.body().string());
                String newContent = jsonResponse.get("choices").get(0).get("message").get("content").asText().replaceAll("\\n", "<br>");
                fullResponse.append(newContent);
                
             // AI 응답을 기록에 추가
                messageHistory.add(objectMapper.createObjectNode().put("role", "assistant").put("content", newContent));

                return fullResponse.toString(); 
            } catch (IOException e) {
                System.out.println("API 호출 중 오류 발생: " + e.getMessage());
            }
        }

        return "AI 응답을 받을 수 없습니다."; // 모든 시도 실패 시 기본 메시지 반환
    }
    
}
