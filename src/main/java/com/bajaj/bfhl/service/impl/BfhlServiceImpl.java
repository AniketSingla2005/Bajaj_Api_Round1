package com.bajaj.bfhl.service.impl;

import com.bajaj.bfhl.dto.BfhlRequest;
import com.bajaj.bfhl.dto.BfhlResponse;
import com.bajaj.bfhl.service.BfhlService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BfhlServiceImpl implements BfhlService {

    @Value("${bfhl.user-id}")
    private String userId;

    @Value("${bfhl.email}")
    private String email;

    @Value("${bfhl.roll-number}")
    private String rollNumber;

    @Override
    public BfhlResponse processData(BfhlRequest request) {
        List<String> data = request.getData();

        List<String> oddNumbers = new ArrayList<>();
        List<String> evenNumbers = new ArrayList<>();
        List<String> alphabets = new ArrayList<>();
        List<String> specialChars = new ArrayList<>();
        long numericSum = 0;
        List<Character> allAlphaChars = new ArrayList<>();

        for (String token : data) {
            if (isNumeric(token)) {
                long value = Long.parseLong(token);
                numericSum += value;
                if (value % 2 == 0) {
                    evenNumbers.add(token);
                } else {
                    oddNumbers.add(token);
                }
            } else if (isAlphabetic(token)) {
                alphabets.add(token.toUpperCase());
                for (char ch : token.toCharArray()) {
                    if (Character.isLetter(ch)) {
                        allAlphaChars.add(ch);
                    }
                }
            } else {
                specialChars.add(token);
            }
        }

        return BfhlResponse.builder()
                .isSuccess(true)
                .userId(userId)
                .email(email)
                .rollNumber(rollNumber)
                .oddNumbers(oddNumbers)
                .evenNumbers(evenNumbers)
                .alphabets(alphabets)
                .specialCharacters(specialChars)
                .sum(String.valueOf(numericSum))
                .concatString(buildConcatString(allAlphaChars))
                .build();
    }

    public boolean isNumeric(String token) {
        if (token == null || token.isEmpty()) return false;
        int start = (token.charAt(0) == '-') ? 1 : 0;
        if (start == token.length()) return false;
        for (int i = start; i < token.length(); i++) {
            if (!Character.isDigit(token.charAt(i))) return false;
        }
        return true;
    }

    public boolean isAlphabetic(String token) {
        if (token == null || token.isEmpty()) return false;
        for (char ch : token.toCharArray()) {
            if (!Character.isLetter(ch)) return false;
        }
        return true;
    }

    public String buildConcatString(List<Character> allAlphaChars) {
        List<Character> reversed = new ArrayList<>(allAlphaChars);
        Collections.reverse(reversed);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < reversed.size(); i++) {
            char ch = reversed.get(i);
            sb.append(i % 2 == 0 ? Character.toUpperCase(ch) : Character.toLowerCase(ch));
        }
        return sb.toString();
    }
}
