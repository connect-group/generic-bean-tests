package com.connect_group.test.genericbean.testbeans;

import java.io.Serializable;
import java.util.List;

public class HasNonSerializableNestedListInBean implements Serializable {

    private List<List<SimpleBean>> nonSerializableNestedList;
}
