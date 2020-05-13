package kr.ssu.ai_fitness.vo;

import java.util.HashMap;
import java.util.Map;

public class ChatModel {
    //여기서 uid를 String으로 하는 이유는 파이어베이스에서 int형은 에러처리하기 때문이다.
    public Map<String, Boolean> users = new HashMap<>();//uid와 destUser 정보. 즉 채팅방 유저들
    public Map<String, Comment> comments = new HashMap<>();//채팅방의 내용

    public static  class Comment {
        public String uid;
        public String message;
    }

}
