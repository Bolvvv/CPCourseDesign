package cpcourse.back.Service.ServiceImpl;

import cpcourse.back.Core.RecursionCore;
import cpcourse.back.Core.TreeNode;
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
            return "1";
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
            TreeNode treeNode = new TreeNode("*begin");
            if(recursionCore.program1(treeNode)) {
                StringBuilder stringBuilder = new StringBuilder();
                recursionTree(treeNode, stringBuilder, 1);
                System.out.println("语法无错误");
                return stringBuilder.toString();
            }
            else {
                System.out.println("语法错误");
                System.out.println("语法错误位置为"+recursionCore.errorPointer+"附近");
                return "2"+recursionCore.errorPointer;

            }
        }catch (Exception e){
            System.out.println("语法错误");
            System.out.println("语法错误位置为"+recursionCore.errorPointer+"附近");
            return "2"+recursionCore.errorPointer;
        }
    }

    //去除注释间的内容，将原有的内容设置为-1
    private void deleteArrays(int[] source, int begin, int end){
        for(int i = 0; i < source.length; i++){
            if(i>=begin&&i<=end) {
                source[i] = -1;
            }
        }
    }

    private void recursionTree(TreeNode head, StringBuilder result, int level){
        String[] key = {"else", "if", "int", "return", "void", "while", "+", "-", "*", "/", "<", "<=", ">", ">=", "==", "!=", "=", ";", ",", "(", ")", "{", "}", "[", "]", "/*", "*/", "ID", "NUM"};
        for(int i = 0; i < level; i++){
            result.append("  ");
        }
        if(head.getName().charAt(0) == '*') result.append(head.getName());
        else {
            int locate = Integer.valueOf(head.getName());
            result.append(key[locate]);
        }
        if(head.getSonNode() != null){
            for(TreeNode t : head.getSonNode()){
                result.append("\r");
                recursionTree(t, result, level+1);
            }
        }
    }
}
