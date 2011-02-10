package eu.scy.client.desktop.scydesktop.art.eloicons;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
/**
 * @author lars
 */
public class Hypothese2Icon extends CustomNode {

public var windowColorScheme: WindowColorScheme;

public function createNode(): Node {

return Group {

			content: [
				Polygon {
					points: [6.91,1.58,7.07,1.57,7.24,1.55,7.40,1.54,7.57,1.53,7.74,1.53,7.90,1.53,8.07,1.53,8.24,1.53,8.41,1.54,8.57,1.55,8.74,1.57,8.91,1.59,9.07,1.61,9.25,1.64,9.41,1.66,9.58,1.70,9.74,1.73,9.91,1.77,10.07,1.82,10.23,1.86,10.40,1.91,10.55,1.96,10.71,2.02,10.87,2.08,11.03,2.14,11.18,2.20,11.33,2.27,11.47,2.34,11.62,2.42,11.77,2.50,11.91,2.58,12.05,2.66,12.14,2.73,12.22,2.79,12.31,2.86,12.39,2.94,12.47,3.02,12.54,3.10,12.61,3.19,12.68,3.28,12.75,3.38,12.80,3.48,12.87,3.58,12.92,3.69,12.97,3.79,13.03,3.90,13.07,4.02,13.12,4.13,13.17,4.25,13.21,4.36,13.29,4.60,13.36,4.83,13.42,5.08,13.49,5.31,13.54,5.54,13.59,5.77,13.61,5.88,13.64,5.99,13.64,6.02,13.65,6.06,13.65,6.08,13.65,6.11,13.65,6.16,13.64,6.21,13.63,6.25,13.61,6.28,13.59,6.32,13.57,6.35,13.51,6.42,13.49,6.46,13.45,6.49,13.42,6.54,13.39,6.59,13.36,6.64,13.33,6.71,13.31,6.76,13.29,6.82,13.28,6.89,13.28,6.96,13.28,7.03,13.29,7.09,13.30,7.15,13.32,7.18,13.33,7.21,13.36,7.26,13.38,7.30,13.41,7.35,13.43,7.39,13.48,7.47,13.53,7.54,13.57,7.60,13.61,7.66,13.70,7.76,13.78,7.87,13.83,7.92,13.88,7.98,13.93,8.04,13.99,8.10,14.05,8.18,14.08,8.22,14.11,8.26,14.18,8.34,14.24,8.42,14.31,8.51,14.36,8.60,14.39,8.64,14.41,8.69,14.43,8.74,14.44,8.78,14.45,8.82,14.45,8.86,14.45,8.90,14.44,8.95,14.43,8.98,14.41,9.01,14.39,9.03,14.37,9.06,14.35,9.09,14.32,9.11,14.26,9.15,14.20,9.18,14.14,9.21,14.07,9.24,13.99,9.26,13.92,9.28,13.85,9.31,13.77,9.33,13.71,9.36,13.65,9.40,13.59,9.44,13.56,9.46,13.54,9.49,13.52,9.52,13.50,9.54,13.47,9.59,13.46,9.63,13.45,9.67,13.45,9.71,13.45,9.76,13.46,9.80,13.48,9.84,13.50,9.88,13.52,9.92,13.54,9.97,13.60,10.05,13.66,10.14,13.72,10.23,13.73,10.25,13.74,10.28,13.75,10.30,13.76,10.32,13.76,10.35,13.75,10.37,13.75,10.39,13.74,10.41,13.72,10.43,13.71,10.45,13.68,10.49,13.64,10.53,13.60,10.56,13.55,10.59,13.50,10.62,13.45,10.64,13.40,10.66,13.35,10.68,13.31,10.70,13.28,10.71,13.26,10.72,13.25,10.73,13.32,10.74,13.40,10.76,13.47,10.79,13.53,10.81,13.56,10.82,13.59,10.83,13.61,10.85,13.63,10.87,13.65,10.90,13.66,10.92,13.66,10.95,13.66,10.98,13.65,11.01,13.64,11.04,13.63,11.07,13.61,11.09,13.56,11.15,13.52,11.21,13.47,11.26,13.43,11.32,13.41,11.35,13.40,11.37,13.40,11.40,13.39,11.43,13.40,11.48,13.41,11.53,13.42,11.58,13.45,11.63,13.47,11.67,13.50,11.72,13.63,11.90,13.65,11.95,13.68,11.99,13.70,12.04,13.72,12.09,13.72,12.14,13.72,12.20,13.72,12.21,13.72,12.24,13.72,12.26,13.71,12.29,13.70,12.35,13.67,12.41,13.65,12.48,13.62,12.56,13.59,12.63,13.55,12.70,13.51,12.77,13.47,12.84,13.44,12.91,13.40,12.97,13.35,13.02,13.32,13.07,13.29,13.08,13.28,13.10,13.26,13.11,13.24,13.12,13.16,13.15,13.07,13.19,12.98,13.22,12.89,13.25,12.69,13.32,12.49,13.38,12.28,13.44,12.18,13.48,12.09,13.51,12.00,13.54,11.91,13.58,11.82,13.62,11.74,13.66,11.71,13.68,11.68,13.69,11.65,13.71,11.61,13.73,11.60,13.74,11.58,13.74,11.56,13.75,11.54,13.76,11.53,13.78,11.52,13.79,11.52,13.80,11.51,13.81,11.51,13.87,11.51,13.91,11.51,13.93,11.51,13.96,11.50,13.98,11.49,14.01,11.48,14.04,11.47,14.07,11.46,14.12,11.46,14.17,11.46,14.22,11.46,14.27,11.46,14.32,11.47,14.38,11.48,14.44,11.49,14.49,11.52,14.61,11.56,14.72,11.60,14.83,11.64,14.95,11.67,15.06,11.69,15.11,11.69,15.16,11.71,15.21,11.71,15.26,11.72,15.31,11.72,15.35,11.71,15.39,11.70,15.42,11.69,15.46,11.67,15.49,11.65,15.52,11.62,15.54,11.59,15.57,11.54,15.58,11.36,15.64,11.18,15.69,10.98,15.74,10.79,15.77,10.59,15.82,10.38,15.85,10.17,15.88,9.96,15.91,9.74,15.93,9.52,15.95,9.30,15.96,9.08,15.98,8.86,15.99,8.63,15.99,8.17,16.00,7.71,16.01,7.26,16.00,6.81,16.00,6.36,15.99,6.15,15.98,5.93,15.98,5.72,15.97,5.51,15.97,5.31,15.96,5.11,15.96,4.92,15.96,4.72,15.96,4.72,15.91,4.71,15.87,4.71,15.82,4.71,15.77,4.70,15.72,4.70,15.66,4.70,15.54,4.70,15.42,4.71,15.28,4.71,15.15,4.72,15.00,4.73,14.86,4.74,14.71,4.76,14.41,4.77,14.26,4.77,14.19,4.78,14.11,4.78,13.97,4.79,13.82,4.79,13.72,4.79,13.62,4.78,13.41,4.76,13.21,4.74,13.00,4.71,12.79,4.67,12.59,4.60,12.18,4.56,12.03,4.53,11.89,4.49,11.75,4.44,11.63,4.39,11.50,4.33,11.38,4.28,11.27,4.22,11.16,4.15,11.06,4.08,10.97,4.02,10.87,3.95,10.78,3.87,10.69,3.80,10.61,3.73,10.53,3.65,10.46,3.49,10.31,3.34,10.17,3.18,10.03,3.03,9.89,2.88,9.74,2.80,9.67,2.73,9.60,2.66,9.52,2.60,9.45,2.54,9.37,2.47,9.28,2.40,9.17,2.33,9.06,2.26,8.95,2.20,8.84,2.14,8.73,2.09,8.62,2.03,8.52,1.99,8.41,1.94,8.30,1.90,8.20,1.87,8.10,1.83,8.00,1.80,7.90,1.77,7.79,1.74,7.70,1.72,7.60,1.70,7.50,1.69,7.41,1.67,7.32,1.66,7.22,1.65,7.14,1.64,7.05,1.63,6.87,1.63,6.71,1.63,6.55,1.63,6.39,1.65,6.25,1.66,6.10,1.68,5.94,1.70,5.79,1.74,5.65,1.77,5.51,1.81,5.37,1.85,5.24,1.89,5.11,1.94,4.99,2.00,4.87,2.05,4.75,2.11,4.64,2.17,4.53,2.23,4.42,2.30,4.31,2.36,4.22,2.43,4.11,2.50,4.02,2.57,3.93,2.64,3.84,2.71,3.75,2.79,3.67,2.86,3.59,2.94,3.51,3.01,3.44,3.16,3.29,3.23,3.23,3.30,3.16,3.38,3.10,3.44,3.04,3.51,2.98,3.57,2.92,3.63,2.88,3.70,2.82,3.77,2.77,3.85,2.72,3.92,2.66,4.00,2.61,4.09,2.56,4.18,2.51,4.27,2.45,4.37,2.40,4.47,2.35,4.57,2.30,4.67,2.25,4.78,2.20,4.88,2.15,5.00,2.10,5.11,2.06,5.23,2.01,5.35,1.97,5.47,1.93,5.59,1.88,5.71,1.84,5.97,1.77,6.10,1.74,6.23,1.71,6.37,1.68,6.50,1.65,6.64,1.63,6.77,1.61]
					fill: Color.rgb(0xfb,0x6,0xa2)
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeWidth: 0.44
				},
				Polygon {
					points: [3.82,7.00,3.83,6.79,3.84,6.59,3.87,6.39,3.90,6.19,3.95,5.99,4.01,5.80,4.07,5.61,4.14,5.43,4.23,5.25,4.32,5.08,4.42,4.90,4.52,4.74,4.63,4.58,4.75,4.42,4.88,4.28,5.02,4.14,5.16,4.00,5.31,3.87,5.46,3.75,5.62,3.64,5.79,3.54,5.95,3.43,6.13,3.34,6.31,3.26,6.49,3.19,6.68,3.12,6.87,3.07,7.07,3.03,7.27,2.99,7.47,2.96,7.68,2.95,7.89,2.94,8.09,2.95,8.30,2.96,8.50,2.99,8.70,3.03,8.90,3.07,9.09,3.12,9.27,3.19,9.46,3.26,9.65,3.34,9.82,3.43,9.99,3.54,10.15,3.64,10.31,3.75,10.47,3.87,10.61,4.00,10.76,4.14,10.89,4.28,11.01,4.42,11.14,4.58,11.25,4.74,11.36,4.90,11.45,5.08,11.55,5.25,11.63,5.43,11.70,5.61,11.76,5.80,11.82,5.99,11.87,6.19,11.90,6.39,11.93,6.59,11.94,6.79,11.95,7.00,11.94,7.22,11.93,7.42,11.90,7.62,11.87,7.82,11.82,8.01,11.76,8.21,11.70,8.40,11.63,8.58,11.55,8.76,11.45,8.94,11.36,9.10,11.25,9.27,11.14,9.43,11.01,9.59,10.89,9.73,10.76,9.87,10.61,10.00,10.47,10.14,10.31,10.26,10.15,10.37,9.99,10.48,9.82,10.58,9.65,10.67,9.46,10.75,9.27,10.82,9.09,10.88,8.90,10.94,8.70,10.98,8.50,11.02,8.30,11.04,8.09,11.06,7.89,11.07,7.68,11.06,7.47,11.04,7.27,11.02,7.07,10.98,6.87,10.94,6.68,10.88,6.49,10.82,6.31,10.75,6.13,10.67,5.95,10.58,5.79,10.48,5.62,10.37,5.46,10.26,5.31,10.14,5.16,10.00,5.02,9.87,4.88,9.73,4.75,9.59,4.63,9.43,4.52,9.27,4.42,9.10,4.32,8.94,4.23,8.76,4.14,8.58,4.07,8.40,4.01,8.21,3.95,8.01,3.90,7.82,3.87,7.62,3.84,7.42,3.83,7.22]
					fill: Color.WHITE
					stroke: Color.WHITE
					strokeWidth: 0.05
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
				},
				Polygon {
					points: [8.27,5.15,8.21,5.24,8.12,5.29,8.03,5.33,7.93,5.34,7.83,5.33,7.73,5.30,7.66,5.25,7.59,5.17,6.77,4.03,6.74,3.99,6.73,3.95,6.73,3.90,6.73,3.86,6.76,3.77,6.82,3.68,6.89,3.60,6.98,3.54,7.08,3.49,7.18,3.46,7.32,3.45,7.49,3.43,7.93,3.42,8.36,3.43,8.54,3.45,8.67,3.49,8.81,3.51,8.93,3.55,9.03,3.61,9.10,3.68,9.15,3.76,9.16,3.84,9.15,3.94,9.10,4.03]
					fill: Color.rgb(0xfb,0x6,0xa2)
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeWidth: 0.44
				},
				Polygon {
					points: [6.46,5.69,6.50,5.79,6.51,5.89,6.48,5.99,6.45,6.09,6.39,6.16,6.32,6.23,6.23,6.28,6.13,6.30,4.72,6.44,4.67,6.44,4.63,6.43,4.59,6.41,4.55,6.39,4.49,6.32,4.45,6.23,4.42,6.11,4.41,6.01,4.42,5.89,4.45,5.79,4.50,5.66,4.58,5.51,4.78,5.12,5.01,4.76,5.12,4.61,5.21,4.51,5.30,4.40,5.40,4.31,5.49,4.26,5.59,4.23,5.68,4.23,5.75,4.26,5.83,4.32,5.89,4.41]
					fill: Color.rgb(0xfb,0x6,0xa2)
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeWidth: 0.44
				},
				Polygon {
					points: [6.01,7.53,6.12,7.55,6.21,7.59,6.29,7.66,6.35,7.73,6.39,7.82,6.41,7.93,6.41,8.02,6.37,8.11,5.79,9.42,5.77,9.46,5.74,9.49,5.70,9.51,5.67,9.53,5.58,9.54,5.48,9.54,5.36,9.50,5.27,9.46,5.17,9.40,5.09,9.32,5.01,9.22,4.91,9.07,4.69,8.70,4.48,8.31,4.41,8.15,4.36,8.03,4.32,7.89,4.29,7.76,4.29,7.65,4.32,7.55,4.36,7.48,4.43,7.43,4.52,7.39,4.62,7.39]
					fill: Color.rgb(0xfb,0x6,0xa2)
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeWidth: 0.44
				},
				Polygon {
					points: [7.38,8.84,7.45,8.75,7.54,8.70,7.63,8.66,7.73,8.65,7.83,8.66,7.93,8.69,8.01,8.74,8.07,8.82,8.91,9.97,8.93,10.01,8.94,10.05,8.95,10.09,8.94,10.13,8.90,10.22,8.85,10.30,8.77,10.38,8.68,10.45,8.58,10.49,8.48,10.52,8.35,10.54,8.17,10.56,7.73,10.57,7.29,10.56,7.12,10.54,6.99,10.51,6.84,10.49,6.73,10.44,6.63,10.39,6.56,10.32,6.52,10.24,6.51,10.16,6.52,10.06,6.57,9.97]
					fill: Color.rgb(0xfb,0x6,0xa2)
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeWidth: 0.44
				},
				Polygon {
					points: [9.20,8.30,9.17,8.20,9.16,8.10,9.17,8.00,9.22,7.90,9.28,7.83,9.36,7.76,9.45,7.72,9.54,7.70,10.96,7.55,11.00,7.55,11.04,7.56,11.08,7.58,11.12,7.61,11.18,7.68,11.22,7.77,11.25,7.88,11.25,7.99,11.25,8.10,11.21,8.20,11.17,8.32,11.09,8.49,10.89,8.87,10.65,9.24,10.55,9.38,10.46,9.48,10.37,9.59,10.27,9.68,10.17,9.73,10.08,9.76,9.99,9.76,9.91,9.73,9.83,9.67,9.78,9.58]
					fill: Color.rgb(0xfb,0x6,0xa2)
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeWidth: 0.44
				},
				Polygon {
					points: [9.65,6.46,9.55,6.44,9.46,6.39,9.37,6.33,9.32,6.25,9.27,6.16,9.25,6.06,9.26,5.97,9.29,5.87,9.87,4.57,9.90,4.54,9.92,4.51,9.96,4.49,10.00,4.47,10.08,4.45,10.18,4.46,10.30,4.49,10.40,4.54,10.49,4.59,10.57,4.67,10.65,4.77,10.75,4.92,10.98,5.29,11.09,5.49,11.18,5.68,11.25,5.84,11.29,5.96,11.34,6.10,11.36,6.23,11.36,6.34,11.34,6.44,11.30,6.51,11.23,6.56,11.14,6.60,11.03,6.60]
					fill: Color.rgb(0xfb,0x6,0xa2)
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeWidth: 0.44
				},
				Polygon {
					points: [7.61,7.00,7.62,6.96,7.63,6.91,7.67,6.85,7.71,6.81,7.73,6.80,7.78,6.78,7.82,6.78,7.86,6.78,7.91,6.80,7.95,6.81,7.98,6.85,8.01,6.88,8.03,6.91,8.04,6.96,8.04,7.00,8.04,7.05,8.03,7.09,8.01,7.13,7.98,7.16,7.95,7.19,7.91,7.21,7.86,7.22,7.82,7.23,7.78,7.22,7.73,7.21,7.71,7.19,7.67,7.16,7.64,7.13,7.63,7.09,7.62,7.05]
					fill: Color.WHITE
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeWidth: 0.44
				},
				Polygon {
					points: [7.62,7.00,7.62,6.96,7.62,6.96,7.62,6.95,7.62,6.94,7.63,6.92,7.63,6.92,7.63,6.91,7.64,6.90,7.64,6.88,7.67,6.86,7.67,6.85,7.68,6.84,7.68,6.83,7.70,6.82,7.71,6.82,7.72,6.81,7.74,6.80,7.76,6.79,7.77,6.79,7.78,6.79,7.78,6.79,7.82,6.79,7.82,6.77,7.78,6.77,7.77,6.77,7.76,6.78,7.75,6.78,7.73,6.79,7.71,6.80,7.69,6.81,7.69,6.81,7.68,6.82,7.67,6.83,7.66,6.84,7.66,6.84,7.63,6.87,7.63,6.88,7.62,6.89,7.62,6.90,7.61,6.91,7.61,6.92,7.61,6.94,7.61,6.95,7.60,6.96,7.60,6.96,7.60,7.00]
					fill: Color.BLACK
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeWidth: 0.44
				},
				Polygon {
					points: [7.82,6.79,7.85,6.79,7.86,6.79,7.87,6.79,7.88,6.79,7.90,6.80,7.92,6.81,7.94,6.82,7.95,6.82,7.96,6.83,7.97,6.84,7.97,6.85,7.98,6.86,7.99,6.88,8.00,6.88,8.01,6.90,8.02,6.91,8.02,6.92,8.02,6.92,8.03,6.94,8.03,6.95,8.03,6.96,8.03,6.96,8.03,6.99,8.04,7.01,8.04,7.00,8.06,7.00,8.06,7.00,8.05,6.99,8.05,6.96,8.05,6.96,8.04,6.95,8.04,6.94,8.04,6.92,8.03,6.91,8.03,6.90,8.03,6.89,8.02,6.88,8.01,6.87,8.01,6.86,7.99,6.84,7.98,6.84,7.98,6.82,7.97,6.82,7.96,6.81,7.95,6.81,7.93,6.79,7.91,6.79,7.89,6.78,7.88,6.78,7.88,6.78,7.87,6.77,7.85,6.77,7.82,6.77]
					fill: Color.BLACK
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeWidth: 0.44
				},
				Polygon {
					points: [8.04,7.00,8.04,7.00,8.03,7.01,8.03,7.04,8.03,7.05,8.03,7.06,8.03,7.06,8.02,7.08,8.02,7.09,8.02,7.10,8.01,7.11,8.01,7.11,8.00,7.12,7.99,7.13,7.97,7.15,7.97,7.16,7.96,7.17,7.95,7.17,7.94,7.18,7.93,7.19,7.92,7.19,7.91,7.20,7.90,7.20,7.88,7.21,7.87,7.21,7.86,7.21,7.85,7.21,7.82,7.21,7.82,7.22,7.82,7.22,7.82,7.24,7.82,7.23,7.83,7.23,7.85,7.23,7.87,7.23,7.88,7.23,7.88,7.23,7.89,7.22,7.91,7.21,7.92,7.21,7.93,7.20,7.94,7.20,7.95,7.19,7.96,7.19,7.97,7.18,7.98,7.17,7.98,7.17,8.01,7.14,8.01,7.13,8.02,7.13,8.03,7.11,8.03,7.10,8.03,7.10,8.04,7.08,8.04,7.07,8.04,7.06,8.05,7.05,8.05,7.04,8.05,7.01,8.05,7.01,8.06,7.00]
					fill: Color.BLACK
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeWidth: 0.44
				},
				Polygon {
					points: [7.82,7.22,7.82,7.22,7.81,7.21,7.78,7.21,7.78,7.21,7.77,7.21,7.76,7.21,7.74,7.20,7.73,7.19,7.72,7.19,7.71,7.19,7.70,7.18,7.70,7.17,7.69,7.17,7.68,7.16,7.67,7.15,7.64,7.11,7.64,7.11,7.63,7.10,7.63,7.08,7.62,7.06,7.62,7.06,7.62,7.05,7.62,7.04,7.62,7.00,7.60,7.00,7.60,7.04,7.60,7.05,7.61,7.06,7.61,7.07,7.61,7.08,7.61,7.10,7.62,7.10,7.62,7.11,7.63,7.13,7.66,7.16,7.67,7.17,7.67,7.18,7.69,7.19,7.70,7.19,7.70,7.20,7.71,7.20,7.72,7.21,7.73,7.21,7.75,7.23,7.76,7.23,7.77,7.23,7.78,7.23,7.81,7.23,7.82,7.24,7.82,7.24]
					fill: Color.BLACK
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeWidth: 0.44
				},
				SVGPath {
					fill: Color.rgb(0xfb,0x6,0xa2)
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeWidth: 0.44
					content: "M8.63,7.00 C8.63,7.41 8.30,7.75 7.88,7.75 C7.47,7.75 7.14,7.41 7.14,7.00 C7.14,6.59 7.47,6.25 7.88,6.25 C8.30,6.25 8.63,6.59 8.63,7.00 Z "
},
]
}
}
}
