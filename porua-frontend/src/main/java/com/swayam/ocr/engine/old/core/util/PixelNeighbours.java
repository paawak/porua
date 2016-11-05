/*
 * PixelNeighbour.java
 *
 * Created on Aug 31, 2011 10:22:44 PM
 *
 * Copyright (c) 2002 - 2011 : Swayam Inc.
 *
 * P R O P R I E T A R Y & C O N F I D E N T I A L
 *
 * The copyright of this document is vested in Swayam Inc. without
 * whose prior written permission its contents must not be published,
 * adapted or reproduced in any form or disclosed or
 * issued to any third party.
 */

package com.swayam.ocr.engine.old.core.util;

/**
 * <p>
 * Given the image matrix and a point (x, y), this calculates the neighbouring cells or pixels.<br>
 * The pixels are numbered as below. The <strong>grey cell</strong> in the center is the pixel (x, y).<br>
 * 
 * <table border="1" cellpadding="4">
 * <tr>
 * <td>1</td>
 * <td>2</td>
 * <td>3</td>
 * </tr>
 * <tr>
 * <td>8</td>
 * <td bgcolor="#cccccc">&nbsp;</td>
 * <td>4</td>
 * </tr>
 * <tr>
 * <td>7</td>
 * <td>6</td>
 * <td>5</td>
 * </tr>
 * </table>
 * 
 * </p>
 * 
 * Inspired from <a href="http://www.dspguide.com/ch25/4.htm">http://www.dspguide.com/ch25/4.htm</a>
 * 
 * @author paawak
 * 
 */
public class PixelNeighbours {

    public final boolean cell1;
    public final boolean cell2;
    public final boolean cell3;
    public final boolean cell4;
    public final boolean cell5;
    public final boolean cell6;
    public final boolean cell7;
    public final boolean cell8;

    private final boolean[] neighbours = new boolean[8];

    public PixelNeighbours(int x, int y, BinaryImage binaryImage) {

        cell1 = binaryImage.getValueAt(x - 1, y - 1);
        cell2 = binaryImage.getValueAt(x, y - 1);
        cell3 = binaryImage.getValueAt(x + 1, y - 1);
        cell4 = binaryImage.getValueAt(x + 1, y);
        cell5 = binaryImage.getValueAt(x + 1, y + 1);
        cell6 = binaryImage.getValueAt(x, y + 1);
        cell7 = binaryImage.getValueAt(x - 1, y + 1);
        cell8 = binaryImage.getValueAt(x - 1, y);

        neighbours[0] = cell1;
        neighbours[1] = cell2;
        neighbours[2] = cell3;
        neighbours[3] = cell4;
        neighbours[4] = cell5;
        neighbours[5] = cell6;
        neighbours[6] = cell7;
        neighbours[7] = cell8;

    }

    public boolean getCell1() {
        return cell1;
    }

    public boolean getCell2() {
        return cell2;
    }

    public boolean getCell3() {
        return cell3;
    }

    public boolean getCell4() {
        return cell4;
    }

    public boolean getCell5() {
        return cell5;
    }

    public boolean getCell6() {
        return cell6;
    }

    public boolean getCell7() {
        return cell7;
    }

    public boolean getCell8() {
        return cell8;
    }

    public boolean checkAllFalse(int... positions) {

        checkValidPositions(positions);

        boolean sum = false;

        for (int pos : positions) {
            sum |= neighbours[pos - 1];
        }

        return !sum;

    }

    public boolean checkAllTrue(int... positions) {

        checkValidPositions(positions);

        boolean prod = true;

        for (int pos : positions) {
            prod &= neighbours[pos - 1];
        }

        return prod;

    }

    private void checkValidPositions(int... positions) {

        if (positions == null || positions.length == 0) {
            throw new IllegalArgumentException("Positions cannot be blank");
        }

        for (int pos : positions) {
            if (pos < 1 || pos > 8) {
                throw new IllegalArgumentException("Invalid position " + pos
                        + ". Valid values are 1 to 8");
            }
        }

    }

}
