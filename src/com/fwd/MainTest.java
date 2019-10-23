package com.fwd;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class MainTest {
    Main SUT = new Main();

    @Test
    public void should_prase_intent_name() {

        String lines = "  StandardMenu:\n" +
                "    component: \"System.ConditionEquals\"\n" +
                "    properties:\n" +
                "      variable: hasActivePlan\n" +
                "      value: 1\n" +
                "    transitions:\n" +
                "      actions:\n" +
                "        equal: \"StandardMenuHasPlan\"\n" +
                "        notequal: \"StandardMenuNoPlan\"       ";

        List<Intent> intents = SUT.parseIntents(lines);
        assertEquals(intents.size(), 1);
        Intent intent = intents.get(0);
        assertEquals("StandardMenu", intent.getName());
    }


    @Test
    public void should_prase_next_intends_from_current() {
        String lines = "  StandardMenu:\n" +
                "    component: \"System.ConditionEquals\"\n" +
                "    properties:\n" +
                "      variable: hasActivePlan\n" +
                "      value: 1\n" +
                "    transitions:\n" +
                "      actions:\n" +
                "        equal: \"StandardMenuHasPlan\"\n" +
                "        notequal: \"StandardMenuNoPlan\"       ";

        List<Intent> intents = SUT.parseIntents(lines);

        List<Intent> nextIntents = intents.get(0).getNextIntents();
        assertEquals("StandardMenuHasPlan", nextIntents.get(0).getName());
        assertEquals("StandardMenuNoPlan", nextIntents.get(1).getName());
    }

    @Test
    public void rightTrim() {
        String line = "  hello    ";

        assertEquals("  hello",Main.rightTrim(line));
    }
}