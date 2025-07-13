package com.finalYearProject.product.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeminiChatBotApiService {


    public  Map<String ,String> sendMessageToGemini(String message) {


        String systemPrompt = "Sen sürdürülebilir moda ve çevre bilinci konusunda uzman bir sohbet botusun. Sadece sürdürülebilir moda, çevre dostu giyim, adil ticaret, geri dönüşüm, upcycling, döngüsel ekonomi, malzeme sürdürülebilirliği, etik üretim, su ayak izi, karbon ayak izi ve ilgili konularda bilgi sağlayabilirsin. Başka konularda soru sorulduğunda, nazikçe konunun dışına çıktığını ve sadece sürdürülebilir moda hakkında konuşabileceğini belirt. Yanıtların arkadaş canlısı ve bilgilendirici olsun ,sürdürülebilir kıyafet,ürün,hakkında her şeyi cevapla.Kıyafet bağış,takas yerleri varsa onlar hakkında da sorulursa bigi ver.-" +
                " ve **maksimum 100 karakter uzunluğunda olsun.**";
        List<Map<String, Object>> contents = new ArrayList<>();
        contents.add(Map.of("role", "user", "parts", List.of(Map.of("text", systemPrompt))));
        contents.add(Map.of("role", "model", "parts", List.of(Map.of("text", "Merhaba! Sürdürülebilir moda hakkında merak ettiğin her şeyi bana sorabilirsin. Sana yardımcı olmak için buradayım."))));
        contents.add(Map.of("role", "user", "parts", List.of(Map.of("text", message))));
        String geminiApiKey = "AIzaSyCeeiJYTmRB-dBpiMQ-FolchJ70Sawp6VA";
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> requestBody = Map.of("contents", contents);

        String geminiApiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey;
        String responseText = "";
        ResponseEntity<Map> response = restTemplate.postForEntity(geminiApiUrl, requestBody, Map.class);
        // responseBody, API'den gelen JSON'ın Map<String, Object> hali
        Map<String, Object> responseBody = response.getBody();

// 1. "candidates" listesine eriş
        List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");

// 2. candidates listesinin ilk elemanına (ilk adaya) eriş
        if (candidates != null && !candidates.isEmpty()) {
            Map<String, Object> firstCandidate = candidates.get(0);

            // 3. "content" map'ine eriş
            Map<String, Object> content = (Map<String, Object>) firstCandidate.get("content");

            // 4. "parts" listesine eriş
            if (content != null) {
                List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");

                // 5. parts listesinin ilk elemanına (ilk parçaya) eriş
                if (parts != null && !parts.isEmpty()) {
                    Map<String, Object> firstPart = parts.get(0);

                    // 6. "text" alanına eriş ve String olarak çek
                    responseText = (String) firstPart.get("text");
                }
            }
        }
        Map<String ,String>responseMap =new HashMap<>();
        responseMap.put("message",responseText);
        return responseMap;
    }
}
