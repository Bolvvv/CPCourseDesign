package cpcourse.back.Service.ServiceImpl;

import cpcourse.back.Service.LexicalAnalysis;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LexicalAnalysisImpl implements LexicalAnalysis {
    //代码指针，指向某一位置的char
    private int pointer = 0;
    //关键词
    private String[] keyString = {"else", "if", "int", "return", "void", "while"};
    private int[] keySpeciesCode = {0, 1, 2, 3, 4, 5};//对应的种别码
    private Set<String> key = new HashSet<>(Arrays.asList(keyString));
    //关键符号
    private String[] symbolString = {"+", "-", "*", "/", "<", "<=", ">", ">=", "==", "!=", "=", ";", ",", "(", ")", "{", "}", "[", "]", "/*", "*/"};
    private int[] symbolSpeciesCode = {6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};//对应的种别码
    //标识符种别码:27，数字种别码：28
    private Set<String> symbol = new HashSet<>(Arrays.asList(symbolString));
    @Override
    public List<String> lexicalAnalysis(String s) {
        //存储符号串
        List<String> symbolList = new ArrayList<>();
        //转换为char数组
        char[] chars = s.toCharArray();
        for(;pointer < chars.length;){
            int m = modifySingleWord(chars[pointer]);
            switch (m){
                //识别为字母
                case 1:{
                    String temp = judgeLetter(chars, chars[pointer]);
                    if(isKey(temp)){
                        for(int i = 0; i < keyString.length; i++){
                            if(temp.equals(keyString[i])){
                                symbolList.add(String.valueOf(i));
                                symbolList.add(temp);
                                break;
                            }
                        }
                    }
                    else {
                        symbolList.add(String.valueOf(27));
                        symbolList.add(temp);
                    }
                    break;
                }
                //识别为数字
                case 2:{
                    String temp = judgeDigit(chars, chars[pointer]);
                    symbolList.add(String.valueOf(28));
                    symbolList.add(temp);
                    break;
                }
                //识别为关键符号
                case 3:{
                    String temp = judgeSymbol(chars, chars[pointer]);
                    for(int i = 0; i < symbolString.length; i++){
                        if(temp.equals(symbolString[i])){
                            symbolList.add(String.valueOf(i+6));
                            symbolList.add(temp);
                            break;
                        }
                    }
                    break;
                }
                case 4:{
                    pointer++;
                    break;
                }
                //识别到非法字符
                case 5:{
                    throw new Error("检测到非法字符"+chars[pointer]);
                }
            }
        }
        //解析完成，指针归零
        pointer = 0;
        return symbolList;
    }
    //标识符连续判定
    private String judgeSymbol(char[] chars, char c){
        StringBuilder builder = new StringBuilder();
        builder.append(c);
        int temPointer = pointer+1;
        if(temPointer >= chars.length) {
            pointer = temPointer;
            return builder.toString();
        }
        if(c == '<' || c == '>' || c == '=' || c == '!'){
            if(chars[temPointer] == '='){
                builder.append(chars[temPointer]);
                temPointer++;
                pointer = temPointer;
                return builder.toString();
            }
            else {
                pointer = temPointer;
                return builder.toString();
            }
        }
        else if(c == '/'){
            if(chars[temPointer] == '*'){
                builder.append(chars[temPointer]);
                temPointer++;
                pointer = temPointer;
                return builder.toString();
            }
            else {
                pointer = temPointer;
                return builder.toString();
            }
        }
        else if(c == '*'){
            if(chars[temPointer] == '/'){
                builder.append(chars[temPointer]);
                temPointer++;
                pointer = temPointer;
                return builder.toString();
            }
            else {
                pointer = temPointer;
                return builder.toString();
            }
        }
        else {
            pointer = temPointer;
            return builder.toString();
        }
    }
    //数字连续判定
    private String judgeDigit(char[] chars, char c){
        StringBuilder builder = new StringBuilder();
        builder.append(c);
        int temPointer = pointer+1;
        if(temPointer >= chars.length) {
            pointer = temPointer;
            return builder.toString();
        }
        boolean flag = true;
        while (flag){
            if(temPointer >= chars.length) {
                pointer = temPointer;
                return builder.toString();
            }
            if(isDigit(chars[temPointer])){
                builder.append(chars[temPointer]);
                temPointer++;
            }
            else flag = false;
        }
        pointer = temPointer;
        return builder.toString();
    }
    //字符串连续判定
    private String judgeLetter(char [] chars, char c){
        StringBuilder builder = new StringBuilder();
        builder.append(c);
        int temPointer = pointer+1;
        //防止越界
        if(temPointer >= chars.length) {
            pointer = temPointer;
            return builder.toString();
        }
        boolean flag = true;
        while (flag){
            //防止越界
            if(temPointer >= chars.length) {
                pointer = temPointer;
                return builder.toString();
            }
            if(isLetter(chars[temPointer])){
                builder.append(chars[temPointer]);
                if(isKey(builder.toString())){
                    temPointer++;
                    pointer = temPointer;
                    return builder.toString();
                }
                temPointer++;
            }
            else {
                flag = false;
            }
        }
        pointer = temPointer;
        return builder.toString();
    }
    //判定一个字符类型
    private int modifySingleWord(char c){
        if(isLetter(c)) return 1;//字母返回1
        else if(isDigit(c)) return 2;//数字返回2
        else if(symbol.contains(String.valueOf(c))) return 3;//关键符号返回3
        else if(isBlank(c)) return 4;//为空白返回4
        else return 5;//其他返回5，表示出错
    }
    private boolean isKey(String s){
        return key.contains(s);
    }
    private boolean isSymbol(String s){
        return symbol.contains(s);
    }
    private boolean isLetter(char c){
        return ((c<='z')&&(c>='a'))||((c<='Z')&&(c>='A'));
    }
    private boolean isDigit(char c){
        return (c>='0'&&c<='9');
    }
    private boolean isBlank(char c){
        return (c == 32 || c == 12 || c == 10);
    }
    private String cleanTheCode(String s){
        String dest = "";
        if (s!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(s);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
