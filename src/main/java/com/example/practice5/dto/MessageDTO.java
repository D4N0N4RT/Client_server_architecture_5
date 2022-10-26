package com.example.practice5.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Builder
public class MessageDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String sender;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String receiver;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy")
    private LocalDateTime time;
}
