/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

import com.longlinkislong.gloop.UnsafeTools.Pointer;
import java.util.Arrays;
import org.junit.Test;

/**
 *
 * @author zmichaels
 */
public class UnsafeToolsTest {
    @Test
    public void testAllocObjs() throws InstantiationException {
        final UnsafeTools unsafe = UnsafeTools.getInstance();    
        MyStruct[] ref = new MyStruct[4];        
        
        ref[0] = unsafe.malloc(MyStruct.class).instance;
        ref[0].x = 12345f;
        ref[0].y = 54321f;
        
        Pointer<MyStruct[]> ptr = unsafe.moveOffHeap(ref);
        
        ptr.instance[0].x = 98765f;
        ptr.instance[0].y = 56789f;
        
        System.out.println(ref[0]);
        ptr.free();
        
    }
    @Test
    public void testAllocFloatArray() {
        final UnsafeTools unsafe = UnsafeTools.getInstance();
        final Pointer<float[]> array = unsafe.fAlloc(4);
        
        array.instance[0] = 1f;
        array.instance[1] = 2f;
        array.instance[2] = 3f;
        array.instance[3] = 4f;
        
        System.out.println(Arrays.toString(array.instance));
        array.free();
    }
    
    @Test
    public void testAllocObj() throws InstantiationException {
        final UnsafeTools unsafe = UnsafeTools.getInstance();
        final Pointer<MyStruct> struct = unsafe.malloc(MyStruct.class);
        
        struct.instance.x = 123456f;
        struct.instance.y = 654321f;
        
        System.out.println(struct.instance);
    }
    
    class MyStruct {
        float x;
        float y;
        
        @Override
        public String toString() {
            return String.format("<%f, %f>", x, y);
        }
    }
    
}
