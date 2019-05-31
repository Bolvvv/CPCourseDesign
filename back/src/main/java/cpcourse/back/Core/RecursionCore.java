package cpcourse.back.Core;

//语法分析递归下降
public class RecursionCore {
    private int[] tokens;
    private int pointer;
    private boolean flag;//语法判断是否正确
    public RecursionCore(int[] tokens){
        this.tokens = tokens;
        this.pointer = -1;
        this.flag = true;
    }
    private void nextPointerAndCheck() throws Exception{
        pointer++;
        if(pointer == tokens.length) throw new Exception("程序执行到尽头，发生错误");
    }
    public boolean program1()throws Exception{
        System.out.println("执行1");
        //program → declaration-list
        return declarationList2();
    }
    private boolean declarationList2()throws Exception{
        //declaration-list → declaration-list declaration|declaration
        //存在左递归,用EBNF转换得:declaration-list → {declaration}
        System.out.println("执行2");
        while (true){
            if(!declaration3()) return false;
            if(pointer == tokens.length-1) return true;
        }
    }
    private boolean declaration3()throws Exception{
        //declaration → var-declaration|fun-declaration
        System.out.println("执行3");
        int p = pointer;//缓存pointer
        if(varDeclaration4()) return true;
        else {
            pointer = p;//恢复指针
            return funDeclaration6();
        }
    }
    private boolean varDeclaration4()throws Exception{
        System.out.println("执行4");
        //var-declaration → type-specifier ID;|type-specifier ID[NUM];
        int p = pointer;
        if(typeSpecifier5()){
            nextPointerAndCheck();
            if(tokens[pointer] == 27){
                nextPointerAndCheck();
                if(tokens[pointer] == 17 || tokens[pointer] == 23){
                    if(tokens[pointer] == 17) return true;
                    else {
                        nextPointerAndCheck();
                        if(tokens[pointer] == 28){
                            nextPointerAndCheck();
                            if(tokens[pointer] == 24) {
                                nextPointerAndCheck();
                                if(tokens[pointer] == 17) return true;
                                else {
                                    pointer = p;
                                    return false;
                                }
                            }
                            else {
                                pointer = p;
                                return false;
                            }
                        }
                        else {
                            pointer = p;
                            return false;
                        }
                    }
                }
                else {
                    pointer = p;
                    return false;
                }
            }
            else {
                pointer = p;
                return false;
            }
        }
        else {
            pointer = p;
            return false;
        }
    }
    private boolean typeSpecifier5()throws Exception{
        //type-specifier → int|void
        System.out.println("执行5");
        int p = pointer;
        nextPointerAndCheck();
        if(tokens[pointer] == 2||tokens[pointer] == 4) return true;
        else {
            pointer = p;
            return false;
        }
    }
    private boolean funDeclaration6()throws Exception{
        //fun-declaration → type-specifier ID(params)|compound-stmt
        System.out.println("执行6");
        int p = pointer;
        if(typeSpecifier5()){
            nextPointerAndCheck();
            if(tokens[pointer] == 27){
                nextPointerAndCheck();
                if(tokens[pointer] == 19){
                    if(params7()){
                        nextPointerAndCheck();
                        if(tokens[pointer] == 20) return true;
                        else {
                            pointer = p;
                            return false;
                        }
                    }
                    else {
                        pointer = p;
                        return false;
                    }
                }
                else {
                    pointer = p;
                    return false;
                }
            }
            else {
                pointer = p;
                return false;
            }
        }
        else {
            pointer = p;
            return compoundStmt10();
        }
    }
    private boolean params7()throws Exception{
        //params → param-list|void
        System.out.println("执行7");
        int p = pointer;
        if(paramList8())return true;
        else {
            pointer = p;
            nextPointerAndCheck();
            if(tokens[pointer] == 4) return true;
            else{
                pointer = p;
                return false;
            }
        }
    }
    private boolean paramList8()throws Exception{
        //param-list → param-list,param|param
        //存在左递归,用EBNF转换得:param-list → param{,param}
        //易知此文法的结束条件为下一个token为')'
        System.out.println("执行8");
        int p = pointer;
        while (true){
            if(param9()){
                nextPointerAndCheck();
                if(tokens[pointer] != 18) {
                    pointer--;//此处因为向前看了一位因此退回一位
                    return true;
                }
            }
            else {
                pointer = p;
                return false;
            }
        }
    }
    private boolean param9()throws Exception{
        //param → type-specifier ID|type-specifier ID[]
        System.out.println("执行9");
        int p = pointer;
        if(typeSpecifier5()){
            nextPointerAndCheck();
            if(tokens[pointer] == 27){
                nextPointerAndCheck();
                if(tokens[pointer] != 23) {
                    pointer -= 1;//当监测到为其他字符时，回退一格
                    return true;
                }
                else{
                    nextPointerAndCheck();
                    if(tokens[pointer] == 24) return true;
                    else {
                        pointer = p;
                        return false;
                    }
                }
            }
            else {
                pointer = p;
                return false;
            }
        }
        else {
            pointer = p;
            return false;
        }
    }
    private boolean compoundStmt10()throws Exception{
        //compound-stmt → {local-declarations statement-list}
        System.out.println("执行10");
        int p = pointer;
        nextPointerAndCheck();
        if(tokens[pointer] == 21){
            if(localDeclarations11()){
                if(statementList12()){
                    nextPointerAndCheck();
                    if(tokens[pointer] == 22) return true;
                    else {
                        pointer = p;
                        return false;
                    }
                }
                else {
                    pointer = p;
                    return false;
                }
            }
            else {
                pointer = p;
                return false;
            }
        }
        else {
            p = pointer;
            return false;
        }
    }
    private boolean localDeclarations11()throws Exception{
        //local-declarations → local-declarations var-declaration|empty
        //存在左递归
        System.out.println("执行11");
        int p1 = pointer;
        while (true){
            if(!varDeclaration4()){
                int p2 = pointer;//根据第10条可得当为empty时即转制下一条进行验证，并且恢复pointer
                if(statementList12()){
                    pointer = p2;
                    return true;
                }
                else {
                    pointer = p1;
                    return false;
                }
            }
        }
    }
    private boolean statementList12()throws Exception{
        //statement-list → statement-list statement|empty
        //存在左递归
        System.out.println("执行12");
        int p = pointer;
        while (true){
            if(!statement13()){
                nextPointerAndCheck();//根据第10条可得当为empty时下一个符号为}，对应22
                if(tokens[pointer] == 22){
                    pointer--;
                    return true;
                }
                else {
                    pointer = p;
                    return false;
                }
            }
        }
    }
    private boolean statement13()throws Exception{
        //statement → expression-stmt|compound-stmt|selection-stmt|iteration-stmt|return-stmt
        System.out.println("执行13");
        int p = pointer;
        if(expressionStmt14()) return true;
        else {
            pointer = p;
            if(compoundStmt10()) return true;
            else {
                pointer = p;
                if(selectionStmt15()) return true;
                else {
                    pointer = p;
                    if(iterationStmt16()) return true;
                    else {
                        pointer = p;
                        return returnStmt17();
                    }
                }
            }
        }
    }
    private boolean expressionStmt14()throws Exception{
        //expression-stmt → expression;|;
        System.out.println("执行14");
        int p = pointer;
        if(expresion18()){
            nextPointerAndCheck();
            if(tokens[pointer] == 17) return true;
            else {
                pointer = p;
                return false;
            }
        }
        else {
            pointer = p;
            nextPointerAndCheck();
            if(tokens[pointer] == 17) return true;
            else {
                pointer = p;
                return false;
            }
        }
    }
    private boolean selectionStmt15()throws Exception{
        //selection-stmt → if(expression) statement | if(expression) statement else statement
        System.out.println("执行15");
        int p = pointer;
        nextPointerAndCheck();
        if(tokens[pointer] == 1){
            nextPointerAndCheck();
            if(tokens[pointer] == 19){
                if(expresion18()){
                    nextPointerAndCheck();
                    if(tokens[pointer] == 20){
                        if(statement13()){
                            nextPointerAndCheck();
                            if(tokens[pointer] != 0){
                                pointer--;//恢复
                                return true;
                            }
                            else {
                                if(statement13()) return true;
                                else {
                                    pointer = p;
                                    return false;
                                }
                            }
                        }
                        else {
                            pointer = p;
                            return false;
                        }
                    }
                    else {
                        pointer = p;
                        return false;
                    }
                }
                else {
                    pointer = p;
                    return false;
                }
            }
            else {
                pointer = p;
                return false;
            }
        }
        else {
            pointer = p;
            return false;
        }
    }
    private boolean iterationStmt16()throws Exception{
        //iteration-stmt → while(expression) statement
        System.out.println("执行16");
        int p = pointer;
        nextPointerAndCheck();
        if(tokens[pointer] == 5){
            nextPointerAndCheck();
            if(tokens[pointer] == 19){
                if(expresion18()){
                    nextPointerAndCheck();
                    if(tokens[pointer] == 20){
                        if(statement13()) return true;
                        else {
                            pointer = p;
                            return false;
                        }
                    }
                    else {
                        pointer = p;
                        return false;
                    }
                }
                else {
                    pointer = p;
                    return false;
                }
            }
            else {
                pointer = p;
                return false;
            }
        }
        else {
            pointer = p;
            return false;
        }
    }
    private boolean returnStmt17()throws Exception{
        //return-stmt → return;|return expression;
        System.out.println("执行17");
        int p = pointer;
        nextPointerAndCheck();
        if(tokens[pointer] == 3){
            nextPointerAndCheck();
            if(tokens[pointer] == 17)return true;
            else {
                pointer--;//恢复
                if(expresion18()){
                    nextPointerAndCheck();
                    if(tokens[pointer] == 17) return true;
                    else {
                        pointer = p;
                        return false;
                    }
                }
                else {
                    pointer = p;
                    return false;
                }
            }
        }
        else {
            pointer = p;
            return false;
        }
    }
    private boolean expresion18()throws Exception{
        //expression → var=expression|simple-expression
        //!!!注意simple-expression里单独存在一个var → ID|ID[expression],会造成var=expression判定出错
        //式子x = input(),这里x和input都是ID.考虑到一旦使用了var=expression之后，下一个expression必定不会是var=expression,因此首先判断simple-expression
        //再检查后续是否存在"="，若存在则证明应该使用var=expression文法

        //!!!经过实际检测，该文法应直接简化为expression -> simple-expression = simple-expression
        System.out.println("执行18");
        int p = pointer;
        if(simpleExpression20()){
            nextPointerAndCheck();
            if(tokens[pointer] == 16){
                if(simpleExpression20()) return true;
                else {
                    pointer = p;
                    return false;
                }
            }
            else {
                pointer--;
                return true;
            }
        }
        else {
            pointer = p;
            return false;
        }
    }
    private boolean var19()throws Exception{
        //var → ID|ID[expression]
        System.out.println("执行19");
        int p = pointer;
        nextPointerAndCheck();
        if(tokens[pointer] == 27){
            nextPointerAndCheck();
            if(tokens[pointer] != 23) {
                pointer--;//恢复
                return true;
            }
            else {
                if(expresion18()){
                    nextPointerAndCheck();
                    if(tokens[pointer] == 24) return true;
                    else {
                        pointer = p;
                        return false;
                    }
                }
                else {
                    pointer = p;
                    return false;
                }
            }
        }
        else {
            pointer = p;
            return false;
        }
    }
    private boolean simpleExpression20() throws Exception{
        //simple-expression → additive-expression relop additive-expression|additive-expression
        System.out.println("执行20");
        int p1 = pointer;
        if(additiveExpression22()){
            int p2 = pointer;
            if(!relop21()){
                pointer = p2;//恢复
                return true;
            }
            else {
                if(additiveExpression22()) return true;
                else {
                    pointer = p1;
                    return false;
                }
            }
        }
        else {
            pointer = p1;
            return false;
        }
    }
    private boolean relop21() throws Exception{
        //relop → <=|<|>|>=|==|!=
        System.out.println("执行21");
        int p = pointer;
        nextPointerAndCheck();
        if(tokens[pointer] <= 15 && tokens[pointer] >= 10) return true;
        else {
            pointer = p;
            return false;
        }
    }
    private boolean additiveExpression22() throws Exception{
        //additive-expression → additive-expression addop term|term
        System.out.println("执行22");
        int p1 = pointer;
        while (true){
            if(term24()){
                int p2 = pointer;
                if(!addop23()){
                  pointer = p2;
                  return true;
                }
            }
            else {
                pointer = p1;
                return false;
            }
        }
    }
    private boolean addop23() throws Exception{
        //addop → +|-
        System.out.println("执行23");
        int p = pointer;
        nextPointerAndCheck();
        if(tokens[pointer] == 6 || tokens[pointer] == 7) return true;
        else {
            pointer = p;
            return false;
        }
    }
    private boolean term24() throws Exception{
        //term → term mulop factor|factor
        System.out.println("执行24");
        int p1 = pointer;
        while (true){
            if(factor26()){
                int p2 = pointer;
                if(!mulop25()){
                    pointer = p2;
                    return true;
                }
            }
            else {
                pointer = p1;
                return false;
            }
        }
    }
    private boolean mulop25() throws Exception{
        //mulop → *|/
        System.out.println("执行25");
        int p = pointer;
        nextPointerAndCheck();
        if(tokens[pointer] == 8 || tokens[pointer] == 9) return true;
        else {
            pointer = p;
            return false;
        }
    }
    private boolean factor26() throws Exception{
        //factor → (expression)|var|call|NUM
        //!!!注意，此处，var和call存在左部相同"ID",因此要区别对待,因此此函数不同于之前的代码，需要考虑上下文关系
        //call → ID(args)
        //var → ID|ID[expression]
        System.out.println("执行26");
        int p = pointer;
        nextPointerAndCheck();
        if(tokens[pointer] == 28) return true;
        else {
            if(tokens[pointer] == 19){
                if(expresion18()){
                    nextPointerAndCheck();
                    if(tokens[pointer] == 20) return true;
                    else {
                        pointer = p;
                        return false;
                    }
                }
                else {
                    pointer = p;
                    return false;
                }
            }
            else {
                if(tokens[pointer] == 27){
                    nextPointerAndCheck();
                    if(tokens[pointer] == 19){
                        if(args28()){
                            nextPointerAndCheck();
                            if(tokens[pointer] == 20) return true;
                            else {
                                pointer = p;
                                return false;
                            }
                        }
                        else {
                            pointer = p;
                            return false;
                        }
                    }
                    else if(tokens[pointer] == 23){
                        if(expresion18()){
                            nextPointerAndCheck();
                            if(tokens[pointer] == 24) return true;
                            else {
                                pointer = p;
                                return false;
                            }
                        }
                        else {
                            pointer = p;
                            return false;
                        }
                    }
                    else {
                        pointer--;//恢复
                        return true;
                    }
                }
                else {
                    pointer = p;
                    return false;
                }
            }
        }
    }
    private boolean call27() throws Exception{
        //call → ID(args)
        System.out.println("执行27");
        int p = pointer;
        nextPointerAndCheck();
        if(tokens[pointer] == 27){
            nextPointerAndCheck();
            if(tokens[pointer] == 19){
                if(args28()){
                    nextPointerAndCheck();
                    if(tokens[pointer] == 20) return true;
                    else {
                        pointer = p;
                        return false;
                    }
                }
                else {
                    pointer = p;
                    return false;
                }
            }
            else {
                pointer = p;
                return false;
            }
        }
        else {
            pointer = p;
            return false;
        }
    }
    private boolean args28() throws Exception{
        //args → arg-list|empty
        System.out.println("执行28");
        int p = pointer;
        if(argList29()) return true;
        else {
            pointer = p;
            nextPointerAndCheck();
            if(tokens[pointer] == 20) {
                pointer--;//恢复
                return true;
            }
            else {
                pointer--;
                return false;
            }
        }
    }
    private boolean argList29() throws Exception{
        //arg-list → arg-list, expression|expression
        System.out.println("执行29");
        int p1 = pointer;
        while (true){
            if(expresion18()){
                int p2 = pointer;
                nextPointerAndCheck();
                if(tokens[pointer] != 18){
                    pointer = p2;
                    return true;
                }
            }
            else {
                this.pointer = p1;
                return false;
            }
        }

    }
}
