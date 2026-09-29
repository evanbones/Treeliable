package com.evandev.treeliable.api;

public interface FellData extends FellDataImmutable {
    void setTree(TreeData tree);

    void setBreakLeaves(boolean breakLeaves);
}
