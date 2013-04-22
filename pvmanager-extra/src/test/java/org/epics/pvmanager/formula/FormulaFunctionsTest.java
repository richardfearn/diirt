/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.formula;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.epics.vtype.VDouble;
import org.epics.vtype.VString;
import org.epics.pvmanager.formula.LastOfChannelExpression;
import org.epics.vtype.VNumber;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import static org.hamcrest.Matchers.*;
import static org.epics.vtype.ValueFactory.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author carcassi
 */
public class FormulaFunctionsTest {

    @Test
    public void matchArgumentTypes1() {
        FormulaFunction function = mock(FormulaFunction.class);
        when(function.isVarargs()).thenReturn(false);
        when(function.getArgumentTypes()).thenReturn(Arrays.<Class<?>>asList(Number.class, String.class));
        
        assertThat(FormulaFunctions.matchArgumentTypes(Arrays.<Object>asList(3.0, "test"), function), equalTo(true));
        assertThat(FormulaFunctions.matchArgumentTypes(Arrays.<Object>asList("test", "test"), function), equalTo(false));
        assertThat(FormulaFunctions.matchArgumentTypes(Arrays.<Object>asList(1.0, ""), function), equalTo(true));
        assertThat(FormulaFunctions.matchArgumentTypes(Arrays.<Object>asList(1.0), function), equalTo(false));
        assertThat(FormulaFunctions.matchArgumentTypes(Arrays.<Object>asList(3.0, "test", 1.0), function), equalTo(false));
        assertThat(FormulaFunctions.matchArgumentTypes(Arrays.<Object>asList(3.0, "test", "test"), function), equalTo(false));
    }

    @Test
    public void matchArgumentTypes2() {
        FormulaFunction function = mock(FormulaFunction.class);
        when(function.isVarargs()).thenReturn(true);
        when(function.getArgumentTypes()).thenReturn(Arrays.<Class<?>>asList(Number.class, String.class));
        
        assertThat(FormulaFunctions.matchArgumentTypes(Arrays.<Object>asList(3.0, "test"), function), equalTo(true));
        assertThat(FormulaFunctions.matchArgumentTypes(Arrays.<Object>asList("test", "test"), function), equalTo(false));
        assertThat(FormulaFunctions.matchArgumentTypes(Arrays.<Object>asList(1.0, ""), function), equalTo(true));
        assertThat(FormulaFunctions.matchArgumentTypes(Arrays.<Object>asList(1.0), function), equalTo(true));
        assertThat(FormulaFunctions.matchArgumentTypes(Arrays.<Object>asList(3.0, "test", 1.0), function), equalTo(false));
        assertThat(FormulaFunctions.matchArgumentTypes(Arrays.<Object>asList(3.0, "test", "test"), function), equalTo(true));
    }

    @Test
    public void findFirstMatch1() {
        FormulaFunction function1 = mock(FormulaFunction.class);
        when(function1.isVarargs()).thenReturn(false);
        when(function1.getArgumentTypes()).thenReturn(Arrays.<Class<?>>asList(String.class));
        FormulaFunction function2 = mock(FormulaFunction.class);
        when(function2.isVarargs()).thenReturn(true);
        when(function2.getArgumentTypes()).thenReturn(Arrays.<Class<?>>asList(Number.class));
        List<FormulaFunction> functions = Arrays.asList(function1, function2);
        
        assertThat(FormulaFunctions.findFirstMatch(Arrays.<Object>asList(3.0, "test"), functions), nullValue());
        assertThat(FormulaFunctions.findFirstMatch(Arrays.<Object>asList(3.0), functions), sameInstance(function2));
        assertThat(FormulaFunctions.findFirstMatch(Arrays.<Object>asList(3.0, 4.0), functions), sameInstance(function2));
        assertThat(FormulaFunctions.findFirstMatch(Arrays.<Object>asList("test"), functions), sameInstance(function1));
    }
    
    @Test
    public void matchArgumentCount1() {
        FormulaFunction function = mock(FormulaFunction.class);
        when(function.isVarargs()).thenReturn(false);
        when(function.getArgumentTypes()).thenReturn(Arrays.<Class<?>>asList(Number.class, String.class));
        
        assertThat(FormulaFunctions.matchArgumentCount(2, function), equalTo(true));
        assertThat(FormulaFunctions.matchArgumentCount(3, function), equalTo(false));
        assertThat(FormulaFunctions.matchArgumentCount(1, function), equalTo(false));
    }
    
    @Test
    public void matchArgumentCount2() {
        FormulaFunction function = mock(FormulaFunction.class);
        when(function.isVarargs()).thenReturn(true);
        when(function.getArgumentTypes()).thenReturn(Arrays.<Class<?>>asList(Number.class, String.class));
        
        assertThat(FormulaFunctions.matchArgumentCount(0, function), equalTo(false));
        assertThat(FormulaFunctions.matchArgumentCount(1, function), equalTo(true));
        assertThat(FormulaFunctions.matchArgumentCount(2, function), equalTo(true));
        assertThat(FormulaFunctions.matchArgumentCount(3, function), equalTo(true));
        assertThat(FormulaFunctions.matchArgumentCount(10, function), equalTo(true));
    }
    
    @Test
    public void formatSignature1() {
        FormulaFunction function = new ArrayOfFormulaFunction();
        assertThat(FormulaFunctions.formatSignature(function), equalTo("arrayOf(VNumber... args): VNumberArray"));
    }
    
    @Test
    public void formatSignature2() {
        FormulaFunction function = new OneArgNumericFormulaFunction("log", "desc", "input") {
            @Override
            double calculate(double arg) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        assertThat(FormulaFunctions.formatSignature(function), equalTo("log(VNumber input): VNumber"));
    }
    
    @Test
    public void formatSignature3() {
        FormulaFunction function = new NumberOperatorFunctionSet().findFunctions("+").iterator().next();
        assertThat(FormulaFunctions.formatSignature(function), equalTo("(VNumber arg1 + VNumber arg2): VNumber"));
    }
    
}
