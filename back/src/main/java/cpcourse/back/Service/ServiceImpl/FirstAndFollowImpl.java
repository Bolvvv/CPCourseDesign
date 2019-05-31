package cpcourse.back.Service.ServiceImpl;

import cpcourse.back.Service.FirstAndFollow;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;

@Service
public class FirstAndFollowImpl implements FirstAndFollow {

    @Override
    public String getFirstAndFollow(String s) {
        String [] array = s.split("\\s+");//根据空白对字符串进行分割
        ArrayList<LeftPart> tempSets = dataProcessing(array);//对分割好的字符串进行归类
        ArrayList<LeftPart> firstResult = getFirst(tempSets);//求First集合
        ArrayList<LeftPart> followResult = getFollow(tempSets, firstResult);//求Follow集合
        String result;//存储结果
        result = "First集:\n";
        for(int i = 0; i < tempSets.size(); i++){
            result = result+firstResult.get(i).getKey()+":{";
            for(String t:firstResult.get(i).getValue()){
                result = result+t+",";
            }
            result = result.substring(0, result.length()-1)+"}\n";
        }
        result = result+"Follow集:\n";
        for(int i = 0; i < tempSets.size(); i++){
            result = result+followResult.get(i).getKey()+":{";
            for(String t:followResult.get(i).getValue()){
                result = result+t+",";
            }
            result = result.substring(0, result.length()-1)+"}\n";
        }
        return result;
    }
    //求数组的Follow集合
    private ArrayList<LeftPart> getFollow(ArrayList<LeftPart> tempSets, ArrayList<LeftPart> firstResult){
        //初始化tempSets中的complete状态
        for(LeftPart l : tempSets) l.setComplete(false);
        ArrayList<LeftPart> followResult = new ArrayList<>();
        //首先求起始左部的follow集，添加终结符号#
        HashSet<String> value = new HashSet<>();
        value.add("#");
        LeftPart beginPart = new LeftPart(tempSets.get(0).getKey(), value);
        followResult.add(beginPart);
        for(int i = 0; i < tempSets.size(); i++){
            recursionForFollow(tempSets.get(i).getKey(), tempSets, firstResult, followResult);
        }
        return followResult;
    }
    //求数组的First集合
    private ArrayList<LeftPart> getFirst(ArrayList<LeftPart> tempSets){
        ArrayList<LeftPart> firstResult = new ArrayList<>();
        for(int i = 0; i < tempSets.size(); i++){
            recursionForFirst(tempSets.get(i).getKey(), tempSets, firstResult);
        }
        return firstResult;
    }
    //数据处理
    private ArrayList<LeftPart> dataProcessing(String [] array){
        ArrayList<LeftPart> tempSets = new ArrayList<>();
        //根据左部进行分类，并将结果存入类中
        for(int i = 0; i < array.length-1; i = i+2){
            if(!isUpperAlphabet(array[i].charAt(0))) {
                System.out.println("出现大写字母异常"+i);
            }//不是大写字母，异常
            if(array[i+1].charAt(0) == array[i].charAt(0)) {
                System.out.println("出现左递归"+i);
            }//出现左递归，返回异常
            int judge = isKeyExit(tempSets, array[i]);
            if(judge == -1){
                HashSet<String> value = new HashSet<>();
                value.add(array[i+1]);
                LeftPart t = new LeftPart(array[i], value);
                tempSets.add(t);
            }
            else {
                HashSet<String> value = tempSets.get(judge).getValue();
                value.add(array[i+1]);
                tempSets.get(judge).setValue(value);
            }
        }
        return tempSets;
    }
    //递归求follow
    private LeftPart recursionForFollow(String leftPart, ArrayList<LeftPart> tempSets, ArrayList<LeftPart> firstResult, ArrayList<LeftPart> followResult){
        //判断当前求的左部的follow是否已经求完
        LeftPart tempLeft = findLeftPartByName(leftPart, tempSets);
        if(tempLeft.isComplete()) return findLeftPartByName(leftPart, followResult);
        //若未求完，进行下列操作
        for(int i = 0; i < tempSets.size(); i ++){
            if(leftPart.equals(tempSets.get(i).getKey())) continue;//如果和所需求的左部index相同，则跳过
            else{
                for(String s: tempSets.get(i).getValue()){
                    int temp = isCharInString(leftPart.charAt(0), s);
                    if(temp == s.length()) continue;//如果等于长度，证明在此个value中不存在左部的key
                    else {
                        int j = temp+1;
                        for(; j < s.length(); j++){
                            int judge1 = isKeyExit(followResult, leftPart);
                            //如果是大写字母，证明为非终结符
                            if(isUpperAlphabet(s.charAt(j))){
                                LeftPart sonLeft = findLeftPartByName(String.valueOf(s.charAt(j)), firstResult);
                                //首先添加大写字母的First集
                                if(judge1 == -1){
                                    HashSet<String> value = new HashSet<>();
                                    for(String s1: sonLeft.getValue()){
                                        if(!s1.equals("^")) {
                                            value.add(s1);
                                        }
                                    }
                                    LeftPart l = new LeftPart(leftPart, value);
                                    followResult.add(l);
                                }
                                else {
                                    HashSet<String> value = followResult.get(judge1).getValue();
                                    for(String s1: sonLeft.getValue()){
                                        if(!s1.equals("^")) {
                                            value.add(s1);
                                        }
                                    }
                                }
                                //判断该大写字母的First集是否包含空集
                                if(!sonLeft.hasEmpty()) break;
                            }
                            else {
                                if(judge1 == -1){
                                    HashSet<String> value = new HashSet<>();
                                    value.add(String.valueOf(s.charAt(j)));
                                    LeftPart l = new LeftPart(leftPart, value);
                                    followResult.add(l);
                                }
                                else {
                                    HashSet<String> value = followResult.get(judge1).getValue();
                                    value.add(String.valueOf(s.charAt(j)));
                                }
                                break;
                            }
                        }
                        //当j == s.length()时，证明空集已经到达最后一位或该所需求左部本来就位于最后一位，因此添加所在左部的follow集到所需求左部的follow集中
                        if(j == s.length()){
                            int judge2 = isKeyExit(followResult, leftPart);
                            LeftPart sonLeft = recursionForFollow(tempSets.get(i).getKey(), tempSets, firstResult, followResult);//这里求得的是complete状态
                            if(judge2 == -1){
                                HashSet<String> value = new HashSet<>();
                                for(String s1: sonLeft.getValue()){
                                    value.add(s1);
                                }
                                LeftPart l = new LeftPart(leftPart, value);
                                followResult.add(l);
                            }
                            else {
                                HashSet<String> value = followResult.get(judge2).getValue();
                                for(String s1: sonLeft.getValue()){
                                    value.add(s1);
                                }
                            }
                        }
                    }
                }
            }
        }
        tempLeft.setComplete(true);
        LeftPart l = findLeftPartByName(leftPart, followResult);
        l.setComplete(true);
        return l;
    }
    //递归求first
    private LeftPart recursionForFirst(String leftPart, ArrayList<LeftPart> tempSets, ArrayList<LeftPart> firstResult){
        LeftPart tempLeft = findLeftPartByName(leftPart, tempSets);
        if(tempLeft.isComplete()) return findLeftPartByName(leftPart, firstResult);
        for(String s: tempLeft.getValue()){
            for(int i = 0; i < s.length(); i++){
                int judge = isKeyExit(firstResult, leftPart);
                //如果是终结符
                if(!isUpperAlphabet(s.charAt(i))){
                    if(judge == -1){
                        HashSet<String> value = new HashSet<>();
                        value.add(String.valueOf(s.charAt(i)));
                        LeftPart l = new LeftPart(leftPart, value);
                        firstResult.add(l);
                    }
                    else {
                        HashSet<String> value = firstResult.get(judge).getValue();
                        value.add(String.valueOf(s.charAt(i)));
                    }
                    break;
                }
                //如果不是终结符
                else {
                    LeftPart sonLeft = recursionForFirst(String.valueOf(s.charAt(i)), tempSets, firstResult);//这里返回来的左部都是complete状态的
                    if(judge == -1){
                        HashSet<String> value = new HashSet<>();
                        for(String temps: sonLeft.getValue()){
                            value.add(temps);
                        }
                        LeftPart l = new LeftPart(leftPart, value);
                        firstResult.add(l);
                    }
                    else {
                        LeftPart l = firstResult.get(judge);
                        HashSet<String> value = l.getValue();
                        for(String temps: sonLeft.getValue()){
                            value.add(temps);
                        }
                    }

                    if(!sonLeft.hasEmpty()) break;
                }
            }
        }
        tempLeft.setComplete(true);
        LeftPart l = findLeftPartByName(leftPart, firstResult);
        l.setComplete(true);
        return l;
    }
    //根据名称查找左部对应的类
    private LeftPart findLeftPartByName(String leftPart, ArrayList<LeftPart> tempSets){
        for(int i = 0; i < tempSets.size(); i++){
            if(tempSets.get(i).getKey().equals(leftPart)) return tempSets.get(i);
        }
        System.out.println("出现错误，无法根据key在tempSets中查询到对应的左部类");
        return null;
    }
    //char a是否在string b中，若在则返回其位置，若不在则返回数组长度
    private int isCharInString(char a, String b){
        int result = b.length();
        for(int i = 0; i < b.length(); i++){
            if(a == b.charAt(i)) {
                result = i;
                return result;
            }
        }
        return result;
    }
    //判断是否已经添加进数组，已添加进则返回数组位置，反之返回-1
    private int isKeyExit(ArrayList<LeftPart> t, String key){
        for(int i = 0; i < t.size(); i++){
            if(t.get(i).getKey().equals(key)) return i;
        }
        return -1;
    }
    //判断是否是大写字母
    private boolean isUpperAlphabet(char s){
        return (s >= 'A' && s <= 'Z');
    }
}

class LeftPart {
    private String key;
    private HashSet<String> value;
    private boolean isComplete;//判断该文法的右部是否全部已转换为终结符

    LeftPart(String key, HashSet<String> value){
        this.key = key;
        this.value = value;
        this.isComplete = false;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public HashSet<String> getValue() {
        return value;
    }

    public void setValue(HashSet<String> value) {
        this.value = value;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    //判断是否存在空集，此处空集符号为^
    public boolean hasEmpty(){
        return value.contains("^");
    }
}


