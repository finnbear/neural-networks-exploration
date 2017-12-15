package com.finnbear.nnvisualizer;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Main extends JFrame {

	private ArrayList<Neuron[]> layers;
	
	public static void main(String[] args) {
		Runnable starter = new Runnable() {
			@Override
			public void run() {
				new Main();
			}
		};
		
		SwingUtilities.invokeLater(starter);
	}
	
	private BufferedImage context;
	private JPanel contextPanel;
	private JLabel contextRender;
	RenderingHints antialiasing;
	
	public Main() {
		super("Neural Network Visualizer");
		
		layers = new ArrayList<Neuron[]>();
		
		layers.add(new Neuron[] {new Neuron("x₁", null), new Neuron("x₂", null)});
		layers.add(new Neuron[] {new Neuron("h₁", null), new Neuron("h₂", null), new Neuron("h₃", null), new Neuron("h₄", null)});
		layers.add(new Neuron[] {new Neuron("y", null)});
		
		int width = 1000;
		int height = 500;
		int padding = 6;
		
		contextPanel = new JPanel();
		
		antialiasing = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		context =  new BufferedImage(width + (2 * padding), height + (2 * padding), BufferedImage.TYPE_INT_RGB);
		contextRender = new JLabel(new ImageIcon(context));
		
		contextPanel.add(contextRender);
		contextPanel.setSize(width + padding * 2, height + padding * 2);
		JFrame.setDefaultLookAndFeelDecorated(true);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setContentPane(contextPanel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.paint();
		this.setVisible(true);
	}
	
	public void paint() {
		Graphics2D graphics = context.createGraphics();
		graphics.setRenderingHints(antialiasing);
		
		Font font = graphics.getFont();

		Font nameFont = font.deriveFont(Font.BOLD, 20f);
		graphics.setFont(nameFont);
		FontMetrics nameFontMetrics = graphics.getFontMetrics();

		Font valueFont = font.deriveFont(Font.PLAIN, 18f);
		graphics.setFont(valueFont);
		FontMetrics valueFontMetrics = graphics.getFontMetrics();

		Font synapseFont = font.deriveFont(Font.PLAIN, 16);
		graphics.setFont(synapseFont);
		FontMetrics synapseFontMetrics = graphics.getFontMetrics();
		

		
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, context.getWidth(), context.getHeight());
		
		int xPerLayer = context.getWidth() / layers.size();
		
		// Calculate neurons
		for (int i = 0; i < layers.size(); i++) {
			Neuron[] layer = layers.get(i);
			
			int yPerLayer = context.getHeight() / layer.length;
			
			for (int j = 0; j < layer.length; j++) {
				Neuron neuron = layer[j];
				
				int x = (int)((i + 0.5f) * xPerLayer);
				int y = (int)((j + 0.5f) * yPerLayer);
				
				Point2D point = new Point(x, y);
				neuron.setPosition(point);
				
				double radius = Math.min(xPerLayer, yPerLayer) / 2.5;
				
				neuron.setRadius(radius);
			}
		}
		
		// Draw synapses
		graphics.setColor(Color.BLACK);
		graphics.setFont(synapseFont);
		
		for (int i = 0; i < layers.size() - 1; i++) {
			Neuron[] layer = layers.get(i);
			Neuron[] nextLayer = layers.get(i + 1);
			
			for (int j = 0; j < layer.length; j++) {
				for (int k = 0; k < nextLayer.length; k++) {
					Neuron neuron1 = layer[j];
					Neuron neuron2 = nextLayer[k];
					
					double x1 = neuron1.getPosition().getX();
					double y1 = neuron1.getPosition().getY();
					
					double x2 = neuron2.getPosition().getX();
					double y2 = neuron2.getPosition().getY();
					
					Path2D line = new Path2D.Double();
					line.moveTo(x1, y1);
					line.lineTo(x2, y2);
					line.closePath();

					graphics.setStroke(new BasicStroke(3 ));
					graphics.draw(line);

					double weight = neuron1.getRadius() / Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));

					weight *= 2;
					weight = 1 - weight;

					double xc = weight * x1 + (1 - weight) * x2;
					double yc = weight * y1 + (1 - weight) * y2;
					
					double rc = Math.atan2(y2 - y1, x2 - x1);
					
					graphics.translate(xc, yc);
					graphics.rotate(rc);
					//graphics.drawString("Sigmoid", 0, 2);
					graphics.rotate(-rc);
					graphics.translate(-xc, -yc);
				}
			}
		}
		
		// Draw neurons
		graphics.setStroke(new BasicStroke(3.0f));
		
		for (int i = 0; i < layers.size(); i++) {
			Neuron[] layer = layers.get(i);
			
			int yPerLayer = context.getHeight() / layer.length;
			
			for (int j = 0; j < layer.length; j++) {
				Neuron neuron = layer[j];
		
				Ellipse2D outline = Main.getCircleByCenter(neuron.getPosition(), neuron.getRadius());

				Float bias = neuron.getBias();
				if (bias == null) {
					graphics.setColor(Color.WHITE);
					graphics.setStroke(new BasicStroke(3.0f));
				} else {
					float color = bias / 4 + 0.75f;
					graphics.setColor(new Color(color, color, color));
					graphics.setStroke(new BasicStroke(2.0f * (bias + 1)));
				}

				graphics.fill(outline);
				graphics.setColor(Color.BLACK);
				graphics.draw(outline);

				graphics.setFont(nameFont);
				Rectangle2D nameStringBounds = nameFontMetrics.getStringBounds(neuron.getNameString(), graphics);
				graphics.drawString(neuron.getNameString(), (int)(neuron.getPosition().getX() - nameStringBounds.getWidth() / 2), (int)neuron.getPosition().getY());

				graphics.setFont(valueFont);
				Rectangle2D valueStringBounds = valueFontMetrics.getStringBounds(neuron.getValueString(), graphics);
				graphics.drawString(neuron.getValueString(), (int)(neuron.getPosition().getX() - valueStringBounds.getWidth() / 2), (int)(neuron.getPosition().getY() + nameStringBounds.getHeight()));
			}
		}
		
		contextRender.repaint();

		try {
			ImageIO.write(context, "png", new File("network.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static Ellipse2D getCircleByCenter(Point2D center, double radius)
    {
        Ellipse2D.Double myCircle = new Ellipse2D.Double(center.getX() - radius, center.getY() - radius, 2 * radius, 2 * radius);
        return myCircle;
    }
}
