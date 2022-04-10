package com.intepreter.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.intepreter.core.TokenType.*;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    private int start= 0; //points to the first char in current lexeme
    private int current = 0; //points to current character
    private int line = 1; //current line

    Scanner(String source){
        this.source = source;
    }

    public List<Token> scanTokens(){
        while(!isAtEnd()){
            start = current;
            scanToken();
        }

        //Add END OF FILE (EOF) token
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch (c){
            case '(':
                addToken(LEFT_PAREN);
                break;
            case ')':
                addToken(RIGHT_PAREN);
                break;
            case '{':
                addToken(LEFT_BRACE);
                break;
            case '}':
                addToken(RIGHT_BRACE);
                break;
            case ',':
                addToken(COMMA);
                break;
            case '.':
                addToken(DOT);
                break;
            case '-':
                addToken(MINUS);
                break;
            case '+':
                addToken(PLUS);
                break;
            case ';':
                addToken(SEMICOLON);
                break;
            case '*':
                addToken(STAR);
                break;
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;
            case '/':
                if(match('/')){
                    //The rest of the line is a comment
                    while(peek() != '\n' && !isAtEnd())
                        advance();
                }
                else
                    addToken(SLASH);
                break;
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                break;
            default:
                Lox.error(line, "Unexpected character");
                break;
        }
    }

    private boolean match(char expected) {
        if(isAtEnd()) return false;
        if(source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private char peek(){
        if(isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private boolean isAtEnd(){
        return current >= source.length();
    }

    private char advance(){
        return source.charAt(current++);
    }

    private void addToken(TokenType type){
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add((new Token(type, text, literal, line)));
    }
}