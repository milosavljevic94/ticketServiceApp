package com.ftn.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.ftn.dtos.LoggedInUserDTO;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LoggedInUserSerializer extends JsonDeserializer<LoggedInUserDTO> {

    @Override
    public LoggedInUserDTO deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        LoggedInUserDTO logged = new LoggedInUserDTO();

        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);

        logged.setId(node.get("id").longValue());
        logged.setToken(node.get("token").textValue());
        logged.setUsername(node.get("username").textValue());
        logged.setEmail(node.get("email").textValue());

        Iterator<JsonNode> elements = node.get("authorities").elements();
        while (elements.hasNext()){
            JsonNode next = elements.next();
            JsonNode authority = next.get("authority");
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(authority.asText()));
            logged.setAuthorities(authorities);
        }
        return logged;
    }
}
