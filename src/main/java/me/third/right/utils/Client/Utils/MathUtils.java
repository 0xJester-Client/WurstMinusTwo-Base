/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package me.third.right.utils.Client.Utils;

import java.util.concurrent.ThreadLocalRandom;

public final class MathUtils {
	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
			
		}catch(NumberFormatException e) {
			return false;
		}
	}
	
	public static boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
			
		}catch(NumberFormatException e) {
			return false;
		}
	}
	
	public static int floor(float value) {
		int i = (int)value;
		return value < i ? i - 1 : i;
	}
	
	public static int floor(double value)
	{
		int i = (int)value;
		return value < i ? i - 1 : i;
	}
	
	public static int clamp(int num, int min, int max)
	{
		return num < min ? min : num > max ? max : num;
	}
	
	public static float clamp(float num, float min, float max)
	{
		return num < min ? min : num > max ? max : num;
	}
	
	public static double clamp(double num, double min, double max)
	{
		return num < min ? min : num > max ? max : num;
	}

	public static double round(double value, int places) {
		double scale = Math.pow(10, places);
		return Math.round(value * scale) / scale;
	}

	public static double newRandomNumberDouble(double min, double max){ return ThreadLocalRandom.current().nextDouble(min, max + 1); }
	public static int newRandomNumberInt(int min, int max){ return ThreadLocalRandom.current().nextInt(min, max + 1); }

	public static float toPercentage(float min, float max, float value) {
		return ((value - min) * 100) / (max - min);
	}

	public static float fromPercentage(float min, float max, float value) {
		return (((max - min) * (value)) + min);
	}
}
