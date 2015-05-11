/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.nullClass;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kristianw
 */
public class TesterClassTest {

    @Test
    public void isMeaningOfLifeCorrect() {
        assertEquals(42, TesterClass.meaningOfLife());
    }
}
