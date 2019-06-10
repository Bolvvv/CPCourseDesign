package cpcourse.back.Core;

import java.util.ArrayList;
import java.util.List;

//语法分析递归下降
public class RecursionCore {
    private int[] tokens;
    private int pointer;
    public int errorPointer;//错误位置
    public boolean errorFlag;//错误标识
    public RecursionCore(int[] tokens){
        this.tokens = tokens;
        this.pointer = -1;
        this.errorPointer = -1;
        this.errorFlag = false;
    }
    private void nextPointerAndCheck() throws Exception{
        pointer++;
        if(pointer == tokens.length) throw new Exception("程序执行到尽头，发生错误");
    }
    private void setError(int pointer){
        this.errorFlag = true;
        this.errorPointer = pointer;
    }
    private TreeNode initFatherSonNode(TreeNode fatherNode, String nodeName){
        List<TreeNode> fatherSonNode;
        if(fatherNode.getSonNode() == null){
            fatherSonNode = new ArrayList<>();
        }
        else {
            fatherSonNode = fatherNode.getSonNode();
        }
        TreeNode node = new TreeNode(nodeName);
        fatherSonNode.add(node);
        fatherNode.setSonNode(fatherSonNode);
        return node;
    }
    private void addSonTerminatorNode(TreeNode fatherNode, int number){
        String sNumber = String.valueOf(number);
        TreeNode sonNode = new TreeNode(sNumber);
        List<TreeNode> fatherSonNode;
        if(fatherNode.getSonNode() == null){
            fatherSonNode = new ArrayList<>();
        }
        else {
            fatherSonNode = fatherNode.getSonNode();
        }
        fatherSonNode.add(sonNode);
        fatherNode.setSonNode(fatherSonNode);
    }
    public boolean program1(TreeNode fatherNode)throws Exception{
        System.out.println("执行1");
        //program → declaration-list
        //完成父亲孩子节点初始化
        TreeNode node = initFatherSonNode(fatherNode, "*program");
        if(declarationList2(node)){
            return true;
        }
        else {
            node.setSonNode(null);
            return false;
        }
    }
    private boolean declarationList2(TreeNode fatherNode)throws Exception{
        //declaration-list → declaration-list declaration|declaration
        //存在左递归,用EBNF转换得:declaration-list → {declaration}
        System.out.println("执行2");
        TreeNode node = initFatherSonNode(fatherNode, "*declaration-list");
        while (true){
            if(!declaration3(node)) {
                node.setSonNode(null);
                return false;
            }
            if(pointer == tokens.length-1) return true;
        }
    }
    private boolean declaration3(TreeNode fatherNode)throws Exception{
        //declaration → var-declaration|fun-declaration
        System.out.println("执行3");
        TreeNode node = initFatherSonNode(fatherNode, "*declaration");
        int p = pointer;//缓存pointer
        if(varDeclaration4(node)) return true;
        else {
            pointer = p;//恢复指针
            node.setSonNode(null);//恢复子节点
            if(funDeclaration6(node)){
                errorFlag = false;
                return true;
            }
            else {
                node.setSonNode(null);
                return false;
            }
        }
    }
    private boolean varDeclaration4(TreeNode fatherNode)throws Exception{
        System.out.println("执行4");
        //var-declaration → type-specifier ID;|type-specifier ID[NUM];
        TreeNode node = initFatherSonNode(fatherNode, "*var-declaration");
        int p = pointer;
        if(typeSpecifier5(node)){
            nextPointerAndCheck();
            if(tokens[pointer] == 27){
                addSonTerminatorNode(node, 27);
                nextPointerAndCheck();
                if(tokens[pointer] == 17 || tokens[pointer] == 23){
                    if(tokens[pointer] == 17) {
                        addSonTerminatorNode(node, 17);
                        return true;
                    }
                    else {
                        addSonTerminatorNode(node, 23);
                        nextPointerAndCheck();
                        if(tokens[pointer] == 28){
                            addSonTerminatorNode(node, 28);
                            nextPointerAndCheck();
                            if(tokens[pointer] == 24) {
                                addSonTerminatorNode(node, 24);
                                nextPointerAndCheck();
                                if(tokens[pointer] == 17) {
                                    addSonTerminatorNode(node, 17);
                                    return true;
                                }
                                else {
                                    node.setSonNode(null);
                                    setError(pointer);
                                    pointer = p;
                                    return false;
                                }
                            }
                            else {
                                node.setSonNode(null);
                                setError(pointer);
                                pointer = p;
                                return false;
                            }
                        }
                        else {
                            node.setSonNode(null);
                            setError(pointer);
                            pointer = p;
                            return false;
                        }
                    }
                }
                else {
                    node.setSonNode(null);
                    setError(pointer);
                    pointer = p;
                    return false;
                }
            }
            else {
                node.setSonNode(null);
                setError(pointer);
                pointer = p;
                return false;
            }
        }
        else {
            node.setSonNode(null);
            pointer = p;
            return false;
        }
    }
    private boolean typeSpecifier5(TreeNode fatherNode)throws Exception{
        //type-specifier → int|void
        System.out.println("执行5");
        TreeNode node = initFatherSonNode(fatherNode, "*type-specifier");
        int p = pointer;
        nextPointerAndCheck();
        if(tokens[pointer] == 2||tokens[pointer] == 4) {
            if(tokens[pointer] == 2) addSonTerminatorNode(node, 2);
            else addSonTerminatorNode(node, 4);
            return true;
        }
        else {
            node.setSonNode(null);
            setError(pointer);
            pointer = p;
            return false;
        }
    }
    private boolean funDeclaration6(TreeNode fatherNode)throws Exception{
        //fun-declaration → type-specifier ID(params)|compound-stmt
        System.out.println("执行6");
        TreeNode node = initFatherSonNode(fatherNode, "*fun-declaration");
        int p = pointer;
        if(typeSpecifier5(node)){
            nextPointerAndCheck();
            if(tokens[pointer] == 27){
                addSonTerminatorNode(node, 27);
                nextPointerAndCheck();
                if(tokens[pointer] == 19){
                    addSonTerminatorNode(node, 19);
                    if(params7(node)){
                        nextPointerAndCheck();
                        if(tokens[pointer] == 20) {
                            addSonTerminatorNode(node, 20);
                            return true;
                        }
                        else {
                            node.setSonNode(null);
                            setError(pointer);
                            pointer = p;
                            return false;
                        }
                    }
                    else {
                        node.setSonNode(null);
                        pointer = p;
                        return false;
                    }
                }
                else {
                    node.setSonNode(null);
                    setError(pointer);
                    pointer = p;
                    return false;
                }
            }
            else {
                node.setSonNode(null);
                setError(pointer);
                pointer = p;
                return false;
            }
        }
        else {
            pointer = p;
            node.setSonNode(null);
            if(compoundStmt10(node)){
                errorFlag = false;
                return true;
            }
            else {
                node.setSonNode(null);
                return false;
            }
        }
    }
    private boolean params7(TreeNode fatherNode)throws Exception{
        //params → param-list|void
        System.out.println("执行7");
        TreeNode node = initFatherSonNode(fatherNode, "*params");
        int p = pointer;
        if(paramList8(node))return true;
        else {
            pointer = p;
            node.setSonNode(null);
            nextPointerAndCheck();
            if(tokens[pointer] == 4) {
                addSonTerminatorNode(node, 4);
                errorFlag = false;
                return true;
            }
            else{
                node.setSonNode(null);
                setError(pointer);
                pointer = p;
                return false;
            }
        }
    }
    private boolean paramList8(TreeNode fatherNode)throws Exception{
        //param-list → param-list,param|param
        //存在左递归,用EBNF转换得:param-list → param{,param}
        //易知此文法的结束条件为下一个token为')'
        System.out.println("执行8");
        TreeNode node = initFatherSonNode(fatherNode, "*params");
        int p = pointer;
        while (true){
            if(param9(node)){
                nextPointerAndCheck();
                if(tokens[pointer] != 18) {
                    pointer--;//此处因为向前看了一位因此退回一位
                    return true;
                }
                else {
                    addSonTerminatorNode(node, 18);
                }
            }
            else {
                node.setSonNode(null);
                pointer = p;
                return false;
            }
        }
    }
    private boolean param9(TreeNode fatherNode)throws Exception{
        //param → type-specifier ID|type-specifier ID[]
        System.out.println("执行9");
        TreeNode node = initFatherSonNode(fatherNode, "*params");
        int p = pointer;
        if(typeSpecifier5(node)){
            nextPointerAndCheck();
            if(tokens[pointer] == 27){
                addSonTerminatorNode(node, 27);
                nextPointerAndCheck();
                if(tokens[pointer] != 23) {
                    pointer -= 1;//当监测到为其他字符时，回退一格
                    return true;
                }
                else{
                    addSonTerminatorNode(node, 23);
                    nextPointerAndCheck();
                    if(tokens[pointer] == 24) {
                        addSonTerminatorNode(node, 24);
                        return true;
                    }
                    else {
                        node.setSonNode(null);
                        setError(pointer);
                        pointer = p;
                        return false;
                    }
                }
            }
            else {
                node.setSonNode(null);
                setError(pointer);
                pointer = p;
                return false;
            }
        }
        else {
            node.setSonNode(null);
            pointer = p;
            return false;
        }
    }
    private boolean compoundStmt10(TreeNode fatherNode)throws Exception{
        //compound-stmt → {local-declarations statement-list}
        System.out.println("执行10");
        TreeNode node = initFatherSonNode(fatherNode, "*compound-stmt");
        int p = pointer;
        nextPointerAndCheck();
        if(tokens[pointer] == 21){
            addSonTerminatorNode(node, 21);
            if(localDeclarations11(node)){
                //TODO：有点问题
                //{x[i] = input;i = i + 1;}
                //*compound-stmt
                //	{
                //		*local-declarations
                //	    *statement-list
                //      ...
                if(statementList12(node)){
                    nextPointerAndCheck();
                    if(tokens[pointer] == 22) {
                        addSonTerminatorNode(node, 22);
                        return true;
                    }
                    else {
                        node.setSonNode(null);
                        setError(pointer);
                        pointer = p;
                        return false;
                    }
                }
                else {
                    node.setSonNode(null);
                    pointer = p;
                    return false;
                }
            }
            else {
                node.setSonNode(null);
                pointer = p;
                return false;
            }
        }
        else {
            node.setSonNode(null);
            setError(pointer);
            p = pointer;
            return false;
        }
    }
    private boolean localDeclarations11(TreeNode fatherNode)throws Exception{
        //local-declarations → local-declarations var-declaration|empty
        //存在左递归
        System.out.println("执行11");
        TreeNode node = initFatherSonNode(fatherNode, "*local-declarations");
        int p1 = pointer;
        while (true){
            if(!varDeclaration4(node)){
                node.setSonNode(node.getSonNode().subList(0, node.getSonNode().size()-1));//这是去除额外的varDeclaration子节点
                int size = node.getSonNode().size();
                int p2 = pointer;//根据第10条可得当为empty时即转制下一条进行验证，并且恢复pointer
                if(statementList12(node)){
                    //TODO:可能会出异常
                    node.setSonNode(node.getSonNode().subList(0, size));
                    pointer = p2;
                    return true;
                }
                else {
                    node.setSonNode(null);
                    pointer = p1;
                    return false;
                }
            }
        }
    }
    private boolean statementList12(TreeNode fatherNode)throws Exception{
        //statement-list → statement-list statement|empty
        //存在左递归
        System.out.println("执行12");
        TreeNode node = initFatherSonNode(fatherNode, "*statement-list");
        int p = pointer;
        while (true){
            if(!statement13(node)){
                node.setSonNode(node.getSonNode().subList(0, node.getSonNode().size()-1));//这是去除额外的varDeclaration子节点
                nextPointerAndCheck();//根据第10条可得当为empty时下一个符号为}，对应22
                if(tokens[pointer] == 22){
                    pointer--;
                    return true;
                }
                else {
                    node.setSonNode(null);
                    pointer = p;
                    return false;
                }
            }
        }
    }
    private boolean statement13(TreeNode fatherNode)throws Exception{
        //statement → expression-stmt|compound-stmt|selection-stmt|iteration-stmt|return-stmt
        System.out.println("执行13");
        TreeNode node = initFatherSonNode(fatherNode, "*statement");
        int p = pointer;
        if(expressionStmt14(node)) return true;
        else {
            pointer = p;
            node.setSonNode(null);
            if(compoundStmt10(node)) {
                errorFlag = false;
                return true;
            }
            else {
                pointer = p;
                node.setSonNode(null);
                if(selectionStmt15(node)) {
                    errorFlag = false;
                    return true;
                }
                else {
                    node.setSonNode(null);
                    pointer = p;
                    if(iterationStmt16(node)) {
                        errorFlag = false;
                        return true;
                    }
                    else {
                        pointer = p;
                        node.setSonNode(null);
                        if(returnStmt17(node)){
                            errorFlag = false;
                            return true;
                        }
                        else {
                            node.setSonNode(null);
                            return false;
                        }
                    }
                }
            }
        }
    }
    private boolean expressionStmt14(TreeNode fatherNode)throws Exception{
        //expression-stmt → expression;|;
        System.out.println("执行14");
        TreeNode node = initFatherSonNode(fatherNode, "*expression-stmt");
        int p = pointer;
        if(expresion18(node)){
            nextPointerAndCheck();
            if(tokens[pointer] == 17) {
                addSonTerminatorNode(node, 17);
                return true;
            }
            else {
                node.setSonNode(null);
                setError(pointer);
                pointer = p;
                return false;
            }
        }
        else {
            pointer = p;
            node.setSonNode(null);
            nextPointerAndCheck();
            if(tokens[pointer] == 17) {
                addSonTerminatorNode(node, 17);
                errorFlag = false;
                return true;
            }
            else {
                node.setSonNode(null);
                setError(pointer);
                pointer = p;
                return false;
            }
        }
    }
    private boolean selectionStmt15(TreeNode fatherNode)throws Exception{
        //selection-stmt → if(expression) statement | if(expression) statement else statement
        System.out.println("执行15");
        TreeNode node = initFatherSonNode(fatherNode, "*selection-stmt");
        int p = pointer;
        nextPointerAndCheck();
        if(tokens[pointer] == 1){
            addSonTerminatorNode(node, 1);
            nextPointerAndCheck();
            if(tokens[pointer] == 19){
                addSonTerminatorNode(node, 19);
                if(expresion18(node)){
                    nextPointerAndCheck();
                    if(tokens[pointer] == 20){
                        addSonTerminatorNode(node, 20);
                        if(statement13(node)){
                            nextPointerAndCheck();
                            if(tokens[pointer] != 0){
                                pointer--;//恢复
                                return true;
                            }
                            else {
                                if(statement13(node)) return true;
                                else {
                                    node.setSonNode(null);
                                    pointer = p;
                                    return false;
                                }
                            }
                        }
                        else {
                            node.setSonNode(null);
                            pointer = p;
                            return false;
                        }
                    }
                    else {
                        node.setSonNode(null);
                        setError(pointer);
                        pointer = p;
                        return false;
                    }
                }
                else {
                    node.setSonNode(null);
                    pointer = p;
                    return false;
                }
            }
            else {
                node.setSonNode(null);
                setError(pointer);
                pointer = p;
                return false;
            }
        }
        else {
            node.setSonNode(null);
            setError(pointer);
            pointer = p;
            return false;
        }
    }
    private boolean iterationStmt16(TreeNode fatherNode)throws Exception{
        //iteration-stmt → while(expression) statement
        System.out.println("执行16");
        TreeNode node = initFatherSonNode(fatherNode, "*iteration-stmt");
        int p = pointer;
        nextPointerAndCheck();
        if(tokens[pointer] == 5){
            addSonTerminatorNode(node, 5);
            nextPointerAndCheck();
            if(tokens[pointer] == 19){
                addSonTerminatorNode(node, 19);
                if(expresion18(node)){
                    nextPointerAndCheck();
                    if(tokens[pointer] == 20){
                        addSonTerminatorNode(node, 20);
                        if(statement13(node)) return true;
                        else {
                            node.setSonNode(null);
                            pointer = p;
                            return false;
                        }
                    }
                    else {
                        node.setSonNode(null);
                        setError(pointer);
                        pointer = p;
                        return false;
                    }
                }
                else {
                    node.setSonNode(null);
                    pointer = p;
                    return false;
                }
            }
            else {
                node.setSonNode(null);
                setError(pointer);
                pointer = p;
                return false;
            }
        }
        else {
            node.setSonNode(null);
            setError(pointer);
            pointer = p;
            return false;
        }
    }
    private boolean returnStmt17(TreeNode fatherNode)throws Exception{
        //return-stmt → return;|return expression;
        System.out.println("执行17");
        TreeNode node = initFatherSonNode(fatherNode, "*iteration-stmt");
        int p = pointer;
        nextPointerAndCheck();
        if(tokens[pointer] == 3){
            addSonTerminatorNode(node, 3);
            nextPointerAndCheck();
            if(tokens[pointer] == 17){
                addSonTerminatorNode(node, 17);
                return true;
            }
            else {
                pointer--;//恢复
                if(expresion18(node)){
                    nextPointerAndCheck();
                    if(tokens[pointer] == 17) {
                        addSonTerminatorNode(node, 17);
                        return true;
                    }
                    else {
                        node.setSonNode(null);
                        setError(pointer);
                        pointer = p;
                        return false;
                    }
                }
                else {
                    node.setSonNode(null);
                    pointer = p;
                    return false;
                }
            }
        }
        else {
            node.setSonNode(null);
            setError(pointer);
            pointer = p;
            return false;
        }
    }
    private boolean expresion18(TreeNode fatherNode)throws Exception{
        //expression → var=expression|simple-expression
        //!!!注意simple-expression里单独存在一个var → ID|ID[expression],会造成var=expression判定出错
        //式子x = input(),这里x和input都是ID.考虑到一旦使用了var=expression之后，下一个expression必定不会是var=expression,因此首先判断simple-expression
        //再检查后续是否存在"="，若存在则证明应该使用var=expression文法

        //!!!经过实际检测，该文法应直接简化为expression -> simple-expression = simple-expression
        System.out.println("执行18");
        TreeNode node = initFatherSonNode(fatherNode, "*expression");
        int p = pointer;
        if(var19(node)){
            nextPointerAndCheck();
            if(tokens[pointer] == 16){
                addSonTerminatorNode(node, 16);
                if(expresion18(node)) return true;
                else {
                    node.setSonNode(null);
                    pointer = p;
                    return false;
                }
            }
            else {
                node.setSonNode(null);//这里需要将已经添加至node子节点的var去掉
                pointer = p;
                if(simpleExpression20(node)){
                    return true;
                }
                else {
                    node.setSonNode(null);
                    pointer = p;
                    return false;
                }

            }
        }
        else {
            pointer = p;
            if(simpleExpression20(node)) {
                errorFlag = false;
                return true;
            }
            else {
                node.setSonNode(null);
                pointer = p;
                return false;
            }
        }
//        int p = pointer;
//        if(simpleExpression20(node)){
//            nextPointerAndCheck();
//            if(tokens[pointer] == 16){
//                addSonTerminatorNode(node, 16);
//                if(simpleExpression20(node)) return true;
//                else {
//                    node.setSonNode(null);
//                    pointer = p;
//                    return false;
//                }
//            }
//            else {
//                node.setSonNode(null);
//                setError(pointer);
//                pointer--;
//                return true;
//            }
//        }
//        else {
//            node.setSonNode(null);
//            pointer = p;
//            return false;
//        }
    }
    private boolean var19(TreeNode fatherNode)throws Exception{
        //var → ID|ID[expression]
        System.out.println("执行19");
        TreeNode node = initFatherSonNode(fatherNode, "*var");
        int p = pointer;
        nextPointerAndCheck();
        if(tokens[pointer] == 27){
            addSonTerminatorNode(node, 27);
            nextPointerAndCheck();
            if(tokens[pointer] != 23) {
                pointer--;//恢复
                return true;
            }
            else {
                addSonTerminatorNode(node, 23);
                if(expresion18(node)){
                    nextPointerAndCheck();
                    if(tokens[pointer] == 24) {
                        addSonTerminatorNode(node, 24);
                        return true;
                    }
                    else {
                        node.setSonNode(null);
                        setError(pointer);
                        pointer = p;
                        return false;
                    }
                }
                else {
                    node.setSonNode(null);
                    pointer = p;
                    return false;
                }
            }
        }
        else {
            node.setSonNode(null);
            setError(pointer);
            pointer = p;
            return false;
        }
    }
    private boolean simpleExpression20(TreeNode fatherNode) throws Exception{
        //simple-expression → additive-expression relop additive-expression|additive-expression
        System.out.println("执行20");
        TreeNode node = initFatherSonNode(fatherNode, "*simple-expression");
        int p1 = pointer;
        if(additiveExpression22(node)){
            int p2 = pointer;
            if(!relop21(node)){
                //TODO:可能存在异常
                node.setSonNode(node.getSonNode().subList(0, node.getSonNode().size()-1));//这是去除额外的varDeclaration子节点
                pointer = p2;//恢复
                return true;
            }
            else {
                if(additiveExpression22(node)) {
                    errorFlag = false;
                    return true;
                }
                else {
                    node.setSonNode(null);
                    pointer = p1;
                    return false;
                }
            }
        }
        else {
            node.setSonNode(null);
            pointer = p1;
            return false;
        }
    }
    private boolean relop21(TreeNode fatherNode) throws Exception{
        //relop → <=|<|>|>=|==|!=
        System.out.println("执行21");
        TreeNode node = initFatherSonNode(fatherNode, "*relop");
        int p = pointer;
        nextPointerAndCheck();
        if(tokens[pointer] <= 15 && tokens[pointer] >= 10) {
            addSonTerminatorNode(node, tokens[pointer]);
            return true;
        }
        else {
            node.setSonNode(null);
            setError(pointer);
            pointer = p;
            return false;
        }
    }
    private boolean additiveExpression22(TreeNode fatherNode) throws Exception{
        //additive-expression → additive-expression addop term|term
        System.out.println("执行22");
        TreeNode node = initFatherSonNode(fatherNode, "*additive-expression");
        int p1 = pointer;
        while (true){
            if(term24(node)){
                int p2 = pointer;
                if(!addop23(node)){
                  //TODO:可能有异常
                  node.setSonNode(node.getSonNode().subList(0, node.getSonNode().size()-1));//这是去除额外的varDeclaration子节点
                  pointer = p2;
                  return true;
                }
            }
            else {
                node.setSonNode(null);
                pointer = p1;
                return false;
            }
        }
    }
    private boolean addop23(TreeNode fatherNode) throws Exception{
        //addop → +|-
        System.out.println("执行23");
        TreeNode node = initFatherSonNode(fatherNode, "*addop");
        int p = pointer;
        nextPointerAndCheck();
        if(tokens[pointer] == 6 || tokens[pointer] == 7) {
            addSonTerminatorNode(node, tokens[pointer]);
            return true;
        }
        else {
            node.setSonNode(null);
            setError(pointer);
            pointer = p;
            return false;
        }
    }
    private boolean term24(TreeNode fatherNode) throws Exception{
        //term → term mulop factor|factor
        System.out.println("执行24");
        TreeNode node = initFatherSonNode(fatherNode, "*term");
        int p1 = pointer;
        while (true){
            if(factor26(node)){
                int p2 = pointer;
                if(!mulop25(node)){
                    //TODO:可能有异常
                    node.setSonNode(node.getSonNode().subList(0, node.getSonNode().size()-1));//这是去除额外的varDeclaration子节点
                    pointer = p2;
                    return true;
                }
            }
            else {
                node.setSonNode(null);
                pointer = p1;
                return false;
            }
        }
    }
    private boolean mulop25(TreeNode fatherNode) throws Exception{
        //mulop → *|/
        System.out.println("执行25");
        TreeNode node = initFatherSonNode(fatherNode, "*mulop");
        int p = pointer;
        nextPointerAndCheck();
        if(tokens[pointer] == 8 || tokens[pointer] == 9) {
            addSonTerminatorNode(node, tokens[pointer]);
            return true;
        }
        else {
            node.setSonNode(null);
            setError(pointer);
            pointer = p;
            return false;
        }
    }
    private boolean factor26(TreeNode fatherNode) throws Exception{
        //factor → (expression)|var|call|NUM
        //!!!注意，此处，var和call存在左部相同"ID",因此要区别对待,因此此函数不同于之前的代码，需要考虑上下文关系
        //call → ID(args)
        //var → ID|ID[expression]
        System.out.println("执行26");
        TreeNode node = initFatherSonNode(fatherNode, "*factor");
        int p = pointer;
        nextPointerAndCheck();
        if(tokens[pointer] == 28) {
            addSonTerminatorNode(node, 28);
            return true;
        }
        else {
            node.setSonNode(null);
            if(tokens[pointer] == 19){
                addSonTerminatorNode(node, 19);
                if(expresion18(node)){
                    nextPointerAndCheck();
                    if(tokens[pointer] == 20) {
                        addSonTerminatorNode(node, 20);
                        errorFlag = false;
                        return true;
                    }
                    else {
                        node.setSonNode(null);
                        setError(pointer);
                        pointer = p;
                        return false;
                    }
                }
                else {
                    node.setSonNode(null);
                    pointer = p;
                    return false;
                }
            }
            else {
                if(tokens[pointer] == 27){
                    addSonTerminatorNode(node, 27);
                    nextPointerAndCheck();
                    if(tokens[pointer] == 19){
                        addSonTerminatorNode(node, 19);
                        if(args28(node)){
                            nextPointerAndCheck();
                            if(tokens[pointer] == 20) {
                                addSonTerminatorNode(node, 20);
                                errorFlag = false;
                                return true;
                            }
                            else {
                                node.setSonNode(null);
                                setError(pointer);
                                pointer = p;
                                return false;
                            }
                        }
                        else {
                            node.setSonNode(null);
                            pointer = p;
                            return false;
                        }
                    }
                    else if(tokens[pointer] == 23){
                        addSonTerminatorNode(node, 23);
                        if(expresion18(node)){
                            nextPointerAndCheck();
                            if(tokens[pointer] == 24) {
                                addSonTerminatorNode(node, 24);
                                errorFlag = false;
                                return true;
                            }
                            else {
                                node.setSonNode(null);
                                setError(pointer);
                                pointer = p;
                                return false;
                            }
                        }
                        else {
                            node.setSonNode(null);
                            setError(pointer);
                            pointer = p;
                            return false;
                        }
                    }
                    else {
                        errorFlag = false;
                        pointer--;//恢复
                        return true;
                    }
                }
                else {
                    node.setSonNode(null);
                    setError(pointer);
                    pointer = p;
                    return false;
                }
            }
        }
    }
//    private boolean call27() throws Exception{
//        //call → ID(args)
//        System.out.println("执行27");
//        int p = pointer;
//        nextPointerAndCheck();
//        if(tokens[pointer] == 27){
//            nextPointerAndCheck();
//            if(tokens[pointer] == 19){
//                if(args28()){
//                    nextPointerAndCheck();
//                    if(tokens[pointer] == 20) return true;
//                    else {
//                        pointer = p;
//                        return false;
//                    }
//                }
//                else {
//                    pointer = p;
//                    return false;
//                }
//            }
//            else {
//                pointer = p;
//                return false;
//            }
//        }
//        else {
//            pointer = p;
//            return false;
//        }
//    }
    private boolean args28(TreeNode fatherNode) throws Exception{
        //args → arg-list|empty
        System.out.println("执行28");
        TreeNode node = initFatherSonNode(fatherNode, "*args");
        int p = pointer;
        if(argList29(node)) return true;
        else {
            node.setSonNode(null);
            pointer = p;
            nextPointerAndCheck();
            if(tokens[pointer] == 20) {
                addSonTerminatorNode(node, 20);
                errorFlag = false;
                pointer--;//恢复
                return true;
            }
            else {
                node.setSonNode(null);
                setError(pointer);
                pointer--;
                return false;
            }
        }
    }
    private boolean argList29(TreeNode fatherNode) throws Exception{
        //arg-list → arg-list, expression|expression
        System.out.println("执行29");
        TreeNode node = initFatherSonNode(fatherNode, "*arg-list");
        int p1 = pointer;
        while (true){
            if(expresion18(node)){
                int p2 = pointer;
                nextPointerAndCheck();
                if(tokens[pointer] != 18){
                    errorFlag = false;
                    pointer = p2;
                    return true;
                }
                else {
                    addSonTerminatorNode(node, 18);
                }
            }
            else {
                node.setSonNode(null);
                this.pointer = p1;
                return false;
            }
        }

    }
}
