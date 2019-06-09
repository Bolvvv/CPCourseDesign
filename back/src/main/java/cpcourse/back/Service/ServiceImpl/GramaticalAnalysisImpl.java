package cpcourse.back.Service.ServiceImpl;

import cpcourse.back.Core.RecursionCore;
import cpcourse.back.Service.GrammaticalAnalysis;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Stack;

@Service
public class GramaticalAnalysisImpl implements GrammaticalAnalysis {
    @Override
    public String recursion(List<String> lexicalAnalysisString) {
        //词法分析器传来的数据样式为:[1, aa, 2, aa],因此在此处对数据进行处理
        int[] tokens = new int[lexicalAnalysisString.size()/2];
        for(int i = 0, j = 0; i < lexicalAnalysisString.size(); i+=2, j++){
            tokens[j] = Integer.valueOf(lexicalAnalysisString.get(i));
        }
        //去除注释
        Stack<Integer> st = new Stack<Integer>();
        int[] cleanTokens = {};
        for(int i = 0; i < tokens.length; i++){
            if(tokens[i] == 25) st.push(i);
            else if(tokens[i] == 26){
                int begin = st.pop();
                deleteArrays(tokens, begin, i);
            }
        }
        if(!st.empty()) {
            System.out.println("注释错误");
            return null;
        }
        else {
            int count = 0;
            for(int i = 0; i < tokens.length; i++){
                if(tokens[i] == -1) count++;
            }
            cleanTokens = new int[tokens.length - count];
            int j = 0;
            for(int i = 0; i < tokens.length; i++){
                if(tokens[i] != -1){
                    cleanTokens[j] = tokens[i];
                    j++;
                }
            }
        }

        //进行语法分析
        RecursionCore recursionCore = new RecursionCore(cleanTokens);
        try{
            if(recursionCore.program1()) System.out.println("语法无错误");
            else {
                System.out.println("语法错误");
                System.out.println("语法错误位置为"+recursionCore.errorPointer+"附近");

            }
        }catch (Exception e){
            System.out.println("语法错误");
            System.out.println("语法错误位置为"+recursionCore.errorPointer+"附近");
        }
        return null;
    }

    //去除注释间的内容，将原有的内容设置为-1
    private void deleteArrays(int[] source, int begin, int end){
        for(int i = 0; i < source.length; i++){
            if(i>=begin&&i<=end) {
                source[i] = -1;
            }
        }
    }
}
