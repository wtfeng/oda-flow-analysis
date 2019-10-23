package com.fwd;

import java.util.List;

public class Intent {
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Intent> getNextIntents() {
        return nextIntents;
    }

    public void setNextIntents(List<Intent> nextIntents) {
        this.nextIntents = nextIntents;
    }

    List<Intent> nextIntents;

}
