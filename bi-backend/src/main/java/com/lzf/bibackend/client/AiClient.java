package com.lzf.bibackend.client;

import com.lzf.bibackend.model.dto.ai.DoChatRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionChoice;
import lombok.Data;
import okhttp3.*;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class AiClient {

    @Value("${openai.api.end_point_id}")
    private String endpointId;

    @Value("${openai.api.key}")
    private String apiKey;

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS) // 连接超时
            .writeTimeout(5, TimeUnit.SECONDS) // 写入超时
            .readTimeout(5, TimeUnit.SECONDS) // 读取超时
            .build();

    public String doChat(DoChatRequest req) {

        if(StringUtils.isBlank(endpointId) || StringUtils.isBlank(apiKey)){
            throw new IllegalArgumentException("endpointId 和 apiKey 必须被设置");
        }


        ArkService service = new ArkService(apiKey);

        final List<ChatMessage> messages = new ArrayList<>();
        String roleContent = "你是一个数据分析师和前端开发专家, 接下来我会按照以下固定格式给你提供内容.";
        String promptContent = getContentPrompt(req.getData(), req.getQuestion(), req.getChatType());

        final ChatMessage systemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content(roleContent).build();
        final ChatMessage userMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(promptContent).build();

        messages.add(systemMessage);
        messages.add(userMessage);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(endpointId) // 替换为实际的端点 ID
                .messages(messages)
                .build();

        List<ChatCompletionChoice> choices;
        try {
            choices = service.createChatCompletion(chatCompletionRequest).getChoices();
        } catch (Exception e) {
            e.printStackTrace();
            return "API调用失败：" + e.getMessage();
        }

        StringBuilder responseContent = new StringBuilder();
        for (ChatCompletionChoice choice : choices) {
            responseContent.append(choice.getMessage().getContent()).append("\n");
        }

        // 关闭服务
        service.shutdownExecutor();

        return responseContent.toString();
    }

    private String getContentPrompt(String data, String question, String chatType) {
        StringBuilder sb = new StringBuilder();
        sb.append("分析需求:\n ");
        sb.append(question);
        if (chatType != null && !chatType.equals("")) {
            sb.append("\n请使用").append(chatType).append("进行分析.\n");
        }
        sb.append("\n{数据分析的需求或者目标}\n数据:\n");
        sb.append(data);
        sb.append("\n{csv格式的数据，用逗号分隔}\n");
        sb.append("请根据这两部分内容, 按照以下指定格式生成内容(此外不要输出任何多余的开头、结尾、注释)\n");
        sb.append("++++\n" +
                "{前端 Echarts V5的option配置对象js代码，合理地将数据进行可视化，不要生成任何多余的内容，比如注释}\n" +
                "++++\n");
        sb.append("输出要求: {Echarts V5的option配置对象JS代码} 数据分析结论: {详细的数据分析结论}");
        return sb.toString();
    }
}