/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.qc.bdeb.baldr.utils;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author maxim
 */
public class ArrayUtilTest {

    @Test
    public void testToIntArray() {
        int[] arr = {1, 1, 2, 3, 5, 8, 13, 21};
        ArrayList<Integer> list = new ArrayList<>();

        for (int elm : arr) {
            list.add(elm);
        }

        assertArrayEquals(arr, ArrayUtil.toIntArray(list));
    }

    @Test
    public void testPrependString() {
        String[] complete = {"abc", "def", "ghi", "klm"};
        String[] incomplete = {"def", "ghi", "klm"};

        assertArrayEquals(complete, ArrayUtil.prependString(incomplete, "abc"));
    }

    @Test
    public void testCopyRangeInto() {
        Integer[] arr = {1, 1, 2, 3, 5, 8, 13, 21};
        Integer[] to = new Integer[4];

        ArrayUtil.copyRangeInto(to, 0, arr, 2, 4);

        assertArrayEquals(to, new Integer[]{2, 3, 5, 8});
    }

    @Test
    public void testTrimStringArray() {
        String[] toBeTrimmed = {"   abc ", "", "def\n", "", ""};
        String[] expected = {"abc", "def"};

        assertArrayEquals(expected, ArrayUtil.trimStringsInArray(toBeTrimmed));
    }
}
