package tdd_demo;


import org.junit.Assert;
import org.junit.Test;

public class PrimeUtilTest {

    @Test
    public void testGetPrimesForEmptyResult(){
        int[] expected ={};
        Assert.assertArrayEquals(expected,PrimeUtil.getPrimes(2));
        Assert.assertArrayEquals(expected,PrimeUtil.getPrimes(2));
        Assert.assertArrayEquals(expected,PrimeUtil.getPrimes(2));
    }
    @Test
    public void testGetPrimes(){
        Assert.assertArrayEquals(new int[]{2,3,5,7},PrimeUtil.getPrimes(9));
        Assert.assertArrayEquals(new int[]{2,3,5,7,11,13},PrimeUtil.getPrimes(17));


    }

}
