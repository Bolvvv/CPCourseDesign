package cpcourse.back.Core;

import java.util.List;

public class TreeNode {
    private String name;
    private List<TreeNode> SonNode;

    public TreeNode(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TreeNode> getSonNode() {
        return SonNode;
    }

    public void setSonNode(List<TreeNode> sonNode) {
        SonNode = sonNode;
    }
}
