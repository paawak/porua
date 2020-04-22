/*
 * MomentsCalculator.java
 *
 * Created on Sep 22, 2012
 *
 * Copyright (c) 2002 - 2012 : Swayam Inc.
 *
 * P R O P R I E T A R Y & C O N F I D E N T I A L
 *
 * The copyright of this document is vested in Swayam Inc. without
 * whose prior written permission its contents must not be published,
 * adapted or reproduced in any form or disclosed or
 * issued to any third party.
 */
package com.swayam.ocr.core.moments;

import com.swayam.ocr.core.util.BinaryImage;

/**
 * 
 * @author paawak
 * 
 */
public class HuMomentsCalculator {

	private final BinaryImage image;

	public HuMomentsCalculator(BinaryImage image) {
		this.image = image;
	}

	public double getInvariantMoment(MomentInvariantOrder order) {

		switch (order) {
		case ONE:
			return getInvariantMoment_1();
		case TWO:
			return getInvariantMoment_2();
		case THREE:
			return getInvariantMoment_3();
		case FOUR:
			return getInvariantMoment_4();
		case FIVE:
			return getInvariantMoment_5();
		case SIX:
			return getInvariantMoment_6();
		case SEVEN:
			return getInvariantMoment_7();
		default:
			throw new IllegalArgumentException(order + " case not handled");
		}

	}

	private double getInvariantMoment_1() {
		double mom20 = getNormalizedCentralMoment(2, 0);
		double mom02 = getNormalizedCentralMoment(0, 2);

		// η 20 + η 02

		return mom20 + mom02;
	}

	private double getInvariantMoment_2() {
		double mom20 = getNormalizedCentralMoment(2, 0);
		double mom02 = getNormalizedCentralMoment(0, 2);
		double mom11 = getNormalizedCentralMoment(1, 1);

		// ( η 20 − η 02) 2 + 4 η11 2

		return Math.pow(mom20 - mom02, 2) + 4 * Math.pow(mom11, 2);
	}

	private double getInvariantMoment_3() {
		double mom12 = getNormalizedCentralMoment(1, 2);
		double mom21 = getNormalizedCentralMoment(2, 1);
		double mom30 = getNormalizedCentralMoment(3, 0);
		double mom03 = getNormalizedCentralMoment(0, 3);

		// ( η 30 − 3 η12 ) 2 + (3 η 21 − μ03 )2

		return Math.pow(mom30 - 3 * mom12, 2) + Math.pow(3 * mom21 - mom03, 2);
	}

	private double getInvariantMoment_4() {
		double mom12 = getNormalizedCentralMoment(1, 2);
		double mom21 = getNormalizedCentralMoment(2, 1);
		double mom30 = getNormalizedCentralMoment(3, 0);
		double mom03 = getNormalizedCentralMoment(0, 3);

		// ( η 30 + η12 ) 2 + ( η 21 + μ 03 ) 2

		return Math.pow(mom30 + mom12, 2) + Math.pow(mom21 + mom03, 2);
	}

	private double getInvariantMoment_5() {
		double mom12 = getNormalizedCentralMoment(1, 2);
		double mom21 = getNormalizedCentralMoment(2, 1);
		double mom30 = getNormalizedCentralMoment(3, 0);
		double mom03 = getNormalizedCentralMoment(0, 3);

		// ( η 30 −3 η12)( η30 +η 12)[( η30 +η 12)2 −3 ( η21 +η03) 2 ]
		// + ( 3 η 21 − η 03)( η21 +η03 )[3 ( η 30 +η 12 )2 − ( η 21 + η 03)2]

		return (mom30 - 3 * mom12) * (mom30 + mom12)
				* (Math.pow(mom30 + mom12, 2) - 3 * Math.pow(mom21 + mom03, 2))
				+ (3 * mom21 - mom03) * (mom21 + mom03)
				* (3 * Math.pow(mom30 + mom12, 2) - Math.pow(mom21 + mom03, 2));
	}

	private double getInvariantMoment_6() {
		double mom11 = getNormalizedCentralMoment(1, 1);
		double mom02 = getNormalizedCentralMoment(0, 2);
		double mom20 = getNormalizedCentralMoment(2, 0);
		double mom12 = getNormalizedCentralMoment(1, 2);
		double mom21 = getNormalizedCentralMoment(2, 1);
		double mom30 = getNormalizedCentralMoment(3, 0);
		double mom03 = getNormalizedCentralMoment(0, 3);

		// ( η20 − η 02 )[( η 30 + η 12 ) 2 - ( η 21+ η 03) 2 ]
		// + 4 η 11( η 30 + η 12 )( η 21+ η 03 )

		return (mom20 - mom02)
				* (Math.pow(mom30 + mom12, 2) - Math.pow(mom21 + mom03, 2)) + 4
				* mom11 * (mom30 + mom12) * (mom21 + mom03);
	}

	private double getInvariantMoment_7() {
		double mom12 = getNormalizedCentralMoment(1, 2);
		double mom21 = getNormalizedCentralMoment(2, 1);
		double mom30 = getNormalizedCentralMoment(3, 0);
		double mom03 = getNormalizedCentralMoment(0, 3);

		// ( 3 η21 − η03 )( η30+ η12)[( η30 + η12 )2 - 3( η 21 + η03 )2 ]
		// - ( η30 -3 η12)( η21 + η 03)[(3( η30 + η12 ) 2 - ( η21 + η03 )2 ]

		return (3 * mom21 - mom03) * (mom30 + mom12)
				* (Math.pow(mom30 + mom12, 2) - 3 * Math.pow(mom21 + mom03, 2))
				- (mom30 - 3 * mom12) * (mom21 + mom03)
				* (3 * Math.pow(mom30 + mom12, 2) - Math.pow(mom21 + mom03, 2));
	}

	private double getMoment(int p, int q) {
		return getMoment(p, q, new Point(0, 0));
	}

	private double getMoment(int p, int q, Point centroid) {

		double moment = 0;

		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				if (image.getValueAt(i, j)) {
					moment += Math.pow(i - centroid.x, p)
							* Math.pow(j - centroid.y, q);
				}
			}
		}

		return moment;

	}

	private double getCentroidMoment(int p, int q) {
		return getMoment(p, q, getCentroid());
	}

	private double getNormalizedCentralMoment(int p, int q) {
		return getCentroidMoment(p, q)
				/ Math.pow(getCentroidMoment(0, 0), (p + q + 2) / 2);
	}

	private Point getCentroid() {
		double m00 = getMoment(0, 0);
		double m01 = getMoment(0, 1);
		double m10 = getMoment(1, 0);
		return new Point(m10 / m00, m01 / m00);
	}

	private class Point {

		final double x;
		final double y;

		Point(double x, double y) {
			this.x = x;
			this.y = y;
		}

	}

	public static enum MomentInvariantOrder {
		ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN;
	}

}
