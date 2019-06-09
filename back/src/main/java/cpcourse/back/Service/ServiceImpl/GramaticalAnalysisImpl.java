package cpcourse.back.Service.ServiceImpl;

import cpcourse.back.Core.RecursionCore;
import cpcourse.back.Service.GrammaticalAnalysis;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GramaticalAnalysisImpl implements GrammaticalAnalysis {
    @Override
    public String recursion(List<String> lexicalAnalysisString) {
        //词法分析器传来的数据样式为:[1, aa, 2, aa],因此在此处对数据进行处理
        int[] tokens = new int[lexicalAnalysisString.size()/2];
        for(int i = 0, j = 0; i < lexicalAnalysisString.size(); i+=2, j++){
            tokens[j] = Integer.valueOf(lexicalAnalysisString.get(i));
        }
        RecursionCore recursionCore = new RecursionCore(tokens);
        try{
            if(recursionCore.program1()) System.out.println("语法无错误");
            else {
                System.out.println("语法错误");
                System.out.println("语法错误位置为"+recursionCore.errorPointer+"附近");

            }
        }catch (Exception e){
            System.out.println("语法错误，指针已到末尾");
        }
        return null;
    }

}
