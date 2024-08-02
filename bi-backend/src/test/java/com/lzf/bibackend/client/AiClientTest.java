package com.lzf.bibackend.client;

import com.lzf.bibackend.model.dto.ai.DoChatRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AiClientTest {
    @Autowired
    private AiClient aiClient;

    @Test
    void doChat() {
        DoChatRequest request = new DoChatRequest();
        request.setQuestion("未来网页会怎么样?");
        request.setData("日期,访问量,点击率\n" +
                "2024/1/1,102938,12434\n" +
                "2024/1/2,145934,3452\n" +
                "2024/1/3,345338,124151\n" +
                "2024/1/4,198038,43543\n" +
                "2024/1/5,923476,354432\n" +
                "2024/1/6,1243253,2345\n" +
                "2024/1/7,112438,145234\n" +
                "2024/1/8,235938,534542");
        System.out.println(aiClient.doChat(request));
    }
}