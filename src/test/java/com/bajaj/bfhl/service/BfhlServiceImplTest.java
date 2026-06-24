package com.bajaj.bfhl.service;

import com.bajaj.bfhl.dto.BfhlRequest;
import com.bajaj.bfhl.dto.BfhlResponse;
import com.bajaj.bfhl.service.impl.BfhlServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BfhlServiceImplTest {

    private BfhlServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new BfhlServiceImpl();
        ReflectionTestUtils.setField(service, "userId",     "aniket_singla_01102005");
        ReflectionTestUtils.setField(service, "email",      "aniket2100.be23@chitkara.edu.in");
        ReflectionTestUtils.setField(service, "rollNumber", "2310992100");
    }

    @Test
    @DisplayName("Example A")
    void exampleA() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("a", "1", "334", "4", "R", "$"));
        BfhlResponse response = service.processData(request);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getOddNumbers()).containsExactly("1");
        assertThat(response.getEvenNumbers()).containsExactly("334", "4");
        assertThat(response.getAlphabets()).containsExactly("A", "R");
        assertThat(response.getSpecialCharacters()).containsExactly("$");
        assertThat(response.getSum()).isEqualTo("339");
        assertThat(response.getConcatString()).isEqualTo("Ra");
    }

    @Test
    @DisplayName("Example B")
    void exampleB() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("2", "a", "y", "4", "&", "-", "*", "5", "92", "b"));
        BfhlResponse response = service.processData(request);

        assertThat(response.getOddNumbers()).containsExactly("5");
        assertThat(response.getEvenNumbers()).containsExactly("2", "4", "92");
        assertThat(response.getAlphabets()).containsExactly("A", "Y", "B");
        assertThat(response.getSpecialCharacters()).containsExactly("&", "-", "*");
        assertThat(response.getSum()).isEqualTo("103");
        assertThat(response.getConcatString()).isEqualTo("ByA");
    }

    @Test
    @DisplayName("Example C")
    void exampleC() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("A", "ABCD", "DOE"));
        BfhlResponse response = service.processData(request);

        assertThat(response.getOddNumbers()).isEmpty();
        assertThat(response.getEvenNumbers()).isEmpty();
        assertThat(response.getAlphabets()).containsExactly("A", "ABCD", "DOE");
        assertThat(response.getSpecialCharacters()).isEmpty();
        assertThat(response.getSum()).isEqualTo("0");
        assertThat(response.getConcatString()).isEqualTo("EoDdCbAa");
    }

    @Test
    @DisplayName("Only numbers")
    void onlyNumbers() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("10", "3", "7"));
        BfhlResponse response = service.processData(request);

        assertThat(response.getEvenNumbers()).containsExactly("10");
        assertThat(response.getOddNumbers()).containsExactly("3", "7");
        assertThat(response.getAlphabets()).isEmpty();
        assertThat(response.getSpecialCharacters()).isEmpty();
        assertThat(response.getSum()).isEqualTo("20");
        assertThat(response.getConcatString()).isEmpty();
    }

    @Test
    @DisplayName("Only special characters")
    void onlySpecialCharacters() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("@", "#", "!"));
        BfhlResponse response = service.processData(request);

        assertThat(response.getEvenNumbers()).isEmpty();
        assertThat(response.getOddNumbers()).isEmpty();
        assertThat(response.getAlphabets()).isEmpty();
        assertThat(response.getSpecialCharacters()).containsExactly("@", "#", "!");
        assertThat(response.getSum()).isEqualTo("0");
        assertThat(response.getConcatString()).isEmpty();
    }

    @Test
    @DisplayName("Single alphabet token")
    void singleAlphabetToken() {
        BfhlRequest request = new BfhlRequest(List.of("z"));
        BfhlResponse response = service.processData(request);

        assertThat(response.getAlphabets()).containsExactly("Z");
        assertThat(response.getConcatString()).isEqualTo("Z");
    }

    @Test
    @DisplayName("Zero is even")
    void zeroIsEven() {
        BfhlRequest request = new BfhlRequest(List.of("0"));
        BfhlResponse response = service.processData(request);

        assertThat(response.getEvenNumbers()).containsExactly("0");
        assertThat(response.getOddNumbers()).isEmpty();
        assertThat(response.getSum()).isEqualTo("0");
    }

    @Test
    @DisplayName("isNumeric helper")
    void isNumericTest() {
        assertThat(service.isNumeric("123")).isTrue();
        assertThat(service.isNumeric("0")).isTrue();
        assertThat(service.isNumeric("-5")).isTrue();
        assertThat(service.isNumeric("abc")).isFalse();
        assertThat(service.isNumeric("1a")).isFalse();
        assertThat(service.isNumeric("")).isFalse();
        assertThat(service.isNumeric(null)).isFalse();
    }

    @Test
    @DisplayName("isAlphabetic helper")
    void isAlphabeticTest() {
        assertThat(service.isAlphabetic("abc")).isTrue();
        assertThat(service.isAlphabetic("ABC")).isTrue();
        assertThat(service.isAlphabetic("AbCd")).isTrue();
        assertThat(service.isAlphabetic("123")).isFalse();
        assertThat(service.isAlphabetic("ab1")).isFalse();
        assertThat(service.isAlphabetic("")).isFalse();
        assertThat(service.isAlphabetic(null)).isFalse();
    }

    @Test
    @DisplayName("buildConcatString helper")
    void buildConcatStringTest() {
        assertThat(service.buildConcatString(Arrays.asList('a', 'R'))).isEqualTo("Ra");
        assertThat(service.buildConcatString(Arrays.asList('a', 'y', 'b'))).isEqualTo("ByA");
        assertThat(service.buildConcatString(Arrays.asList('A','A','B','C','D','D','O','E'))).isEqualTo("EoDdCbAa");
    }

    @Test
    @DisplayName("Identity fields in response")
    void identityFieldsInResponse() {
        BfhlRequest request = new BfhlRequest(List.of("1"));
        BfhlResponse response = service.processData(request);

        assertThat(response.getUserId()).isEqualTo("aniket_singla_01102005");
        assertThat(response.getEmail()).isEqualTo("aniket2100.be23@chitkara.edu.in");
        assertThat(response.getRollNumber()).isEqualTo("2310992100");
    }
}
