

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

/*
 * http://marathoncode.blogspot.com.br/search/label/threshold
 * 
 * 
 * http://stackoverflow.com/questions/13269556/strange-behavior-of-class-getresource-and-classloader-getresource-in-executa
 * 
 * */

public class App {

	public static void main(String[] args)  {
		BufferedImage image = null;
		try {
			System.out.println(System.getProperty("user.dir"));
			String path [] = {
				"file:///Dev/workspace/ThresholdProcessImage/src/pictures/colors.jpg"
				,"file:///Dev/workspace/ThresholdProcessImage/src/pictures/highway.jpg"
				,"file:///Dev/workspace/ThresholdProcessImage/src/pictures/rain.jpg"
				,"http://www.mkyong.com/image/mypic.jpg"
				,"http://3.bp.blogspot.com/-MybVmfBEjXk/T1wzkoZ5x0I/AAAAAAAAAqw/RPAVYnFDJz8/s1600/che3.jpg"
			};
			//URL file= new URL(path[2]);
			URL in = App.class.getClass().getResource("./src/pictures/highway.jpg");
			File file = new File("./src/pictures/highway.jpg");
			image = ImageIO.read(file);
			int w = image.getWidth();
			int h = image.getHeight();
			int pixelsImage[] = image.getRGB(0, 0, w, h, null, 0, w);
			pixelsImage = effectThreshold(pixelsImage, w, h);

			image.setRGB(0, 0, w, h, pixelsImage, 0, w);
			ImageIO.write(image, "JPG", new File("D:/Dev/workspace/ThresholdProcessImage/src/pictures/colorThreshold.jpg"));
			System.out.println("finish");
		} catch(IOException ioex) {
			System.out.println(ioex.getMessage());
		}
	}
	
	public static int[] effectThreshold(int pixelsImage[], int w, int h) {
		double mediaR = 0.0
			  ,mediaG = 0.0
			  ,mediaB = 0.0
			  ,mediaT = 0.0;
			/**
			 * a matrix da imagem eh invertida
			 * o loop e feito atraves da linha/coluna
			 * passa por todas as linhas de uma coluna ate a ultima linha da coluna corrente
			 * */
			
		for(int i=0; i<w; i++) {
			for(int j=0; j<h; j++) {
				// j*w+i = num de linhas x qtd colunas + a qtd de linhas ja processadas da matriz
				Color color = new Color(pixelsImage[j*w+i]);
				int r = color.getRed(), g = color.getGreen(), b = color.getBlue();
				mediaR += (double) r / (w*h);
				mediaG += (double) g / (w*h);
				mediaB += (double) b / (w*h);
				mediaT += (double) (r + g + b) / (3.0 * w * h);
			}
		}
		System.out.printf("%f %f %f %f\n", mediaR, mediaG, mediaB, mediaT);
		int threshold = (int) mediaT;
		for(int i=0; i<w; i++) {
			for(int j=0; j<h; j++) {
				Color color = new Color(pixelsImage[j*w+i]);
				int r = color.getRed(), g = color.getGreen(), b = color.getBlue();
				if(r > threshold || g > threshold || b > threshold){
					r = 255; g = 255; b = 255;
				} else {
					r = 0; g = 0; b = 0;
				}
				pixelsImage[j*w+i] = new Color(r,g,b).getRGB();
			}
		}
		return pixelsImage;
	}
	
	public static int[] invertEffectThreshold(int pixelsImage[], int w, int h) {
		double mediaR = 0.0
			  ,mediaG = 0.0
			  ,mediaB = 0.0
			  ,mediaT = 0.0;	
		// aqui um loop processando coluna/linha
		// passa por todas as colunas de uma linha ate a ultima coluna da linha corrente
		// um loop que ja estamos acostumados a fazer em matrizes numericas
		for(int i=0; i<h; i++) {
			for(int j=0; j<w; j++) {
				// i*w+j = num de linhas x qtd colunas + a qtd de linhas ja processadas da matriz
				Color color = new Color(pixelsImage[i*h+j]);
				int r = color.getRed(), g = color.getGreen(), b = color.getBlue();
				mediaR += (double) r / (w*h);
				mediaG += (double) g / (w*h);
				mediaB += (double) b / (w*h);
				mediaT += (double) (r + g + b) / (3.0 * w * h);
			}
		}
		System.out.printf("%f %f %f %f\n", mediaR, mediaG, mediaB, mediaT);
		int threshold = (int) mediaT;
		for(int i=0; i<h; i++) {
			for(int j=0; j<w; j++) {
				Color color = new Color(pixelsImage[i*w+j]);
				int r = color.getRed(), g = color.getGreen(), b = color.getBlue();
				if(r > threshold || g > threshold || b > threshold){
					r = 255; g = 255; b = 255;
				} else {
					r = 0; g = 0; b = 0;
				}
				pixelsImage[i*w+j] = new Color(r,g,b).getRGB();
			}
		}

		return pixelsImage;
	}
}
